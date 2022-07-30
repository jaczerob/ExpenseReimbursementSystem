package com.github.jaczerob.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.server.SMTPServer;

import com.github.jaczerob.project1.services.MailService;

public class MailServiceTest {
    MailService mail;
    SMTPServer server;

    @Before
    public void init() throws IOException {
        try (InputStream input = Files.newInputStream(Paths.get("src/test/resources/mail.properties"))) {
            Properties props = new Properties();
            props.load(input);
            mail = new MailService(props);
        }
    }

    @Test
    public void testSendEmail() throws AddressException, MessagingException {
        MessageHandlerFactory fact = new MessageHandlerFactory() {
            @Override
            public MessageHandler create(MessageContext ctx) {
                return new Handler();
            }
            
            class Handler implements MessageHandler {
                public void from(String from) throws RejectException {
                    Assert.assertEquals("thewallacems@gmail.com", from);
                }
        
                public void recipient(String recipient) throws RejectException {
                    Assert.assertEquals("scopes.haricot_0n@icloud.com", recipient);
                }
        
                public void data(InputStream data) throws IOException {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(data));

                    String line = null;
                    try {
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("Subject"))
                                Assert.assertEquals("Test", line.substring(9));
                            else if (line.startsWith("Text"))
                                Assert.assertEquals("This is a test email.", line.substring(6));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        
                public void done() { }
            }
        };

        server = new SMTPServer(fact);
        server.setPort(25000);
        server.start();

        mail.sendEmail("scopes.haricot_0n@icloud.com", "Test", "Text: This is a test email.");
    }
}
