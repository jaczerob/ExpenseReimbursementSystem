package com.github.jaczerob.project1.web.responses.manager.request;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response representing an employee's pending requests
 * @author Jacob
 * @since 0.13
 * @version 1.0
 */
public class PendingRequestsResponse extends Response {
    private List<ReimbursementRequest> pendingRequests;

    /**
     * Constructs a new PendingRequestsResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     * @param employeeID The ID of the employee
     * @param requests The list of pending requests
     */
    public PendingRequestsResponse(HttpServletResponse resp, int employeeID, List<ReimbursementRequest> requests) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage(String.format("Here are %d's pending requests.", employeeID));
        this.pendingRequests = requests;
    }

    /**
     * Constructs a new PendingRequestsResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     * @param requests The list of pending requests
     */
    public PendingRequestsResponse(HttpServletResponse resp, List<ReimbursementRequest> requests) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("Here are all employee pending requests.");
        this.pendingRequests = requests;
    }

    public List<ReimbursementRequest> getRequests() {
        return this.pendingRequests;
    }
}
