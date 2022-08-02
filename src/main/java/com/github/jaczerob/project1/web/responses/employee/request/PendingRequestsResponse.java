package com.github.jaczerob.project1.web.responses.employee.request;

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
public class PendingRequestsResponse extends Response {
    private List<ReimbursementRequest> resolvedRequests;

    /**
     * Constructs a new ResolvedRequestsResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public PendingRequestsResponse(HttpServletResponse resp, List<ReimbursementRequest> requests) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        this.setMessage("Here are your resolved requests.");
        this.resolvedRequests = requests;
    }

    public List<ReimbursementRequest> getRequests() {
        return this.resolvedRequests;
    }
}
