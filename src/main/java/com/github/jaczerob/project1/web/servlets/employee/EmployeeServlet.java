package com.github.jaczerob.project1.web.servlets.employee;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.NotAuthorizedResponse;
import com.github.jaczerob.project1.web.servlets.Servlet;


/**
 * An abstract servlet providing authentication for all Employee endpoints
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
public abstract class EmployeeServlet extends Servlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        if (!(session.getAttribute("user") instanceof Employee)) {
            Response response = new NotAuthorizedResponse(resp);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
            return;
        }
        
        super.service(req, resp);
    }
}
