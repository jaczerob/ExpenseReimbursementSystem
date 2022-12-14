package com.github.jaczerob.project1.web.servlets.manager;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.requests.PendingReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.users.Manager;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.services.MailService;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.services.UserService;
import com.github.jaczerob.project1.web.responses.Response;
import com.github.jaczerob.project1.web.responses.common.ServerError;
import com.github.jaczerob.project1.web.responses.manager.request.IDTypeErrorResponse;
import com.github.jaczerob.project1.web.responses.manager.request.NoIDSetResponse;
import com.github.jaczerob.project1.web.responses.manager.request.RequestAlreadyResolvedResponse;
import com.github.jaczerob.project1.web.responses.manager.request.RequestNotExistsResponse;
import com.github.jaczerob.project1.web.responses.manager.request.SuccessfullyResolvedRequestResponse;

@WebServlet(name="RequestDenyServlet", urlPatterns={"/request/deny/*"})
public class RequestDenyServlet extends ManagerServlet {
    private static Logger logger = LogManager.getLogger(RequestDenyServlet.class);
    
    private ReimbursementRequestService reimbursementRequestService;
    private ObjectMapper objectMapper;
    private MailService mailService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        reimbursementRequestService = (ReimbursementRequestService) this.getServletContext().getAttribute("reimbursementRequestService");
        objectMapper = (ObjectMapper) this.getServletContext().getAttribute("objectMapper");
        mailService = (MailService) this.getServletContext().getAttribute("mailService");
        userService = (UserService) this.getServletContext().getAttribute("userService");
        super.init();
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response = null;
        if (req.getRequestURI().split("/").length == 3) {
            response = new NoIDSetResponse(resp);
            resp.getWriter().write(objectMapper.writeValueAsString(response));
            return;
        }

        HttpSession session = req.getSession(false);
        Manager manager = (Manager) session.getAttribute("user"); 

        try {
            int reimbursementRequestID = Integer.parseInt(req.getRequestURI().split("/")[3]);
            Optional<ReimbursementRequest> request = reimbursementRequestService.getReimbursementRequest(reimbursementRequestID);
            if (!(request.orElse(null) instanceof PendingReimbursementRequest)) {
                response = new RequestAlreadyResolvedResponse(resp);
            } else {
                PendingReimbursementRequest pendingReimbursementRequest = (PendingReimbursementRequest) request.get();
                Optional<User> user = this.userService.getUser(pendingReimbursementRequest.getEmployeeID());
                this.reimbursementRequestService.resolveReimbursementRequest(pendingReimbursementRequest, false, manager);
                this.mailService.sendEmail(
                    user.get().getEmail(), 
                    String.format("Reimbursement request %s has been processed", pendingReimbursementRequest.getType()), 
                    String.format("Your request for %f has been denied.", pendingReimbursementRequest.getAmount())
                );

                response = new SuccessfullyResolvedRequestResponse(resp);
            }
        } catch (NumberFormatException ex) {
            response = new IDTypeErrorResponse(resp);
        } catch (IllegalArgumentException | MessagingException | NoSuchElementException e) {
            logger.error("error servicing /request/deny/*", e);
            response = new ServerError(resp);
        } catch (RecordNotExistsException e) {
            response = new RequestNotExistsResponse(resp);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
