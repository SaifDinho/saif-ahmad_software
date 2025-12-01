package com.library.notification;

public class EmailNotificationObserver implements NotificationObserver {
    
    @Override
    public void update(NotificationEvent event) {
        sendEmail(event.getRecipientEmail(), event.getEventType().toString(), event.getMessage());
    }

    private void sendEmail(String recipientEmail, String subject, String message) {
        // Mock email implementation - in production, integrate with real email service
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + recipientEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("=========================");
    }

    public String getEmailContent(String subject, String message) {
        return "Subject: " + subject + "\nMessage: " + message;
    }
}
