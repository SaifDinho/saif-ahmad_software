package com.example.library.notification;

import com.example.library.domain.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Email notification implementation of the Notifier interface.
 * Currently stores messages in memory for testing purposes.
 * In production, this would integrate with an actual email service.
 */
public class EmailNotifier implements Notifier {
    
    private final List<String> sentMessages;
    private final Properties mailProperties;
    
    /**
     * Constructs a new email notifier.
     */
    public EmailNotifier() {
        this.sentMessages = new ArrayList<>();
        this.mailProperties = loadMailProperties();
    }
    
    /**
     * Sends an email notification to a user.
     * Currently simulates email sending by storing the message in memory.
     * 
     * @param user the user to notify
     * @param message the notification message
     */
    @Override
    public void notify(User user, String message) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        
        String emailMessage = user.getEmail() + ": " + message;
        sentMessages.add(emailMessage);
        
        // Attempt to send a real email if mail configuration is available.
        // Any failure here should NOT break application flow; it will only be logged.
        if (!mailProperties.isEmpty()) {
            try {
                sendRealEmail(user, message);
            } catch (Exception e) {
                // Log to stderr to avoid affecting normal program behavior or tests
                System.err.println("Failed to send real email: " + e.getMessage());
            }
        }
    }
    
    /**
     * Retrieves all sent messages.
     * Returns an unmodifiable list for testing purposes.
     * 
     * @return list of all sent email messages
     */
    public List<String> getSentMessages() {
        return Collections.unmodifiableList(sentMessages);
    }
    
    /**
     * Clears all sent messages.
     * Useful for resetting state between tests.
     */
    public void clearMessages() {
        sentMessages.clear();
    }

    private Properties loadMailProperties() {
        Properties props = new Properties();
        try (InputStream in = EmailNotifier.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (in == null) {
                return props;
            }
            props.load(in);
        } catch (IOException e) {
            System.err.println("Failed to load email.properties: " + e.getMessage());
        }
        return props;
    }

    private void sendRealEmail(User user, String messageBody) throws MessagingException {
        String host = mailProperties.getProperty("mail.smtp.host");
        String port = mailProperties.getProperty("mail.smtp.port", "587");
        String username = mailProperties.getProperty("mail.smtp.username");
        String password = mailProperties.getProperty("mail.smtp.password");
        String fromAddress = mailProperties.getProperty("mail.from.address", username);
        String fromName = mailProperties.getProperty("mail.from.name", "Library System");

        if (host == null || username == null || password == null) {
            // Missing essential configuration - skip real sending
            return;
        }

        Properties sessionProps = new Properties();
        sessionProps.put("mail.smtp.host", host);
        sessionProps.put("mail.smtp.port", port);
        sessionProps.put("mail.smtp.auth", mailProperties.getProperty("mail.smtp.auth", "true"));
        sessionProps.put("mail.smtp.starttls.enable", mailProperties.getProperty("mail.smtp.starttls.enable", "true"));

        Session session = Session.getInstance(sessionProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(fromAddress, fromName));
        } catch (java.io.UnsupportedEncodingException e) {
            mimeMessage.setFrom(new InternetAddress(fromAddress));
        }
        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        mimeMessage.setSubject("Library Notification");
        mimeMessage.setText(messageBody);

        Transport.send(mimeMessage);
    }
}
