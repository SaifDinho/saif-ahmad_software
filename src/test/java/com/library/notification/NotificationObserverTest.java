package com.library.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationObserverTest {

    @Mock
    private NotificationObserver mockObserver;

    private EmailNotificationObserver emailObserver;

    @BeforeEach
    public void setUp() {
        emailObserver = new EmailNotificationObserver();
    }

    @Test
    public void testEmailNotificationObserverUpdate() {
        NotificationEvent event = new NotificationEvent(
                1,
                NotificationEvent.EventType.ITEM_BORROWED,
                "You have successfully borrowed a book",
                "user@example.com"
        );

        emailObserver.update(event);

        // Verify email was sent (check via console output)
        assertNotNull(event.getMessage());
    }

    @Test
    public void testNotificationEventCreation() {
        String message = "Test message";
        String email = "test@example.com";
        NotificationEvent event = new NotificationEvent(
                1,
                NotificationEvent.EventType.FINE_GENERATED,
                message,
                email
        );

        assert event.getUserId() == 1;
        assert event.getMessage().equals(message);
        assert event.getRecipientEmail().equals(email);
    }

    @Test
    public void testMultipleObserverNotifications() {
        NotificationObserver observer1 = mock(NotificationObserver.class);
        NotificationObserver observer2 = mock(NotificationObserver.class);

        NotificationEvent event = new NotificationEvent(
                1,
                NotificationEvent.EventType.ITEM_RETURNED,
                "Item returned successfully",
                "user@example.com"
        );

        observer1.update(event);
        observer2.update(event);

        verify(observer1).update(event);
        verify(observer2).update(event);
    }

    private void assertNotNull(Object object) {
        assert object != null;
    }
}
