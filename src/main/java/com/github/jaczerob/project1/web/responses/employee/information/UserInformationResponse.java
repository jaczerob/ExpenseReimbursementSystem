package com.github.jaczerob.project1.web.responses.employee.information;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.web.responses.Response;

/**
 * Represents a HTTP response showing user information
 * @author Jacob
 * @since 0.10
 * @version 0.13
 */
public class UserInformationResponse extends Response {
    private User user;
    private List<ReimbursementRequest> pendingRequests;
    private List<ReimbursementRequest> resolvedRequests;

    /**
     * Constructs a new UserInformationResponse
     * Sets the status code to 200 (OK)
     * @param resp The raw HTTP response
     */
    public UserInformationResponse(HttpServletResponse resp, User user, List<ReimbursementRequest> pendingRequests, List<ReimbursementRequest> resolvedRequests) {
        resp.setStatus(HttpServletResponse.SC_OK);
        this.setMessage("Here is your user information!");
        this.user = user;
        this.pendingRequests = pendingRequests;
        this.resolvedRequests = resolvedRequests;
    }

    public String getUsername() {
        return this.user.getUsername();
    }

    public String getEmail() {
        return this.user.getEmail();
    }

    public int getID() {
        return this.user.getID();
    }

    public List<ReimbursementRequest> getPendingRequests() {
        return this.pendingRequests;
    }

    public List<ReimbursementRequest> getResolvedRequests() {
        return this.resolvedRequests;
    }
}
