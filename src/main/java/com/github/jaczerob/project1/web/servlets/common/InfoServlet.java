package com.github.jaczerob.project1.web.servlets.common;

import javax.servlet.annotation.WebServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.NotAuthorizedResponse;
import com.github.jaczerob.project1.web.responses.employee.information.UserInformationResponse;
import com.github.jaczerob.project1.web.servlets.Servlet;

/**
 * A servlet exposing the /info endpoint for checking user information
 * @author Jacob
 * @since 0.10
 * @version 0.10
 */
@WebServlet(name="InfoServlet", urlPatterns={"/info"})
public class InfoServlet extends Servlet {
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        
        Response response;
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response = new NotAuthorizedResponse(resp);
        } else {
            response = new UserInformationResponse(resp, user);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
