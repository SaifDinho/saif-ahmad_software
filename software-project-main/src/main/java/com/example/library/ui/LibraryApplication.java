package com.example.library.ui;

import com.example.library.repository.*;
import com.example.library.service.*;
import com.example.library.service.fine.FineCalculator;

import javax.swing.*;

/**
 * Swing GUI application bootstrapper for the Library Management System.
 * Initializes repositories, services, and launches the login window.
 * Sets up dependency injection for all components and starts the user interface.
 * 
 * @author Library System Team
 * @version 1.0
 */
public class LibraryApplication {
    
    /**
     * Main entry point for the Library Management System GUI application.
     * Initializes all repositories and services with dependency injection,
     * then launches the login window to start the application.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize repositories
        UserRepository userRepository = new JdbcUserRepository();
        MediaItemRepository mediaItemRepository = new JdbcMediaItemRepository();
        LoanRepository loanRepository = new JdbcLoanRepository();
        FineRepository fineRepository = new JdbcFineRepository();
        
        // Initialize FineCalculator
        FineCalculator fineCalculator = new FineCalculator();
        
        // Initialize services
        AuthService authService = new AuthServiceImpl(userRepository);
        LibraryService libraryService = new LibraryServiceImpl(
                userRepository,
                mediaItemRepository,
                loanRepository,
                fineRepository,
                fineCalculator
        );
        PaymentService paymentService = new PaymentServiceImpl(fineRepository);
        
        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for native appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fall back to default look and feel
                System.err.println("Could not set system look and feel: " + e.getMessage());
            }
            
            // Create and show login frame
            LoginFrame loginFrame = new LoginFrame(authService, libraryService, paymentService, userRepository, mediaItemRepository, fineRepository, loanRepository);
            loginFrame.setVisible(true);
        });
    }
}
