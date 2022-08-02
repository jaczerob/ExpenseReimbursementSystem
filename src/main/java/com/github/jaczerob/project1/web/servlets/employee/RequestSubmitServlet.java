package com.github.jaczerob.project1.web.servlets.employee;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.requests.PendingReimbursementRequest;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.ServerError;
import com.github.jaczerob.project1.web.responses.employee.request.AmountTypeErrorResponse;
import com.github.jaczerob.project1.web.responses.employee.request.SuccessfulSubmitResponse;

@WebServlet(name="RequestSubmitServlet", urlPatterns={"/request/submit"})
public class RequestSubmitServlet extends EmployeeServlet {
    private static Logger logger = LogManager.getLogger(RequestSubmitServlet.class);
    
    private ReimbursementRequestService reimbursementRequestService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        reimbursementRequestService = (ReimbursementRequestService) this.getServletContext().getAttribute("reimbursementRequestService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        Employee employee = (Employee) session.getAttribute("user");
        Response response = null;

        String type = req.getParameter("type");
        float amount;
        
        try {
            amount = Float.parseFloat(req.getParameter("amount"));
            reimbursementRequestService.addReimbursementRequest(new PendingReimbursementRequest(0, employee.getID(), amount, type));
            response = new SuccessfulSubmitResponse(resp);
        } catch (NumberFormatException e) {
            response = new AmountTypeErrorResponse(resp);
        } catch (IllegalArgumentException e) {
            logger.error("error servicing /request/submit", e);
            response = new ServerError(resp);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
