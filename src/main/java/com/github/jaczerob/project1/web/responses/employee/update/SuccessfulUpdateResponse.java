package com.github.jaczerob.project1.web.responses.employee.update;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response if a user successfully updates their information.
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
public class SuccessfulUpdateResponse extends Response {
    /**
     * Constructs a new SuccessfulUpdateResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public SuccessfulUpdateResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("Your information has been updated. To view your information, invoke the /info endpoint.");
    }
}
