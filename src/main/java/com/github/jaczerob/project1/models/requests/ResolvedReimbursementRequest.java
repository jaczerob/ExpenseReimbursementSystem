package com.github.jaczerob.project1.models.requests;

/**
 * Represents a resolved reimbursement request
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public class ResolvedReimbursementRequest extends ReimbursementRequest {
    private boolean approved;
    private int approvedBy;

    /**
     * Create an instance of {@link ResolvedReimbursementRequest}
     * @param id The ID of the reimbursement request
     * @param employeeID The ID of the employee
     * @param amount The amount of the reimbursement
     * @param type The type of the reimbursement
     * @param approved Whether the request was approved
     * @param approvedBy The ID of the manager that resolved the reimbursement request
     */
    public ResolvedReimbursementRequest(int id, int employeeID, float amount, String type, boolean approved, int approvedBy) {
        super(id, employeeID, amount, type);
        this.approved = approved;
        this.approvedBy = approvedBy;
    }

    /**
     * Create an instance of {@link ResolvedReimbursementRequest} using a {@link PendingReimbursementRequest}
     * @param id The ID of the reimbursement request
     * @param employeeID The ID of the employee
     * @param amount The amount of the reimbursement
     * @param type The type of the reimbursement
     * @param approved Whether the request was approved
     * @param approvedBy The ID of the manager that resolved the reimbursement request
     */
    public ResolvedReimbursementRequest(PendingReimbursementRequest request, boolean approved, int approvedBy) {
        super(request.getID(), request.getEmployeeID(), request.getAmount(), request.getType());
        this.approved = approved;
        this.approvedBy = approvedBy;
    }

    public boolean getApproved() {
        return this.approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public int getApprovedBy() {
        return this.approvedBy;
    }
}
