package com.github.jaczerob.project1.web.responses.employee.request;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response representing a successful reimbursement request submission
 * @author Jacob
 * @since 0.13
 * @version 0.13
 */
public class SuccessfulSubmitResponse extends Response {
    /**
     * Constructs a new SuccessfulSubmitResponse
     * Sets the status code to 201 (Created)
     * @param resp The raw HTTP response
     */
    public SuccessfulSubmitResponse(HttpServletResponse resp) {
        resp.setStatus(HttpServletResponse.SC_CREATED);
        this.setMessage("Your reimbursement request has been successfully submitted!");
    }
}
