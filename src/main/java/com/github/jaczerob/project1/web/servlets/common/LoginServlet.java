package com.github.jaczerob.project1.web.servlets.common;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.InvalidParametersResponse;
import com.github.jaczerob.project1.web.responses.login.AlreadyLoggedInResponse;
import com.github.jaczerob.project1.web.responses.login.InvalidLoginDetailsResponse;
import com.github.jaczerob.project1.web.responses.login.SuccessfulLoginResponse;
import com.github.jaczerob.project1.web.servlets.Servlet;

/**
 * A servlet exposing the /login endpoint for logging in users
 * @author Jacob
 * @since 0.9
 * @since 0.9
 */
@WebServlet(name="LoginServlet", urlPatterns={"/login"})
public class LoginServlet extends Servlet {
    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        userService = (UserService) this.getServletContext().getAttribute("userService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Response response;

        resp.setContentType("application/json");

        if (username != null && password != null) {
            HttpSession session = req.getSession(true);

            if (session.getAttribute("user") != null) {
                response = new AlreadyLoggedInResponse(resp);
            } else {
                try {
                    Optional<User> user = userService.loginUser(username, password);

                    if (!user.isPresent()) {
                        response = new InvalidLoginDetailsResponse(resp);
                    } else {
                        User gotUser = user.get();
                        session.setAttribute("user", gotUser);
        
                        response = new SuccessfulLoginResponse(resp);
                    }
                } catch (IllegalArgumentException e) {
                    response = new InvalidLoginDetailsResponse(resp);
                }
            }
        } else {
            response = new InvalidParametersResponse(resp, "username", "password");
        }

        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
