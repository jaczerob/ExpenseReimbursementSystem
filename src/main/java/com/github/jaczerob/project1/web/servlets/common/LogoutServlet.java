package com.github.jaczerob.project1.web.servlets.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.logout.NotLoggedInResponse;
import com.github.jaczerob.project1.web.responses.common.logout.SuccessfullyLoggedOutResponse;
import com.github.jaczerob.project1.web.servlets.Servlet;

/**
 * A servlet exposing the /logout endpoint for logging out users
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
@WebServlet(name="LogoutServlet", urlPatterns={"/logout"})
public class LogoutServlet extends Servlet {
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(true);
        Response response;

        if (session.getAttribute("user") == null) {
            response = new NotLoggedInResponse(resp);
        } else {
            session.invalidate();
            response = new SuccessfullyLoggedOutResponse(resp);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
