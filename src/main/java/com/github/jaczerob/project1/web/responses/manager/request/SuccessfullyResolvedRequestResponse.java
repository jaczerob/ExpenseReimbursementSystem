package com.github.jaczerob.project1.web.responses.manager.request;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response showing the user has successfully resolved a request
 * @author Jacob
 * @since 0.14
 * @version 0.14
 */
public class SuccessfullyResolvedRequestResponse extends Response {
    /**
     * Constructs a new SuccessfullyResolvedRequestResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public SuccessfullyResolvedRequestResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("This request was successfully resolved.");
    }
}
