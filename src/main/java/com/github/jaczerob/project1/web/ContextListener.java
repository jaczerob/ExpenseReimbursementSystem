package com.github.jaczerob.project1.web;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import com.github.jaczerob.project1.repositories.ReimbursementRequestRepository;
import com.github.jaczerob.project1.repositories.UserRepository;

public class ContextListener implements ServletContextListener {
    @Resource(name="jdbc/database")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        UserRepository userRepository = new UserRepository(dataSource);
        servletContextEvent.getServletContext().setAttribute("userRepository", userRepository);

        ReimbursementRequestRepository reimbursementRequestRepository = new ReimbursementRequestRepository(dataSource);
        servletContextEvent.getServletContext().setAttribute("reimbursementRequestRepository", reimbursementRequestRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context Destroyed");
    }
}