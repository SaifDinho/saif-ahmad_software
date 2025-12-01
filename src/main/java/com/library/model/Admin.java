package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    private int adminId;
    private String username;
    private String passwordHash;
    private LocalDateTime createdDate;

    public Admin() {
    }

    public Admin(int adminId, String username, String passwordHash, LocalDateTime createdDate) {
        this.adminId = adminId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdDate = createdDate;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", username='" + username + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
