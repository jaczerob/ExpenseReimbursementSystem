package com.github.jaczerob.project1.web.responses.common;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempted to use an
 * endpoint that they did not have access to
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
public class NotAuthorizedResponse extends Response {
    /**
     * Constructs a new NotAuthorizedResponse
     * Sets the status code to 401 (Unauthorized)
     * @param resp The raw HTTP response
     */
    public NotAuthorizedResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        this.setMessage("You are not authorized to access this endpoint.");
    }
}
