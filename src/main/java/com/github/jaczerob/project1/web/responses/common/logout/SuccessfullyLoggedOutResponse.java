package com.github.jaczerob.project1.web.responses.common.logout;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user successfully logs out
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
public class SuccessfullyLoggedOutResponse extends Response {
    /**
     * Constructs a new SuccessfullyLoggedOutResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public SuccessfullyLoggedOutResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("You have been logged out. Have a great day!");
    }
}
