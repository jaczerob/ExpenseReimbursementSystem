package com.github.jaczerob.project1.services;

import java.util.List;
import java.util.Optional;

import com.github.jaczerob.project1.repositories.ReimbursementRequestRepository;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.requests.PendingReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ResolvedReimbursementRequest;
import com.github.jaczerob.project1.models.users.Manager;

/**
 * Represents the reimbursement request service bridge to the repository
 * @author Jacob
 * @since 0.2
 * @version 0.3
 */
public class ReimbursementRequestService {
    private ReimbursementRequestRepository reimbursementRequestRepository;

    /**
     * Constructor for class {@link ReimbursementRequestService}
     * @param reimbursementRequestRepository The repository to retrieve request data
     */
    public ReimbursementRequestService(ReimbursementRequestRepository reimbursementRequestRepository) {
        this.reimbursementRequestRepository = reimbursementRequestRepository;
    }

    /**
     * Retrieves reimbursement request by id
     * @param id The ID of the request
     * @return The optional request
     * @throws IllegalArgumentException If the ID is less than 0
     */
    public Optional<ReimbursementRequest> getReimbursementRequest(int id) throws IllegalArgumentException {
        if (id < 0) throw new IllegalArgumentException("Invalid id");
        return this.reimbursementRequestRepository.get(id);
    }

    /**
     * Resolved a pending reimbursement request
     * @param request The request to resolve
     * @param approved Whether the request is approved
     * @param approvedBy The manager to resolve the request
     * @throws RecordNotExistsException If the request is not found in the database
     * @throws IllegalArgumentException If the request was submitted as not resolved or the request properties were invalid
     */
    public void resolveReimbursementRequest(PendingReimbursementRequest request, boolean approved, Manager approvedBy) throws RecordNotExistsException, IllegalArgumentException {
        if (
            request.getID() < 0 ||
            request.getAmount() < 1f
        ) throw new IllegalArgumentException("Invalid request properties");
        ResolvedReimbursementRequest resolved = new ResolvedReimbursementRequest(request, approved, approvedBy.getID());
        this.reimbursementRequestRepository.update(resolved);
    }

    /**
     * Adds a request to the request repository
     * @param request The request to add
     * @throws IllegalArgumentException If the request was submitted as not pending or the request properties are invalid
     */
    public void addReimbursementRequest(PendingReimbursementRequest request) throws IllegalArgumentException {
        if (
            request.getID() < 0 ||
            request.getAmount() < 1f
        ) throw new IllegalArgumentException("Invalid request properties");
        this.reimbursementRequestRepository.insert(request);
    }

    /**
     * Gets all pending reimbursement requests
     * @return The list of pending reimbursement requests
     */
    public List<ReimbursementRequest> getAllPendingReimbursements() {
        return this.reimbursementRequestRepository.getAllFromStatus(true);
    }

    /**
     * Gets all resolved reimbursement requests
     * @return The list of resolved reimbursement requests 
     */
    public List<ReimbursementRequest> getAllResolvedReimbursements() {
        return this.reimbursementRequestRepository.getAllFromStatus(false);
    }

    /**
     * Gets all reimbursement requests from an employee
     * @return The list of resolved reimbursement requests 
     * @throws IllegalArgumentException If the ID is less than 0
     */
    public List<ReimbursementRequest> getAllEmployeeReimbursements(int employeeID) throws IllegalArgumentException {
        if (employeeID < 0) throw new IllegalArgumentException("Invalid id");
        return this.reimbursementRequestRepository.getAllFromEmployee(employeeID);
    }
}
