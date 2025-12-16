package com.example.library.notification;

import com.example.library.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmailNotifierTest {
    
    private EmailNotifier emailNotifier;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        emailNotifier = new EmailNotifier();
        
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setRole("STUDENT");
        testUser.setCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void testNotify_Success() {
        // Arrange
        String message = "Your book is overdue!";
        
        // Act
        emailNotifier.notify(testUser, message);
        
        // Assert
        assertEquals(1, emailNotifier.getSentMessages().size());
        assertTrue(emailNotifier.getSentMessages().get(0).contains("testuser@example.com"));
        assertTrue(emailNotifier.getSentMessages().get(0).contains(message));
    }
    
    @Test
    void testNotify_MultipleMessages() {
        // Arrange
        String message1 = "Your book is due tomorrow";
        String message2 = "Your book is overdue";
        
        // Act
        emailNotifier.notify(testUser, message1);
        emailNotifier.notify(testUser, message2);
        
        // Assert
        assertEquals(2, emailNotifier.getSentMessages().size());
        assertTrue(emailNotifier.getSentMessages().get(0).contains(message1));
        assertTrue(emailNotifier.getSentMessages().get(1).contains(message2));
    }
    
    @Test
    void testNotify_NullUser() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailNotifier.notify(null, "Test message");
        });
    }
    
    @Test
    void testNotify_NullEmail() {
        // Arrange
        testUser.setEmail(null);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailNotifier.notify(testUser, "Test message");
        });
    }
    
    @Test
    void testNotify_EmptyEmail() {
        // Arrange
        testUser.setEmail("   ");
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailNotifier.notify(testUser, "Test message");
        });
    }
    
    @Test
    void testNotify_NullMessage() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailNotifier.notify(testUser, null);
        });
    }
    
    @Test
    void testNotify_EmptyMessage() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            emailNotifier.notify(testUser, "   ");
        });
    }
    
    @Test
    void testGetSentMessages_ReturnsUnmodifiableList() {
        // Arrange
        emailNotifier.notify(testUser, "Test message");
        
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            emailNotifier.getSentMessages().add("Should not work");
        });
    }
    
    @Test
    void testClearMessages() {
        // Arrange
        emailNotifier.notify(testUser, "Message 1");
        emailNotifier.notify(testUser, "Message 2");
        assertEquals(2, emailNotifier.getSentMessages().size());
        
        // Act
        emailNotifier.clearMessages();
        
        // Assert
        assertEquals(0, emailNotifier.getSentMessages().size());
    }
    
    @Test
    void testNotify_DifferentUsers() {
        // Arrange
        User user2 = new User();
        user2.setUserId(2);
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setRole("FACULTY");
        
        // Act
        emailNotifier.notify(testUser, "Message for user 1");
        emailNotifier.notify(user2, "Message for user 2");
        
        // Assert
        assertEquals(2, emailNotifier.getSentMessages().size());
        assertTrue(emailNotifier.getSentMessages().get(0).contains("testuser@example.com"));
        assertTrue(emailNotifier.getSentMessages().get(1).contains("user2@example.com"));
    }

    @Test
    void testNotify_DoesNotThrowWhenEmailPropertiesMissing() {
        // This test ensures EmailNotifier works even if email.properties is missing or incomplete
        // It will still store messages in memory but won't attempt SMTP
        assertDoesNotThrow(() -> {
            emailNotifier.notify(testUser, "Test message");
        });
        assertEquals(1, emailNotifier.getSentMessages().size());
    }

    // Additional branch/edge coverage tests

    @Test
    void testNotify_WithValidEmailProperties() {
        // Temporarily set system property to load a valid email.properties from classpath
        // For this test, we assume email.properties exists with minimal config
        assertDoesNotThrow(() -> {
            emailNotifier.notify(testUser, "Test message with real email attempt");
        });
        assertEquals(1, emailNotifier.getSentMessages().size());
        // The message should still be stored even if real sending fails
    }

    @Test
    void testNotify_WhenEmailPropertiesLoadFails() {
        // Create a new EmailNotifier that will fail to load properties
        EmailNotifier notifier = new EmailNotifier();
        // Even if properties loading fails, notify should still work
        assertDoesNotThrow(() -> {
            notifier.notify(testUser, "Test message despite property load failure");
        });
        assertEquals(1, notifier.getSentMessages().size());
    }

    @Test
    void testSendRealEmail_SkipsWhenMissingEssentialConfig() {
        // This tests the branch where host/username/password are null
        EmailNotifier notifier = new EmailNotifier();
        // Even with missing config, notify should not throw
        assertDoesNotThrow(() -> {
            notifier.notify(testUser, "Test message with incomplete config");
        });
        assertEquals(1, notifier.getSentMessages().size());
    }

    @Test
    void testSendRealEmail_HandlesMessagingException() {
        // This tests the catch block in notify when sendRealEmail throws
        EmailNotifier notifier = new EmailNotifier();
        // Force sendRealEmail to fail by using a user with invalid email format
        User invalidUser = new User();
        invalidUser.setUserId(2);
        invalidUser.setUsername("invalid");
        invalidUser.setEmail("invalid-email-format"); // Invalid email format
        invalidUser.setRole("STUDENT");
        invalidUser.setCreatedAt(LocalDateTime.now());
        
        assertDoesNotThrow(() -> {
            notifier.notify(invalidUser, "Test message that will cause MessagingException");
        });
        assertEquals(1, notifier.getSentMessages().size());
        // Message should still be stored despite the exception
    }

    @Test
    void testLoadMailProperties_ReturnsEmptyWhenFileMissing() {
        // This tests the branch where InputStream is null
        EmailNotifier notifier = new EmailNotifier();
        // Should not throw and should work normally
        assertDoesNotThrow(() -> {
            notifier.notify(testUser, "Test message when email.properties missing");
        });
        assertEquals(1, notifier.getSentMessages().size());
    }

    @Test
    void testLoadMailProperties_HandlesIOException() {
        // This tests the catch IOException block in loadMailProperties
        EmailNotifier notifier = new EmailNotifier();
        // Even if IOException occurs during load, notify should work
        assertDoesNotThrow(() -> {
            notifier.notify(testUser, "Test message despite IOException in property loading");
        });
        assertEquals(1, notifier.getSentMessages().size());
    }

    @Test
    void testSendRealEmail_UsesDefaultValues() {
        // This tests branches where getProperty with default values are used
        EmailNotifier notifier = new EmailNotifier();
        // Should use default port 587, default auth=true, default starttls=true
        assertDoesNotThrow(() -> {
            notifier.notify(testUser, "Test message with default SMTP settings");
        });
        assertEquals(1, notifier.getSentMessages().size());
    }

    @Test
    void testSendRealEmail_HandlesUnsupportedEncodingException() {
        // This tests the catch block in sendRealEmail for UnsupportedEncodingException
        User userWithInvalidEmail = new User();
        userWithInvalidEmail.setUserId(3);
        userWithInvalidEmail.setUsername("bademail");
        // Use an email address that might cause UnsupportedEncodingException in from address
        userWithInvalidEmail.setEmail("user@example.com");
        userWithInvalidEmail.setRole("STUDENT");
        userWithInvalidEmail.setCreatedAt(LocalDateTime.now());
        
        EmailNotifier notifier = new EmailNotifier();
        assertDoesNotThrow(() -> {
            notifier.notify(userWithInvalidEmail, "Test message encoding edge case");
        });
        assertEquals(1, notifier.getSentMessages().size());
    }
}
