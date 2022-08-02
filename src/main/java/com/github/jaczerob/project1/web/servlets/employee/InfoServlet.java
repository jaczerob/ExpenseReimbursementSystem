package com.github.jaczerob.project1.web.servlets.employee;

import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.ServerError;
import com.github.jaczerob.project1.web.responses.employee.information.UserInformationResponse;

/**
 * A servlet exposing the /info endpoint for checking user information
 * @author Jacob
 * @since 0.10
 * @version 0.10
 */
@WebServlet(name="InfoServlet", urlPatterns={"/info"})
public class InfoServlet extends EmployeeServlet {
    private ReimbursementRequestService reimbursementRequestService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        reimbursementRequestService = (ReimbursementRequestService) this.getServletContext().getAttribute("reimbursementRequestService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        super.init();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        
        Response response;
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

    
        try {
            List<ReimbursementRequest> pendingRequests = reimbursementRequestService.getAllPendingEmployeeReimbursements(user.getID());
            List<ReimbursementRequest> resolvedRequests = reimbursementRequestService.getAllResolvedEmployeeReimbursements(user.getID());
            response = new UserInformationResponse(resp, user, pendingRequests, resolvedRequests);
        } catch (IllegalArgumentException e) {
            response = new ServerError(resp);
        }


        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
