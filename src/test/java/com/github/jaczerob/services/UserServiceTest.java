package com.github.jaczerob.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.jaczerob.project1.exceptions.RecordAlreadyExistsException;
import com.github.jaczerob.project1.models.users.Employee;
import com.github.jaczerob.project1.models.users.User;
import com.github.jaczerob.project1.repositories.UserRepository;
import com.github.jaczerob.project1.services.UserService;

public class UserServiceTest {
    UserService users;
    UserRepository userRepository;

    @Before
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        users = new UserService(userRepository);
    }

    @Test
    public void testGetUserSuccess() throws IllegalArgumentException {
        User user = new Employee(1, "email", "username", "password");
        Mockito.when(userRepository.get("username")).thenReturn(Optional.of(user));
        Assert.assertEquals(user, this.users.getUser("username").get());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetUserFail_whenUsernameEmpty() throws IllegalArgumentException {
        this.users.getUser("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetUserFail_whenUsernameTooLong() throws IllegalArgumentException {
        this.users.getUser("meowmeowmeowmeowmeowmeowmeowmeowmeow");
    }

    @Test
    public void testRegisterUserSuccess() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "email", "username", "password");
        this.users.registerUser(user);
    }

    @Test(expected=RecordAlreadyExistsException.class)
    public void testRegisterUserFail_whenDuplicateEntries() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "email", "username", "password");
        this.users.registerUser(user);

        Mockito.doThrow(RecordAlreadyExistsException.class).when(this.userRepository).insert(user);
        this.users.registerUser(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRegisterUserFail_whenIDLessThanZero() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(-1, "email", "username", "password");
        this.users.registerUser(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRegisterUserFail_whenUsernameEmpty() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "email", "", "password");
        this.users.registerUser(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRegisterUserFail_whenUsernameTooLong() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "email", "meowmeowmeowmeowmeowmeowmeowmeowmeow", "password");
        this.users.registerUser(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRegisterUserFail_whenPasswordEmpty() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "email", "username", "");
        this.users.registerUser(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRegisterUserFail_whenPasswordTooLong() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "email", "username", "meowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeow");
        this.users.registerUser(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRegisterUserFail_whenEmailEmpty() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "", "username", "password");
        this.users.registerUser(user);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRegisterUserFail_whenEmailTooLong() throws IllegalArgumentException, RecordAlreadyExistsException {
        User user = new Employee(1, "meowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeowmeow", "username", "password");
        this.users.registerUser(user);
    }

    @Test
    public void testGetAllEmployeesSuccess() {
        List<Employee> employees = new ArrayList<Employee>() {
            {
                add(Mockito.mock(Employee.class));
                add(Mockito.mock(Employee.class));
                add(Mockito.mock(Employee.class));
                add(Mockito.mock(Employee.class));
                add(Mockito.mock(Employee.class));
            }
        };

        Mockito.when(this.userRepository.getAllEmployees()).thenReturn(employees);
        Assert.assertEquals(employees, this.users.getAllEmployees());
    }
}
