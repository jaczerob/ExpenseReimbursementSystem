package com.github.jaczerob.project1.models.requests;

/**
 * Represents a pending reimbursement request
 * @author Jacob
 * @since 0.1
 * @version 0.3
 */
public class PendingReimbursementRequest extends ReimbursementRequest {
    /**
     * Create an instance of {@link ReimbursementRequest}
     * @param id The ID of the reimbursement request
     * @param employeeID The ID of the employee
     * @param amount The amount of the reimbursement
     * @param type The type of the reimbursement
     */
    public PendingReimbursementRequest(int id, int employeeID, float amount, String type) {
        super(id, employeeID, amount, type);
    }
}
