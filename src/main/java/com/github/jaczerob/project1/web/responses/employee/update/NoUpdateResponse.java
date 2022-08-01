package com.github.jaczerob.project1.web.responses.employee.update;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user attempts to update their information without
 * providing any information to update.
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
public class NoUpdateResponse extends Response {
    /**
     * Constructs a new NoUpdateResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public NoUpdateResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("You did not specify any username, email, or password to update, so nothing was updated.");
    }
}
