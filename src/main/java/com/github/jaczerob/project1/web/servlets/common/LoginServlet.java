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
import com.github.jaczerob.project1.web.responses.common.login.AlreadyLoggedInResponse;
import com.github.jaczerob.project1.web.responses.common.login.InvalidLoginDetailsResponse;
import com.github.jaczerob.project1.web.responses.common.login.SuccessfulLoginResponse;
import com.github.jaczerob.project1.web.servlets.Servlet;

/**
 * A servlet exposing the /login endpoint for logging in users
 * @author Jacob
 * @since 0.9
 * @version 0.9
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
        Response response;

        // get username and password parameters
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // make sure the username and password are not null
        if (username != null && password != null) {
            // get the user session and create if needed
            HttpSession session = req.getSession(true);

            // check if the user is already in the session
            if (session.getAttribute("user") != null) {
                // there is already a user in the session so they are logged in already
                response = new AlreadyLoggedInResponse(resp);
            } else {
                try {
                    // try to log in the user by getting them from the user service and checking password.
                    // my login method returns Optional<User> based on whether the user details match
                    // something in the database.
                    Optional<User> user = userService.loginUser(username, password);
                    if (!user.isPresent()) {
                        // login details are incorrect
                        response = new InvalidLoginDetailsResponse(resp);
                    } else {
                        // login details are correct
                        User gotUser = user.get();

                        // put the user in the user session as an attribute
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
