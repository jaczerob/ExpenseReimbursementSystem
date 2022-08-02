package com.github.jaczerob.project1.web.servlets.manager;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.ServerError;
import com.github.jaczerob.project1.web.responses.manager.request.IDTypeErrorResponse;
import com.github.jaczerob.project1.web.responses.manager.request.PendingRequestsResponse;

@WebServlet(name="RequestViewPendingServlet", urlPatterns={"/request/view/pending/*"})
public class RequestViewPendingServlet extends ManagerServlet {
    private static Logger logger = LogManager.getLogger(RequestViewPendingServlet.class);
    
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
        Response response = null;

        List<ReimbursementRequest> requests = null;
        if (req.getRequestURI().split("/").length == 4) {
            requests = reimbursementRequestService.getAllPendingReimbursements();
            response = new PendingRequestsResponse(resp, requests);
        } else {
            try {
                int employeeID = Integer.parseInt(req.getRequestURI().split("/")[5]);
                requests = reimbursementRequestService.getAllPendingEmployeeReimbursements(employeeID);
                response = new PendingRequestsResponse(resp, employeeID, requests);
            } catch (NumberFormatException ex) {
                response = new IDTypeErrorResponse(resp);
            } catch (IllegalArgumentException e) {
                logger.error("error servicing /request/view/pending/*", e);
                response = new ServerError(resp);
            }
        }

        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
