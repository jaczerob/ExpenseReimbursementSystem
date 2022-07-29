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
 * @version 0.3
 */
public class ReimbursementRequestRepository implements IRepository<ReimbursementRequest, Integer> {
    private static Logger logger = LogManager.getLogger(ReimbursementRequestRepository.class);
    
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
                int requestID = rs.getInt("reimbursement_request_id");
                int employeeID = rs.getInt("reimbursement_request_employee_id");
                float amount = rs.getInt("reimbursement_request_amount");
                String type = rs.getString("reimbursement_request_type");
                
                if (rs.getBoolean("reimbursement_request_pending")) {
                    reimbursementRequest = new ResolvedReimbursementRequest(requestID, employeeID, amount, type, rs.getBoolean("reimbursement_request_approved"), rs.getInt("reimbursement_request_manager_id"));
                } else {
                    reimbursementRequest = new PendingReimbursementRequest(requestID, employeeID, amount, type);
                }
            }
        } catch (SQLException exc) {
            logger.warn("error with reimbursement_requests get request", exc);
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
            conn.setAutoCommit(false);
            
            ps.setBoolean(1, true);
            ps.setBoolean(2, request.getApproved());
            ps.setInt(3, request.getApprovedBy());
            ps.setInt(4, request.getID());
            
            int rows = ps.executeUpdate();
            conn.commit();

            if (rows < 1) throw new RecordNotExistsException();
        } catch (SQLException exc) {
            logger.warn("error with reimbursement_requests get request", exc);
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
            logger.warn("error with reimbursement_requests insert request", exc);
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
            
            int requestID, employeeID;
            float amount;
            String type;
            
            while (rs.next()) {
                requestID = rs.getInt("reimbursement_request_id");
                employeeID = rs.getInt("reimbursement_request_employee_id");
                amount = rs.getInt("reimbursement_request_amount");
                type = rs.getString("reimbursement_request_type");

                reimbursementRequests.add(new PendingReimbursementRequest(requestID, employeeID, amount, type));
            }
        } catch (SQLException exc) {
            logger.warn("error with reimbursement_requests update request", exc);
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
            
            int requestID, gotEmployeeID;
            float amount;
            String type;
            
            while (rs.next()) {
                requestID = rs.getInt("reimbursement_request_id");
                gotEmployeeID = rs.getInt("reimbursement_request_employee_id");
                amount = rs.getInt("reimbursement_request_amount");
                type = rs.getString("reimbursement_request_type");

                if (rs.getBoolean("reimbursement_request_pending")) {
                    reimbursementRequests.add(new ResolvedReimbursementRequest(requestID, gotEmployeeID, amount, type, rs.getBoolean("reimbursement_request_approved"), rs.getInt("reimbursement_request_manager_id")));
                } else {
                    reimbursementRequests.add(new PendingReimbursementRequest(requestID, gotEmployeeID, amount, type));
                }
            }
        } catch (SQLException exc) {
            logger.warn("error with reimbursement_requests update request", exc);
        }

        return reimbursementRequests;
    }
}
