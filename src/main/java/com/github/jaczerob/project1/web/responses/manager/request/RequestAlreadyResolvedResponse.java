package com.github.jaczerob.project1.web.responses.manager.request;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response showing that the user is trying to resolve a 
 * request that is already resolved
 * @author Jacob
 * @since 0.14
 * @version 0.14
 */
public class RequestAlreadyResolvedResponse extends Response {
    /**
     * Constructs a new RequestAlreadyResolvedResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public RequestAlreadyResolvedResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("This request was already resolved.");
    }
}
