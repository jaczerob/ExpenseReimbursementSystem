package com.github.jaczerob.project1.web.servlets.manager;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.responses.manager.employees.EmployeesResponse;

/**
 * A servlet exposing the /employees endpoint for showing all employees
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
@WebServlet(name="EmployeesServlet", urlPatterns={"/employees"})
public class EmployeesServlet extends ManagerServlet {
    private ObjectMapper objectMapper;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        userService = (UserService) this.getServletContext().getAttribute("userService");
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Employee> employees = userService.getAllEmployees();
        resp.getWriter().write(this.objectMapper.writeValueAsString(new EmployeesResponse(resp, employees)));
    }
}
