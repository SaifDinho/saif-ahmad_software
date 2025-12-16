package com.example.library;

import com.example.library.DatabaseConnection;
import com.example.library.domain.User;
import com.example.library.repository.JdbcUserRepository;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting Library System...");

        try {
            // Quick DB connection check
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                System.out.println("Database connection test successful!");
            } else {
                System.out.println("Failed to connect to database.");
                return;
            }

            // Seed an admin user if none exists
            JdbcUserRepository userRepo = new JdbcUserRepository();
            System.out.println("use.file.db = " + com.example.library.util.DatabaseConfig.useFileDb());
            System.out.println("Users in DB after startup: " + userRepo.findAll().size());
            if (!userRepo.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setEmail("admin@example.com");
                admin.setRole("ADMIN");
                admin.setCreatedAt(java.time.LocalDateTime.now());
                userRepo.save(admin);
                System.out.println("Seeded admin user: username='admin', password='admin123'");
            }

            // Launch the GUI application
            com.example.library.ui.LibraryApplication.main(args);
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            // Exit gracefully without throwing
        }
    }
}
