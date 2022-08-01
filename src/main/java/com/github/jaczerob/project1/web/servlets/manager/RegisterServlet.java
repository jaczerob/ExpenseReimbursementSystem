package com.github.jaczerob.project1.web.servlets.manager;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.services.MailService;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.InvalidParametersResponse;
import com.github.jaczerob.project1.web.responses.common.ServerError;
import com.github.jaczerob.project1.web.responses.manager.register.InvalidRegistrationDetailsResponse;
import com.github.jaczerob.project1.web.responses.manager.register.LoginDetailsExistResponse;
import com.github.jaczerob.project1.web.responses.manager.register.SuccessfulRegistrationResponse;

/**
 * A servlet exposing the /register endpoint for registering users
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
@WebServlet(name="RegisterServlet", urlPatterns={"/register"})
public class RegisterServlet extends ManagerServlet {
    private static Logger logger = LogManager.getLogger(RegisterServlet.class);
    
    private ObjectMapper objectMapper;
    private UserService userService;
    private MailService mailService;

    @Override
    public void init() throws ServletException {
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        userService = (UserService) this.getServletContext().getAttribute("userService");
        mailService = (MailService) this.getServletContext().getAttribute("mailService");
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        Response response;

        if (username != null && email != null) {
            String tempPassword = "password";
            Employee employee = new Employee(0, email, username, tempPassword);

            try {
                this.userService.registerUser(employee);
                this.mailService.sendEmail(
                    email, 
                    String.format("Welcome to ERS, %s", username), 
                    String.format("Your temporary password is %s. Please change this using the /info/update endpoint and providing a new password form.", tempPassword)
                );
                
                response = new SuccessfulRegistrationResponse(resp);
            } catch (RecordAlreadyExistsException e) {
                response = new LoginDetailsExistResponse(resp);
            } catch (IllegalArgumentException e) {
                response = new InvalidRegistrationDetailsResponse(resp);
            } catch (MessagingException e) {
                logger.error("error in the mail service", e);
                response = new ServerError(resp);
            }
        } else {
            response = new InvalidParametersResponse(resp, "username", "email");
        }

        resp.getWriter().write(this.objectMapper.writeValueAsString(response));
    }
}
