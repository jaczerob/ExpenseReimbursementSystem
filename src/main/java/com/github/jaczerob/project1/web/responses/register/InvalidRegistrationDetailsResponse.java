package com.github.jaczerob.project1.web.responses.register;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempted to register with inappropriately
 * set information
 * @author Jacob
 * @since 0.9
 * @version 0.9
 */
public class InvalidRegistrationDetailsResponse extends Response {
    /**
     * Constructs a new InvalidRegistrationDetailsResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public InvalidRegistrationDetailsResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("Registration details are invalid. Make sure your username is between 1 and 32 characters and your e-mail is between 1 and 255 characters long.");
    }
}
