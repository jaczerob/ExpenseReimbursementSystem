package com.github.jaczerob.project1.web.responses.manager.request;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response showing that the user is trying to resolve a 
 * request that does not exist
 * @author Jacob
 * @since 0.14
 * @version 0.14
 */
public class RequestNotExistsResponse extends Response {
    /**
     * Constructs a new RequestNotExistsResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public RequestNotExistsResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("This request does not exist.");
    }
}
