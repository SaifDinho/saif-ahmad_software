package com.library.util;

public class Constants {
    // Borrowing periods (in days)
    public static final int BOOK_LOAN_PERIOD_DAYS = 28;
    public static final int CD_LOAN_PERIOD_DAYS = 7;
    
    // Fine rates (per day)
    public static final double BOOK_FINE_RATE = 0.50;
    public static final double CD_FINE_RATE = 1.00;
    
    // Borrowing limits
    public static final int MAX_ITEMS_PER_USER = 10;
    public static final double MAX_FINE_THRESHOLD = 50.0;
    public static final double MAX_FINE_AMOUNT = 100.0;
    
    // Database
    public static final String DATABASE_URL = "jdbc:sqlite:library_management.db";
    
    // Admin credentials
    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD_HASH = "admin123"; // In production, use proper hashing
}
