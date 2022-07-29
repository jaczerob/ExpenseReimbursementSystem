package com.github.jaczerob.project1.models.requests;

import com.github.jaczerob.project1.models.ERSObject;

/**
 * Represents a reimbursement request
 * @author Jacob
 * @version 0.1
 * @since 0.1
 */
public abstract class ReimbursementRequest extends ERSObject {
    private int employeeID;
    private float amount;
    private String type;

    /**
     * Create an instance of {@link ReimbursementRequest}
     * @param id The ID of the reimbursement request
     * @param employeeID The ID of the employee
     * @param amount The amount of the reimbursement
     * @param type The type of the reimbursement
     */
    public ReimbursementRequest(int id, int employeeID, float amount, String type) {
        super(id);
        this.employeeID = employeeID;
        this.amount = amount;
        this.type = type;
    }

    public float getAmount() {
        return this.amount;
    }

    public int getEmployeeID() {
        return this.employeeID;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ReimbursementRequest)) return false;

        ReimbursementRequest other = (ReimbursementRequest) obj;
        return this.getID() == other.getID();
    }
}
