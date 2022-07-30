package com.github.jaczerob.project1.web;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.jaczerob.project1.repositories.ReimbursementRequestRepository;
import com.github.jaczerob.project1.repositories.UserRepository;
import com.github.jaczerob.project1.services.MailService;
import com.github.jaczerob.project1.services.ReimbursementRequestService;
import com.github.jaczerob.project1.services.UserService;

/**
 * Represents the context listener for the servlets
 * @author Jacob
 * @since 0.2
 * @version 0.7
 */
public class ContextListener implements ServletContextListener {
    private static Logger logger = LogManager.getLogger(ContextListener.class);
    
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

        try (InputStream input = Files.newInputStream(Paths.get(servletContextEvent.getServletContext().getRealPath("/WEB-INF/classes/mail.properties")))) {
            Properties props = new Properties();
            props.load(input);
            logger.info("loaded mail properties successfully");

            MailService mailService = new MailService(props);
            servletContextEvent.getServletContext().setAttribute("mailService", mailService);
        } catch (IOException exc) {
            logger.error("error loading properties, mail service is not available", exc);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) { }
}