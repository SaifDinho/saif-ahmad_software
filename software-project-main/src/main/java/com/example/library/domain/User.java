package com.example.library.domain;

import java.time.LocalDateTime;

/**
 * Represents a user in the Library Management System.
 * Contains user information including credentials, contact details, and role.
 * Users can be either regular users or administrators with different permissions.
 * 
 * @author Library System Team
 * @version 1.0
 */
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    
    /**
     * Default constructor for creating a new User instance.
     * Required for frameworks and ORM mapping.
     */
    public User() {
    }
    
    /**
     * Constructs a new User with all specified properties.
     * 
     * @param userId the unique identifier for the user
     * @param username the user's login name
     * @param password the user's encrypted password
     * @param email the user's email address
     * @param role the user's role (USER or ADMIN)
     * @param createdAt the timestamp when the user was created
     */
    public User(Integer userId, String username, String password, String email, String role, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the unique identifier for this user.
     * 
     * @return the user ID, or null if not yet saved to database
     */
    public Integer getUserId() {
        return userId;
    }
    
    /**
     * Sets the unique identifier for this user.
     * Typically used by repository when saving to database.
     * 
     * @param userId the user ID to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    /**
     * Gets the username used for login.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Sets the username used for login.
     * 
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Gets the user's password (should be encrypted).
     * 
     * @return the encrypted password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the user's password (should be encrypted before storing).
     * 
     * @param password the encrypted password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the user's email address.
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the user's email address.
     * 
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the user's role in the system.
     * 
     * @return the role (USER or ADMIN)
     */
    public String getRole() {
        return role;
    }
    
    /**
     * Sets the user's role in the system.
     * 
     * @param role the role to set (USER or ADMIN)
     */
    public void setRole(String role) {
        this.role = role;
    }
    
    /**
     * Gets the timestamp when this user was created.
     * 
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when this user was created.
     * 
     * @param createdAt the creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Returns a string representation of this user.
     * Excludes the password for security reasons.
     * 
     * @return string representation with user details
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
