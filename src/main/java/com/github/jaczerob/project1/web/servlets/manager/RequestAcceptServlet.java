package com.github.jaczerob.project1.web.servlets.manager;

import java.io.IOException;
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

@WebServlet(name="RequestAcceptServlet", urlPatterns={"/request/accept/*"})
public class RequestAcceptServlet extends ManagerServlet {
    private static Logger logger = LogManager.getLogger(RequestAcceptServlet.class);
    
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
            resp.getWriter().write(this.objectMapper.writeValueAsString(response));
            return;
        }

        HttpSession session = req.getSession(false);
        Manager manager = (Manager) session.getAttribute("user"); 

        try {
            int reimbursementRequestID = Integer.parseInt(req.getRequestURI().split("/")[3]);
            Optional<ReimbursementRequest> request = reimbursementRequestService.getReimbursementRequest(reimbursementRequestID);
            if (!request.isPresent()) {
                response = new RequestNotExistsResponse(resp);
            } else {
                ReimbursementRequest gotRequest = request.get();
                if (!(gotRequest instanceof PendingReimbursementRequest)) {
                    logger.warn("{} is a {}", gotRequest, gotRequest.getClass());
                    response = new RequestAlreadyResolvedResponse(resp);
                } else {
                    PendingReimbursementRequest pendingReimbursementRequest = (PendingReimbursementRequest) request.get();
                    this.reimbursementRequestService.resolveReimbursementRequest(pendingReimbursementRequest, true, manager);
                    
                    Optional<User> user = this.userService.getUser(pendingReimbursementRequest.getEmployeeID());
                    if (user.isPresent()) {
                        this.mailService.sendEmail(
                            user.get().getEmail(), 
                            String.format("Reimbursement request %s has been processed", pendingReimbursementRequest.getType()), 
                            String.format("Your request for $%.2f has been approved.", pendingReimbursementRequest.getAmount())
                        );

                        response = new SuccessfullyResolvedRequestResponse(resp);
                    } else {
                        logger.error("user not found for request {}", pendingReimbursementRequest);
                        response = new ServerError(resp);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            response = new IDTypeErrorResponse(resp);
        } catch (IllegalArgumentException | MessagingException e) {
            logger.error("error servicing /request/accept/*", e);
            response = new ServerError(resp);
        } catch (RecordNotExistsException e) {
            response = new RequestNotExistsResponse(resp);
        }

        resp.getWriter().write(objectMapper.writeValueAsString(response));
    }
}
