package com.github.jaczerob.project1.web.responses.manager.request;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response representing the user input the wrong type for amount
 * @author Jacob
 * @since 0.13
 * @version 0.13
 */
public class IDTypeErrorResponse extends Response {
    /**
     * Constructs a new IDTypeErrorResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public IDTypeErrorResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("Error: ID must be a integer.");
    }
}
