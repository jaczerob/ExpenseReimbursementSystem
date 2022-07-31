package com.github.jaczerob.project1.web.servlets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An abstract servlet providing base logging functionality to all Servlets
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
public abstract class Servlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger(Servlet.class);
    
    @Override
    public void init() throws ServletException {
        logger.info(String.format("%s initializing", this.getClass().getSimpleName()));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info(String.format("servicing %s method to URL %s", req.getMethod(), req.getRequestURL().toString()));
        super.service(req, resp);
    }

    @Override
    public void destroy() {
        logger.info(String.format("%s destroying", this.getClass().getSimpleName()));
    }
}
