package com.github.jaczerob.project1.web.responses.manager.employees;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a manager wants to see all employees
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
public class EmployeesResponse extends Response {
    private List<Employee> employees;

    /**
     * Constructs a new EmployeesResponse
     * Sets the status code to 200 (Bad Request)
     * @param resp The raw HTTP response
     */
    public EmployeesResponse(HttpServletResponse resp, List<Employee> employees) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("Here is the list of all registered employees.");
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }
}
