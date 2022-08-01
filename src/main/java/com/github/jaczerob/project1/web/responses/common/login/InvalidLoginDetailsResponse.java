package com.github.jaczerob.project1.web.responses.common.login;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempted to log in with inappropriately
 * set credentials.
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
public class InvalidLoginDetailsResponse extends Response {
    /**
     * Constructs a new InvalidLoginDetailsResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public InvalidLoginDetailsResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("Username or password is invalid. Make sure your username is between 1 and 32 characters and your password is between 1 and 255 characters long.");
    }
}
