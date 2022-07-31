package com.github.jaczerob.project1.web.responses.common;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempted did not set
 * the appropriate parameters in the form body
 * @author Jacob
 * @since 0.9
 * @since 0.9
 */
public class InvalidParametersResponse extends Response {
    private String[] parameters;

    public InvalidParametersResponse(HttpServletResponse resp, String... parameters) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        this.setMessage("Invalid parameters set.");
        this.parameters = parameters;
    }

    public String getParameters() {
        return String.format("The parameters for this endpoint are: %s", String.join(", ", this.parameters));
    }
}
