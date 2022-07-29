package com.github.jaczerob.project1.web;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import com.github.jaczerob.project1.repositories.ReimbursementRequestRepository;
import com.github.jaczerob.project1.repositories.UserRepository;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.services.UserService;

/**
 * Represents the context listener for the servlets
 * @author Jacob
 * @since 0.2
 * @version 0.3
 */
public class ContextListener implements ServletContextListener {
    @Resource(name="jdbc/database")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        UserRepository userRepository = new UserRepository(dataSource);
        UserService userService = new UserService(userRepository);
        servletContextEvent.getServletContext().setAttribute("userService", userService);

        ReimbursementRequestRepository reimbursementRequestRepository = new ReimbursementRequestRepository(dataSource);
        ReimbursementRequestService reimbursementRequestService = new ReimbursementRequestService(reimbursementRequestRepository);
        servletContextEvent.getServletContext().setAttribute("reimbursementRequestService", reimbursementRequestService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) { }
}