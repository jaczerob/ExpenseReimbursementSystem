package com.github.jaczerob.project1.web.servlets.manager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.InvalidParametersResponse;
import com.github.jaczerob.project1.web.responses.register.InvalidRegistrationDetailsResponse;
import com.github.jaczerob.project1.web.responses.register.LoginDetailsExistResponse;
import com.github.jaczerob.project1.web.responses.register.SuccessfulRegistrationResponse;

/**
 * A servlet exposing the /register endpoint for registering users
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
@WebServlet(name="RegisterServlet", urlPatterns={"/register"})
public class RegisterServlet extends ManagerServlet {
    private ObjectMapper objectMapper;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        userService = (UserService) this.getServletContext().getAttribute("userService");
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        resp.setContentType("application/json");
        Response response;

        if (username != null && email != null) {
            Employee employee = new Employee(0, username, email, "password");

            try {
                this.userService.registerUser(employee);
                response = new SuccessfulRegistrationResponse(resp);
            } catch (RecordAlreadyExistsException e) {
                response = new LoginDetailsExistResponse(resp);
            } catch (IllegalArgumentException e) {
                response = new InvalidRegistrationDetailsResponse(resp);
            }
        } else {
            response = new InvalidParametersResponse(resp, "username", "email");
        }

        resp.getWriter().write(this.objectMapper.writeValueAsString(response));
    }
}
