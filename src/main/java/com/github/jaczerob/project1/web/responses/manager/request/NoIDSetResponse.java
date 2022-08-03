package com.github.jaczerob.project1.web.responses.manager.request;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response showing that the user did not set a reimbursement
 * request ID to be resolved
 * @author Jacob
 * @since 0.14
 * @version 0.14
 */
public class NoIDSetResponse extends Response {
    /**
     * Constructs a new NoIDSetResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public NoIDSetResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("Please set an ID of a pending reimbursement request. These can be retrieved from /request/view/pending. To use, do /request/accept/{id} or /request/deny/{id}.");
    }
}
