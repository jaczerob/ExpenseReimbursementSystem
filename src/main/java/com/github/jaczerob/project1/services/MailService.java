package com.github.jaczerob.project1.services;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Represents a service that sends mail from a SMTP server
 * @author Jacob
 * @since 0.5
 * @since 0.5
 */
public class MailService {
    private final String username;
    private Session session;

    /**
     * Constructs a new MailService instance
     * @param mailProperties The SMTP properties for the mail session
     */
    public MailService(Properties mailProperties) {
        username = mailProperties.remove("mail.smtp.username").toString();
        String password = mailProperties.remove("mail.smtp.password").toString();
        this.session = Session.getInstance(mailProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Sends an email to the specified email
     * @param email The email recipient
     * @param subject The subject of the email
     * @param text The text of the email
     * @throws AddressException If the email is not valid
     * @throws MessagingException If there was an error sending the email internally
     */
    public void sendEmail(String email, String subject, String text) throws AddressException, MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }
}
