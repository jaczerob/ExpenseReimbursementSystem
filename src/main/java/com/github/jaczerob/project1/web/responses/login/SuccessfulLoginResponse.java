package com.github.jaczerob.project1.web.responses.login;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user successfully logged in
 * @author Jacob
 * @since 0.9
 * @since 0.9
 */
public class SuccessfulLoginResponse extends Response {
    /**
     * Constructs a new SuccessfulLoginResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public SuccessfulLoginResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("You are successfully logged in!");
    }
}
