package com.github.jaczerob.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.jaczerob.project1.exceptions.ConnectionException;
import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.repositories.UserRepository;

public class UserRepositoryTest {
    JdbcDataSource dataSource;
    UserRepository userRepository;

    @Before
    public void init() {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE");
        dataSource.setUser("sa");
        dataSource.setPassword("sa");

        userRepository = new UserRepository(dataSource);
    }

    @Before
    public void initDB() throws SQLException {
        Connection conn = this.dataSource.getConnection();

        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (user_id INTEGER GENERATED ALWAYS AS IDENTITY, user_email VARCHAR(50) UNIQUE NOT NULL, user_username VARCHAR(32) UNIQUE NOT NULL, user_password VARCHAR(255) NOT NULL, user_is_manager BOOLEAN NOT NULL, PRIMARY KEY (user_id));";
        Statement createUsersTableStmt = conn.createStatement();
        createUsersTableStmt.execute(createUsersTableSQL);
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
    public void testGetSuccess() throws SQLException, ConnectionException {
        User user = new Employee(1, "", "", "");

        Connection conn = this.dataSource.getConnection();
        String sql = "INSERT INTO users (user_email, user_username, user_password, user_is_manager) VALUES (?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setBoolean(4, false);
        ps.executeUpdate();

        Optional<User> gotUser = this.userRepository.get(user.getID());
        Assert.assertEquals(user, gotUser.get());
    }

    @Test
    public void testInsertSuccess() throws SQLException, ConnectionException, RecordAlreadyExistsException {
        User user = new Employee(1, "", "", "");
        this.userRepository.insert(user);

        Connection conn = this.dataSource.getConnection();

        String selectUserSQL = "SELECT * FROM users WHERE user_id = 1;";
        Statement selectUserStatement = conn.createStatement();
        selectUserStatement.execute(selectUserSQL);

        ResultSet rs = selectUserStatement.getResultSet();
        Assert.assertTrue(rs.next());

        User gotUser = new Employee(
            rs.getInt("user_id"), 
            rs.getString("user_email"),
            rs.getString("user_username"),
            rs.getString("user_password") 
        );

        Assert.assertEquals(user, gotUser);
    }

    @Test
    public void testUpdateSuccess() throws SQLException, ConnectionException, RecordNotExistsException {
        Employee user = new Employee(1, "", "", "");

        Connection conn = this.dataSource.getConnection();
        String sql = "INSERT INTO users (user_email, user_username, user_password, user_is_manager) VALUES (?, ?, ?, ?);";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setBoolean(4, false);
        ps.executeUpdate();

        user.setEmail("new@example.com");
        this.userRepository.update(user);

        String selectUserSQL = "SELECT * FROM users WHERE user_id = 1;";
        Statement selectUserStatement = conn.createStatement();
        selectUserStatement.execute(selectUserSQL);

        ResultSet rs = selectUserStatement.getResultSet();
        Assert.assertTrue(rs.next());

        User gotUser = new Employee(
            rs.getInt("user_id"), 
            rs.getString("user_email"),
            rs.getString("user_username"),
            rs.getString("user_password") 
        );

        Assert.assertEquals(user, gotUser);
    }

    @Test
    public void testGetAllEmployeesSuccess() throws SQLException, ConnectionException {
        Connection conn = this.dataSource.getConnection();
        String sql = "INSERT INTO users (user_email, user_username, user_password, user_is_manager) VALUES (?, ?, ?, ?);";
        
        List<Employee> employees = new ArrayList<Employee>();
        Employee employee;

        PreparedStatement ps;
        for (int i = 1; i <= 10; i++) {
            employee = new Employee(i, String.valueOf(i), String.valueOf(i), "");
            employees.add(employee);

            ps = conn.prepareStatement(sql);
            ps.setString(1, employee.getEmail());
            ps.setString(2, employee.getUsername());
            ps.setString(3, employee.getPassword());
            ps.setBoolean(4, false);
            ps.executeUpdate();
            ps.close();
        }

        List<Employee> gotEmployees = this.userRepository.getAllEmployees();
        Assert.assertEquals(employees, gotEmployees);
    }
}
