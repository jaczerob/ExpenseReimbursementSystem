package com.github.jaczerob.project1.web.responses.login;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempted to log in again
 * without logging out
 * @author Jacob
 * @since 0.9
 * @since 0.9
 */
public class AlreadyLoggedInResponse extends Response {
    /**
     * Constructs a new AlreadyLoggedInResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public AlreadyLoggedInResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("You are already logged in!");
    }
}
