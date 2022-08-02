package com.github.jaczerob.project1.web.responses.employee.request;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response representing the user input the wrong type for amount
 * @author Jacob
 * @since 0.13
 * @version 0.13
 */
public class AmountTypeErrorResponse extends Response {
    /**
     * Constructs a new AmountTypeErrorResponse
     * Sets the status code to 400 (Bad Request)
     * @param resp The raw HTTP response
     */
    public AmountTypeErrorResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("Error: Amount must be a float.");
    }
}
