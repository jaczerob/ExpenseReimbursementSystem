package com.github.jaczerob.project1.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.services.UserService;

@WebServlet(name="RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static Logger logger = LogManager.getLogger(RegisterServlet.class);

    private UserService userService;

    @Override
    public void destroy() {
        logger.info(String.format("%s destroying", this.getClass().getSimpleName()));
    }

    @Override
    public void init() throws ServletException {
        logger.info(String.format("%s initializing", this.getClass().getSimpleName()));
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        response.setContentType("text/plain");

        if (username != null && email != null) {
            Employee employee = new Employee(0, username, email, "password");

            try {
                this.userService.registerUser(employee);
                response.setStatus(201);
                response.getWriter().write("Success");
            } catch (RecordAlreadyExistsException e) {
                response.setStatus(400);
                response.getWriter().write("Username or email already taken");
            } catch (IllegalArgumentException e) {
                response.setStatus(400);
                response.getWriter().write("Entry parameters are not valid");
            }
        } else {
            response.setStatus(400);
            response.getWriter().write("Parameters error");
        }
    }
}
