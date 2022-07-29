package com.github.jaczerob.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.requests.PendingReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ReimbursementRequest;
import com.github.jaczerob.project1.models.requests.ResolvedReimbursementRequest;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.repositories.ReimbursementRequestRepository;

public class ReimbursementRequestRepositoryTest {
    JdbcDataSource dataSource;
    ReimbursementRequestRepository reimbursementRequestRepository;

    @Before
    public void init() {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");

        reimbursementRequestRepository = new ReimbursementRequestRepository(dataSource);
    }

    @Before
    public void initDB() throws SQLException {
        Connection conn = this.dataSource.getConnection();

        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (user_id INTEGER GENERATED ALWAYS AS IDENTITY, user_email VARCHAR(50) UNIQUE NOT NULL, user_username VARCHAR(32) UNIQUE NOT NULL, user_password VARCHAR(255) NOT NULL, user_is_manager BOOLEAN NOT NULL, PRIMARY KEY (user_id));";
        Statement createUsersTableStmt = conn.createStatement();
        createUsersTableStmt.execute(createUsersTableSQL);

        String createReimbursementRequestsTableSQL = "CREATE TABLE IF NOT EXISTS reimbursement_requests (reimbursement_request_id INTEGER GENERATED ALWAYS AS IDENTITY, reimbursement_request_employee_id INTEGER NOT NULL, reimbursement_request_amount FLOAT NOT NULL CHECK (reimbursement_request_amount > 0.0), reimbursement_request_type VARCHAR(24) NOT NULL, reimbursement_request_pending BOOLEAN NOT NULL, reimbursement_request_approved BOOLEAN NOT NULL, reimbursement_request_manager_id INTEGER, PRIMARY KEY (reimbursement_request_id), FOREIGN KEY (reimbursement_request_employee_id) REFERENCES users (user_id), FOREIGN KEY (reimbursement_request_manager_id) REFERENCES users (user_id));";
        Statement createReimbursementRequestsTableStmt = conn.createStatement();
        createReimbursementRequestsTableStmt.execute(createReimbursementRequestsTableSQL);

        User user = new Employee(1, "", "", "");
        String insertUserSQL = "INSERT INTO users (user_email, user_username, user_password, user_is_manager) VALUES (?, ?, ?, ?);";
        PreparedStatement insertUserStmt = conn.prepareStatement(insertUserSQL);

        insertUserStmt.setString(1, user.getEmail());
        insertUserStmt.setString(2, user.getUsername());
        insertUserStmt.setString(3, user.getPassword());
        insertUserStmt.setBoolean(4, false);
        insertUserStmt.executeUpdate();
    }

    @After
    public void dropAllObjects() throws SQLException {
        Connection conn = this.dataSource.getConnection();

        String dropAllObjectsSQL = "DROP ALL OBJECTS;";
        Statement dropAllObjectsStatement = conn.createStatement();
        dropAllObjectsStatement.execute(dropAllObjectsSQL);
    }

    @Test
    public void shouldEstablishDBConnection() throws SQLException {
        dataSource.getConnection();
    }
    
    @Test
    public void testGetSuccess() throws SQLException {
        ReimbursementRequest request = new PendingReimbursementRequest(1, 1, 1f, "money");
        
        Connection conn = this.dataSource.getConnection();
        String sql = "INSERT INTO reimbursement_requests (reimbursement_request_employee_id, reimbursement_request_amount, reimbursement_request_type, reimbursement_request_pending, reimbursement_request_approved, reimbursement_request_manager_id) VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, request.getEmployeeID());
        ps.setFloat(2, request.getAmount());
        ps.setString(3, request.getType());
        ps.setBoolean(4, true);
        ps.setBoolean(5, false);
        ps.setNull(6, Types.INTEGER);
        ps.executeUpdate();

        Optional<ReimbursementRequest> gotRequest = this.reimbursementRequestRepository.get(request.getID());
        Assert.assertEquals(request, gotRequest.get());
    }

    @Test
    public void testInsertSuccess() throws SQLException, RecordAlreadyExistsException, RuntimeException {
        ReimbursementRequest request = new PendingReimbursementRequest(1, 1, 1f, "money");
        this.reimbursementRequestRepository.insert(request);

        Connection conn = this.dataSource.getConnection();

        String selectReimbursementRequestSQL = "SELECT * FROM reimbursement_requests WHERE reimbursement_request_id = 1;";
        Statement selectReimbursementRequestStmt = conn.createStatement();
        selectReimbursementRequestStmt.execute(selectReimbursementRequestSQL);

        ResultSet rs = selectReimbursementRequestStmt.getResultSet();
        Assert.assertTrue(rs.next());

        ReimbursementRequest gotRequest = new PendingReimbursementRequest(
            rs.getInt("reimbursement_request_id"),
            rs.getInt("reimbursement_request_employee_id"),
            rs.getInt("reimbursement_request_amount"),
            rs.getString("reimbursement_request_type")
        );

        Assert.assertEquals(request, gotRequest);
    }

    @Test(expected=RuntimeException.class)
    public void testInsertFail_whenTypeMismatch() throws SQLException, RuntimeException, RecordAlreadyExistsException {
        ReimbursementRequest request = new ResolvedReimbursementRequest(1, 1, 1f, "", false, 1);
        this.reimbursementRequestRepository.insert(request);
    }

    @Test
    public void testUpdateSuccess() throws SQLException, RecordNotExistsException, RuntimeException {
        PendingReimbursementRequest request = new PendingReimbursementRequest(1, 1, 1f, "money");
        Connection conn = this.dataSource.getConnection();

        String sql = "INSERT INTO reimbursement_requests (reimbursement_request_employee_id, reimbursement_request_amount, reimbursement_request_type, reimbursement_request_pending, reimbursement_request_approved, reimbursement_request_manager_id) VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, request.getEmployeeID());
        ps.setFloat(2, request.getAmount());
        ps.setString(3, request.getType());
        ps.setBoolean(4, true);
        ps.setBoolean(5, false);
        ps.setNull(6, Types.INTEGER);
        ps.executeUpdate();

        ReimbursementRequest approvedRequest = new ResolvedReimbursementRequest(request, true, 1);
        this.reimbursementRequestRepository.update(approvedRequest);

        String selectReimbursementRequestSQL = "SELECT * FROM reimbursement_requests WHERE reimbursement_request_id = 1;";
        Statement selectReimbursementRequestStmt = conn.createStatement();
        selectReimbursementRequestStmt.execute(selectReimbursementRequestSQL);

        ResultSet rs = selectReimbursementRequestStmt.getResultSet();
        Assert.assertTrue(rs.next());

        ReimbursementRequest gotRequest = new PendingReimbursementRequest(
            rs.getInt("reimbursement_request_id"),
            rs.getInt("reimbursement_request_employee_id"),
            rs.getInt("reimbursement_request_amount"),
            rs.getString("reimbursement_request_type")
        );

        Assert.assertEquals(approvedRequest, gotRequest);
    }

    @Test(expected=RuntimeException.class)
    public void testUpdateFail_whenTypeMismatch() throws SQLException, RecordNotExistsException, RuntimeException {
        ReimbursementRequest request = new PendingReimbursementRequest(1, 1, 1f, "money");
        this.reimbursementRequestRepository.update(request);
    }

    @Test
    public void testGetAllFromStatusSuccess() throws SQLException {
        List<ReimbursementRequest> requests = new ArrayList<>();
        Connection conn = this.dataSource.getConnection();
        String sql = "INSERT INTO reimbursement_requests (reimbursement_request_employee_id, reimbursement_request_amount, reimbursement_request_type, reimbursement_request_pending, reimbursement_request_approved, reimbursement_request_manager_id) VALUES (?, ?, ?, ?, ?, ?);";
        PendingReimbursementRequest request;

        for (int i = 1; i <= 5; i++) {
            request = new PendingReimbursementRequest(i, 1, 1f, "money");
            requests.add(request);
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, request.getEmployeeID());
            ps.setFloat(2, request.getAmount());
            ps.setString(3, request.getType());
            ps.setBoolean(4, true);
            ps.setBoolean(5, false);
            ps.setNull(6, Types.INTEGER);
            ps.executeUpdate();
        }

        List<ReimbursementRequest> gotRequests = this.reimbursementRequestRepository.getAllFromStatus(true);
        Assert.assertEquals(requests, gotRequests);
    }
}
