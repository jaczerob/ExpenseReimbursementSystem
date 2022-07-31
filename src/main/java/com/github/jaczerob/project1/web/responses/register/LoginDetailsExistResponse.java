package com.github.jaczerob.project1.web.responses.register;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempted to register with existing
 * login details belonging to another user
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
public class LoginDetailsExistResponse extends Response {
    /**
     * Constructs a new LoginDetailsExistResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public LoginDetailsExistResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("Username or email already exist.");
    }
}
