package com.github.jaczerob.project1.web.responses.manager.register;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user has successfully registered
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
public class SuccessfulRegistrationResponse extends Response {
    /**
     * Constructs a new SuccessfulRegistrationResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public SuccessfulRegistrationResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("You have successfully registered!");
    }
}
