package com.github.jaczerob.project1.web;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import com.github.jaczerob.project1.repositories.ReimbursementRequestRepository;
import com.github.jaczerob.project1.repositories.UserRepository;
import com.github.jaczerob.project1.services.ReimbursementRequestService;

/**
 * Represents the context listener for the servlets
 * @author Jacob
 * @version 0.1
 * @since 0.2
 */
public class ContextListener implements ServletContextListener {
    @Resource(name="jdbc/database")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        UserRepository userRepository = new UserRepository(dataSource);
        servletContextEvent.getServletContext().setAttribute("userRepository", userRepository);

        ReimbursementRequestRepository reimbursementRequestRepository = new ReimbursementRequestRepository(dataSource);
        ReimbursementRequestService reimbursementRequestService = new ReimbursementRequestService(reimbursementRequestRepository);
        servletContextEvent.getServletContext().setAttribute("reimbursementRequestService", reimbursementRequestService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) { }
}