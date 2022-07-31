package com.github.jaczerob.project1.web.servlets.manager;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.users.Manager;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.NotAuthorizedResponse;
import com.github.jaczerob.project1.web.servlets.Servlet;


/**
 * An abstract servlet providing authentication for all Manager endpoints
 * @author Jacob
 * @since 0.9
 * @version 0.10
 */
public abstract class ManagerServlet extends Servlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        if (session.getAttribute("user") == null || !(session.getAttribute("user") instanceof Manager)) {
            Response response = new NotAuthorizedResponse(resp);
            resp.getWriter().write(new ObjectMapper().writeValueAsString(response));
            return;
        }
        
        super.service(req, resp);
    }
}
