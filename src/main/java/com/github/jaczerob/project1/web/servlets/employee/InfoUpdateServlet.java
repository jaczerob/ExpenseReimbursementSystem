package com.github.jaczerob.project1.web.servlets.employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.ServerError;
import com.github.jaczerob.project1.web.responses.employee.update.NoUpdateResponse;
import com.github.jaczerob.project1.web.responses.employee.update.SuccessfulUpdateResponse;

@WebServlet(name="InfoUpdateServlet", urlPatterns={"/info/update"})
public class InfoUpdateServlet extends EmployeeServlet {
    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        userService = (UserService) this.getServletContext().getAttribute("userService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        super.init();
    }

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        Employee employee = (Employee) session.getAttribute("user");
        Map<String, String> params = new HashMap<>();
        
        try (
            InputStreamReader reader = new InputStreamReader(req.getInputStream());
            BufferedReader br = new BufferedReader(reader)
        ) {
            for (String param : br.readLine().split("&")) {
                String[] keyValue = param.split("=");
                params.put(keyValue[0], keyValue[1]);
            }
        }

        String newUsername = params.get("username");
        String newPassword = params.get("password");
        String newEmail = params.get("email");

        Response response = null;

        if (newUsername == null && newPassword == null && newEmail == null) {
            response = new NoUpdateResponse(resp);
        } else {
            if (newUsername != null) employee.setUsername(newUsername);
            if (newPassword != null) employee.setPassword(newPassword);
            if (newEmail != null) employee.setEmail(newEmail);

            try {
                userService.updateUser(employee);
                response = new SuccessfulUpdateResponse(resp);
            } catch (RecordNotExistsException exc) {
                response = new ServerError(resp);
            }
        }

        resp.getWriter().write(this.objectMapper.writeValueAsString(response));
    }
}
