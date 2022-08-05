package com.github.jaczerob.project1.web.responses.manager.register;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user has successfully registered
 * @author Jacob
 * @since 0.9
 * @version 1.0
 */
public class SuccessfulRegistrationResponse extends Response {
    /**
     * Constructs a new SuccessfulRegistrationResponse
     * Sets the status code to 201 (Created)
     * @param resp The raw HTTP response
     */
    public SuccessfulRegistrationResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        this.setMessage("You have successfully registered!");
    }
}
