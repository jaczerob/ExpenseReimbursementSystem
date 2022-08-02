package com.github.jaczerob.project1.web.responses.manager.request;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response representing an employee's resolved requests
 * @author Jacob
 * @since 0.13
 * @version 0.13
 */
public class ResolvedRequestsResponse extends Response {
    private List<ReimbursementRequest> resolvedRequests;

    /**
     * Constructs a new ResolvedRequestsResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     * @param employeeID The ID of the employee
     * @param requests The list of resolved requests
     */
    public ResolvedRequestsResponse(HttpServletResponse resp, int employeeID, List<ReimbursementRequest> requests) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        this.setMessage(String.format("Here are %d's resolved requests.", employeeID));
        this.resolvedRequests = requests;
    }

    /**
     * Constructs a new ResolvedRequestsResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     * @param requests The list of resolved requests
     */
    public ResolvedRequestsResponse(HttpServletResponse resp, List<ReimbursementRequest> requests) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        this.setMessage("Here are all employee resolved requests.");
        this.resolvedRequests = requests;
    }

    public List<ReimbursementRequest> getRequests() {
        return this.resolvedRequests;
    }
}
