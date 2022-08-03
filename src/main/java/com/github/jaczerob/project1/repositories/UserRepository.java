package com.github.jaczerob.project1.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.exceptions.RecordNotExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.Manager;
import com.github.jaczerob.project1.models.users.User;

/**
 * Represents a repository interface for accessing and managing users
 * @author Jacob
 * @since 0.1
 * @version 0.15
 */
public class UserRepository implements IRepository<User, String> {
    private static Logger logger = LogManager.getLogger(UserRepository.class);
    
    // column names
    private static final String USER_ID = "user_id";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_USERNAME = "user_username";
    private static final String USER_PASSWORD = "user_password";
    private static final String USER_IS_MANAGER = "user_is_manager";

    private DataSource dataSource;

    /**
     * Constructor for class {@link UserRepository}
     * @param dataSource The data source for the database
     */
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<User> get(String username) {
        User user = null;
        String sql = "SELECT * FROM users WHERE user_username = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt(USER_ID);
                String email = rs.getString(USER_EMAIL);
                String gotUsername = rs.getString(USER_USERNAME);
                String password = rs.getString(USER_PASSWORD);
                
                if (rs.getBoolean(USER_IS_MANAGER)) {
                    user = new Manager(userID, email, gotUsername, password);
                } else {
                    user = new Employee(userID, email, gotUsername, password);
                }
            }
        } catch (SQLException exc) {
            logger.error("error with users get request", exc);
        }

        return Optional.ofNullable(user);
    }

    /**
     * Returns a user based on their ID
     * @param id The ID of the user
     * @return The optional user object
     */
    public Optional<User> get(int id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE user_id = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userID = rs.getInt(USER_ID);
                String email = rs.getString(USER_EMAIL);
                String gotUsername = rs.getString(USER_USERNAME);
                String password = rs.getString(USER_PASSWORD);
                
                if (rs.getBoolean(USER_IS_MANAGER)) {
                    user = new Manager(userID, email, gotUsername, password);
                } else {
                    user = new Employee(userID, email, gotUsername, password);
                }
            }
        } catch (SQLException exc) {
            logger.error("error with users get request", exc);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void update(User user) throws RecordNotExistsException {
        String sql = "UPDATE USERS SET user_email = ?, user_password = ?, user_username = ? WHERE user_id = ?;";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getUsername());
            ps.setInt(4, user.getID());
            
            int rows = ps.executeUpdate();
            if (rows < 1) throw new RecordNotExistsException();
        } catch (SQLException exc) {
            logger.error("error with users update request", exc);
        }
    }

    @Override
    public void insert(User user) throws RecordAlreadyExistsException {
        String sql = "INSERT INTO users (user_email, user_username, user_password, user_is_manager) VALUES (?, ?, ?, ?);";

        try (
            Connection conn = this.dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setBoolean(4, user instanceof Manager);
            ps.executeUpdate();
        } catch (SQLException exc) {
            if (exc.getSQLState().startsWith("23")) throw new RecordAlreadyExistsException();
            logger.error("error with users insert request", exc);
        }
    }

    /**
     * Returns all employees in the database
     * @return A list of all employees
     * @throws SQLException If an error occurs in the SQL statements
     */
    public List<Employee> getAllEmployees() {
        List<Employee> users = new ArrayList<>();

        try (
            Connection conn = this.dataSource.getConnection();
            Statement statement = conn.createStatement()
        ) {
            statement.execute("SELECT * FROM users WHERE user_is_manager = FALSE;");

            ResultSet rs = statement.getResultSet();

            int userID;
            String email;
            String username;
            String password;
            
            while (rs.next()) {
                userID = rs.getInt(USER_ID);
                email = rs.getString(USER_EMAIL);
                username = rs.getString(USER_USERNAME);
                password = rs.getString(USER_PASSWORD);
                
                users.add(new Employee(userID, email, username, password));
            }
        } catch (SQLException exc) {
            logger.error("error with users get request", exc);
        }

        return users;
    }
}
