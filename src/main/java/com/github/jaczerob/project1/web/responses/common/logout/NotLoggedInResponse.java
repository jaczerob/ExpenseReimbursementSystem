package com.github.jaczerob.project1.web.responses.common.logout;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempts to logout without being logged in
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
public class NotLoggedInResponse extends Response {
    /**
     * Constructs a new AlreadyLoggedInResponse
     * Sets the status code to 401 (Bad Request)
     * @param resp The raw HTTP response
     */
    public NotLoggedInResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        this.setMessage("You are not logged in.");
    }
}
