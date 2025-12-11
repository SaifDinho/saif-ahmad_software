package com.example.library.domain;

import java.time.LocalDateTime;

/**
 * Represents a reservation for a media item in the Library Management System.
 * Users can reserve items that are currently unavailable and will be notified
 * when items become available. Reservations have expiration dates and status tracking.
 * 
 * @author Library System Team
 * @version 1.0
 */
public class Reservation {
    private Integer reservationId;
    private Integer userId;
    private Integer itemId;
    private LocalDateTime reservationDate;
    private LocalDateTime expiryDate;
    private String status; // ACTIVE, FULFILLED, EXPIRED, CANCELLED
    
    /**
     * Default constructor for creating a new Reservation instance.
     * Required for frameworks and ORM mapping.
     */
    public Reservation() {
    }
    
    /**
     * Constructs a new Reservation with all specified properties.
     * 
     * @param reservationId the unique identifier for the reservation
     * @param userId the ID of the user making the reservation
     * @param itemId the ID of the media item being reserved
     * @param reservationDate the date when the reservation was created
     * @param expiryDate the date when the reservation expires
     * @param status the current status of the reservation (ACTIVE, FULFILLED, EXPIRED, CANCELLED)
     */
    public Reservation(Integer reservationId, Integer userId, Integer itemId, 
                      LocalDateTime reservationDate, LocalDateTime expiryDate, String status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.itemId = itemId;
        this.reservationDate = reservationDate;
        this.expiryDate = expiryDate;
        this.status = status;
    }
    
    /**
     * Gets the unique identifier for this reservation.
     * 
     * @return the reservation ID, or null if not yet saved to database
     */
    public Integer getReservationId() {
        return reservationId;
    }
    
    /**
     * Sets the unique identifier for this reservation.
     * Typically used by repository when saving to database.
     * 
     * @param reservationId the reservation ID to set
     */
    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }
    
    /**
     * Gets the ID of the user who made this reservation.
     * 
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }
    
    /**
     * Sets the ID of the user who made this reservation.
     * 
     * @param userId the user ID to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    /**
     * Gets the ID of the media item being reserved.
     * 
     * @return the item ID
     */
    public Integer getItemId() {
        return itemId;
    }
    
    /**
     * Sets the ID of the media item being reserved.
     * 
     * @param itemId the item ID to set
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
    
    /**
     * Gets the date when this reservation was created.
     * 
     * @return the reservation date
     */
    public LocalDateTime getReservationDate() {
        return reservationDate;
    }
    
    /**
     * Sets the date when this reservation was created.
     * 
     * @param reservationDate the reservation date to set
     */
    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }
    
    /**
     * Gets the date when this reservation expires.
     * After this date, the reservation is no longer valid.
     * 
     * @return the expiry date
     */
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    
    /**
     * Sets the date when this reservation expires.
     * After this date, the reservation is no longer valid.
     * 
     * @param expiryDate the expiry date to set
     */
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    /**
     * Gets the current status of this reservation.
     * 
     * @return the status (ACTIVE, FULFILLED, EXPIRED, CANCELLED)
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Sets the current status of this reservation.
     * 
     * @param status the status to set (ACTIVE, FULFILLED, EXPIRED, CANCELLED)
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", reservationDate=" + reservationDate +
                ", expiryDate=" + expiryDate +
                ", status='" + status + '\'' +
                '}';
    }
}
