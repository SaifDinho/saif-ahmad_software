package com.library.notification;

import java.io.Serializable;

public class NotificationEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum EventType {
        ITEM_BORROWED,
        ITEM_RETURNED,
        ITEM_OVERDUE,
        FINE_PAYMENT_RECEIVED,
        FINE_GENERATED
    }

    private int userId;
    private EventType eventType;
    private String message;
    private String recipientEmail;
    private long timestamp;

    public NotificationEvent(int userId, EventType eventType, String message, String recipientEmail) {
        this.userId = userId;
        this.eventType = eventType;
        this.message = message;
        this.recipientEmail = recipientEmail;
        this.timestamp = System.currentTimeMillis();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
                "userId=" + userId +
                ", eventType=" + eventType +
                ", message='" + message + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
