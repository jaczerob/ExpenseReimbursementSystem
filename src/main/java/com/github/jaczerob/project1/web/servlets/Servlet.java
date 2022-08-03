package com.github.jaczerob.project1.web.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An abstract servlet providing base logging functionality to all Servlets
 * @author Jacob
 * @since 0.9
 * @version 0.12
 */
public abstract class Servlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger(Servlet.class);
    
    @Override
    public void init() throws ServletException {
        logger.info("{} initializing", this.getClass().getSimpleName());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        StringBuilder formatParams = new StringBuilder();
        for (Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
            if (entry.getKey().equals("password")) {
                formatParams.append(String.format("%s=******", entry.getKey()));
            } else {
                formatParams.append(String.format("%s=%s", entry.getKey(), String.join(" ", entry.getValue())));
            }

            formatParams.append("&");
        }

        String formattedParams = formatParams.toString();
        if (formattedParams.isEmpty()) {
            logger.info("servicing {} method to URL {}", req.getMethod(), req.getRequestURL());
        } else {
            logger.info("servicing {} method to URL {} with parameters: {}", req.getMethod(), req.getRequestURL(), formatParams.substring(0, formatParams.length() - 1));
        }

        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            this.doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    /**
     * Just like PUT except it can be non-idempotent and only requires partial represenation of a resource
     * @param req an HttpServletRequest object that contains the request the client has made of the servlet
     * @param resp an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if the request for the PATCH could not be handled
     * @throws IOExceptionif an input or output error is detected when the servlet handles the PATCH request
     */
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol = req.getProtocol();
        String msg = "This method is not supported on this endpoint.";
        if (protocol.endsWith("1.1")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, msg);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
    }

    @Override
    public void destroy() {
        logger.info("{} destroying", this.getClass().getSimpleName());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
