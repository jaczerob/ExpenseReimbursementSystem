package com.github.jaczerob.project1.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.requests.PendingReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ResolvedReimbursementRequest;

/**
 * Represents a repository interface for accessing and managing reimbursement requests
 * @author Jacob
 * @since 0.1
 * @version 0.13
 */
public class ReimbursementRequestRepository implements IRepository<ReimbursementRequest, Integer> {
    private static Logger logger = LogManager.getLogger(ReimbursementRequestRepository.class);

    // column names
    private static final String REIMBURSEMENT_REQUEST_ID = "reimbursement_request_id";
    private static final String REIMBURSEMENT_REQUEST_EMPLOYEE_ID = "reimbursement_request_employee_id";
    private static final String REIMBURSEMENT_REQUEST_AMOUNT = "reimbursement_request_amount";
    private static final String REIMBURSEMENT_REQUEST_TYPE = "reimbursement_request_type";
    private static final String REIMBURSEMENT_REQUEST_PENDING = "reimbursement_request_pending";
    private static final String REIMBURSEMENT_REQUEST_APPROVED = "reimbursement_request_approved";
    private static final String REIMBURSEMENT_REQUEST_MANAGER_ID = "reimbursement_request_manager_id";
    
    private DataSource dataSource;

    /**
     * Constructor for class {@link ReimbursementRequestRepository}
     * @param dataSource The data source for the database
     */
    public ReimbursementRequestRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<ReimbursementRequest> get(Integer id) {
        ReimbursementRequest reimbursementRequest = null;
        String sql = "SELECT * FROM reimbursement_requests WHERE reimbursement_request_id = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int requestID = rs.getInt(REIMBURSEMENT_REQUEST_ID);
                int employeeID = rs.getInt(REIMBURSEMENT_REQUEST_EMPLOYEE_ID);
                float amount = rs.getInt(REIMBURSEMENT_REQUEST_AMOUNT);
                String type = rs.getString(REIMBURSEMENT_REQUEST_TYPE);
                
                if (rs.getBoolean(REIMBURSEMENT_REQUEST_PENDING)) {
                    reimbursementRequest = new ResolvedReimbursementRequest(requestID, employeeID, amount, type, rs.getBoolean(REIMBURSEMENT_REQUEST_APPROVED), rs.getInt(REIMBURSEMENT_REQUEST_MANAGER_ID));
                } else {
                    reimbursementRequest = new PendingReimbursementRequest(requestID, employeeID, amount, type);
                }
            }
        } catch (SQLException exc) {
            logger.error("error with reimbursement_requests get request", exc);
        }

        return Optional.ofNullable(reimbursementRequest);
    }

    @Override
    public void update(ReimbursementRequest reimbursementRequest) throws RecordNotExistsException, IllegalArgumentException {
        if (!(reimbursementRequest instanceof ResolvedReimbursementRequest)) 
            throw new IllegalArgumentException("ReimbursementRequest must be ResolvedReimbursementRequest");

        ResolvedReimbursementRequest request = (ResolvedReimbursementRequest) reimbursementRequest;
        String sql = "UPDATE reimbursement_requests SET reimbursement_request_pending = ?, reimbursement_request_approved = ?, reimbursement_request_manager_id = ? WHERE reimbursement_request_id = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setBoolean(1, true);
            ps.setBoolean(2, request.getApproved());
            ps.setInt(3, request.getApprovedBy());
            ps.setInt(4, request.getID());
            
            int rows = ps.executeUpdate();
            if (rows < 1) throw new RecordNotExistsException();
        } catch (SQLException exc) {
            logger.error("error with reimbursement_requests update request", exc);
        }
    }

    @Override
    public void insert(ReimbursementRequest reimbursementRequest) throws IllegalArgumentException {
        if (!(reimbursementRequest instanceof PendingReimbursementRequest)) 
            throw new IllegalArgumentException("ReimbursementRequest must be PendingReimbursementRequest");

        PendingReimbursementRequest request = (PendingReimbursementRequest) reimbursementRequest;
        String sql = "INSERT INTO reimbursement_requests (reimbursement_request_employee_id, reimbursement_request_amount, reimbursement_request_type, reimbursement_request_pending, reimbursement_request_approved, reimbursement_request_manager_id) VALUES (?, ?, ?, ?, ?, ?);";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, request.getEmployeeID());
            ps.setFloat(2, request.getAmount());
            ps.setString(3, request.getType());
            ps.setBoolean(4, true);
            ps.setBoolean(5, false);
            ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
        } catch (SQLException exc) {
            logger.error("error with reimbursement_requests insert request", exc);
        }
    }

    /**
     * Returns all reimbursement requests based on whether they are pending or not
     * @param isPending Indicates whether the request is pending
     * @return A list of all pending reimbursement requests
     * @throws SQLException If an error occurs in the SQL statements
     */
    public List<ReimbursementRequest> getAllFromStatus(boolean isPending) {
        List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
        String sql = "SELECT * FROM reimbursement_requests WHERE reimbursement_request_pending = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setBoolean(1, isPending);
            ps.execute();
            
            ResultSet rs = ps.getResultSet();
            
            int requestID;
            int employeeID;
            float amount;
            String type;
            
            while (rs.next()) {
                requestID = rs.getInt(REIMBURSEMENT_REQUEST_ID);
                employeeID = rs.getInt(REIMBURSEMENT_REQUEST_EMPLOYEE_ID);
                amount = rs.getInt(REIMBURSEMENT_REQUEST_AMOUNT);
                type = rs.getString(REIMBURSEMENT_REQUEST_TYPE);

                reimbursementRequests.add(new PendingReimbursementRequest(requestID, employeeID, amount, type));
            }
        } catch (SQLException exc) {
            logger.error("error with reimbursement_requests get request", exc);
        }

        return reimbursementRequests;
    }

    /**
     * Gets all reimbursement requests from an employee
     * @param employeeID The employee's ID
     * @return A list of reimbursement requests from the employee
     */
    public List<ReimbursementRequest> getAllFromEmployee(int employeeID) {
        List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
        String sql = "SELECT * FROM reimbursement_requests WHERE reimbursement_request_employee_id = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, employeeID);
            ps.execute();
            
            ResultSet rs = ps.getResultSet();
            
            int requestID;
            int gotEmployeeID;
            float amount;
            String type;
            
            while (rs.next()) {
                requestID = rs.getInt(REIMBURSEMENT_REQUEST_ID);
                gotEmployeeID = rs.getInt(REIMBURSEMENT_REQUEST_EMPLOYEE_ID);
                amount = rs.getInt(REIMBURSEMENT_REQUEST_AMOUNT);
                type = rs.getString(REIMBURSEMENT_REQUEST_TYPE);
                
                if (rs.getBoolean(REIMBURSEMENT_REQUEST_PENDING)) {
                    reimbursementRequests.add(new ResolvedReimbursementRequest(requestID, gotEmployeeID, amount, type, rs.getBoolean(REIMBURSEMENT_REQUEST_APPROVED), rs.getInt(REIMBURSEMENT_REQUEST_MANAGER_ID)));
                } else {
                    reimbursementRequests.add(new PendingReimbursementRequest(requestID, gotEmployeeID, amount, type));
                }
            }
        } catch (SQLException exc) {
            logger.error("error with reimbursement_requests get request", exc);
        }

        return reimbursementRequests;
    }

    /**
     * Gets all reimbursement requests from an employee by pending status
     * @param employeeID The employee's ID
     * @param isPending Whether the request is pending
     * @return A list of reimbursement requests from the employee
     */
    public List<ReimbursementRequest> getAllFromEmployee(int employeeID, boolean isPending) {
        List<ReimbursementRequest> reimbursementRequests = new ArrayList<>();
        String sql = "SELECT * FROM reimbursement_requests WHERE reimbursement_request_employee_id = ? AND reimbursement_request_pending = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, employeeID);
            ps.setBoolean(2, isPending);
            ps.execute();
            
            ResultSet rs = ps.getResultSet();
            
            int requestID;
            int gotEmployeeID;
            float amount;
            String type;
            
            while (rs.next()) {
                requestID = rs.getInt(REIMBURSEMENT_REQUEST_ID);
                gotEmployeeID = rs.getInt(REIMBURSEMENT_REQUEST_EMPLOYEE_ID);
                amount = rs.getInt(REIMBURSEMENT_REQUEST_AMOUNT);
                type = rs.getString(REIMBURSEMENT_REQUEST_TYPE);
                
                if (rs.getBoolean(REIMBURSEMENT_REQUEST_PENDING)) {
                    reimbursementRequests.add(new ResolvedReimbursementRequest(requestID, gotEmployeeID, amount, type, rs.getBoolean(REIMBURSEMENT_REQUEST_APPROVED), rs.getInt(REIMBURSEMENT_REQUEST_MANAGER_ID)));
                } else {
                    reimbursementRequests.add(new PendingReimbursementRequest(requestID, gotEmployeeID, amount, type));
                }
            }
        } catch (SQLException exc) {
            logger.error("error with reimbursement_requests get request", exc);
        }

        return reimbursementRequests;
    }
}
