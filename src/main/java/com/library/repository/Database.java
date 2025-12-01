package com.library.repository;

import java.sql.*;
import java.time.LocalDateTime;

public class Database {
    private static final String DATABASE_URL = "jdbc:sqlite:library_management.db";
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DATABASE_URL);
            initializeDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DATABASE_URL);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
        return connection;
    }

    private void initializeDatabase() {
        try {
            Statement statement = connection.createStatement();
            
            // Create tables
            statement.execute("CREATE TABLE IF NOT EXISTS books (" +
                    "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "author TEXT NOT NULL," +
                    "isbn TEXT UNIQUE," +
                    "quantity_total INTEGER NOT NULL," +
                    "quantity_available INTEGER NOT NULL," +
                    "daily_fine_rate REAL NOT NULL DEFAULT 0.50" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS cds (" +
                    "cd_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "artist TEXT NOT NULL," +
                    "catalog_number TEXT UNIQUE," +
                    "quantity_total INTEGER NOT NULL," +
                    "quantity_available INTEGER NOT NULL," +
                    "daily_fine_rate REAL NOT NULL DEFAULT 1.00" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT NOT NULL," +
                    "phone TEXT," +
                    "member_id TEXT UNIQUE NOT NULL," +
                    "registration_date TEXT NOT NULL," +
                    "is_active INTEGER DEFAULT 1" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS borrowing_records (" +
                    "record_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "item_id INTEGER NOT NULL," +
                    "item_type TEXT NOT NULL," +
                    "borrow_date TEXT NOT NULL," +
                    "due_date TEXT NOT NULL," +
                    "return_date TEXT," +
                    "is_returned INTEGER DEFAULT 0," +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id)" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS fines (" +
                    "fine_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "record_id INTEGER NOT NULL," +
                    "fine_amount REAL NOT NULL," +
                    "days_overdue INTEGER NOT NULL," +
                    "is_paid INTEGER DEFAULT 0," +
                    "calculation_date TEXT NOT NULL," +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id)," +
                    "FOREIGN KEY(record_id) REFERENCES borrowing_records(record_id)" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS payments (" +
                    "payment_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "fine_id INTEGER NOT NULL," +
                    "amount REAL NOT NULL," +
                    "payment_date TEXT NOT NULL," +
                    "payment_method TEXT," +
                    "FOREIGN KEY(fine_id) REFERENCES fines(fine_id)" +
                    ")");

            statement.execute("CREATE TABLE IF NOT EXISTS admins (" +
                    "admin_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password_hash TEXT NOT NULL," +
                    "created_date TEXT NOT NULL" +
                    ")");

            statement.close();
            
            // Initialize default admin
            initializeDefaultAdmin();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database tables", e);
        }
    }

    private void initializeDefaultAdmin() {
        try {
            String query = "SELECT COUNT(*) FROM admins WHERE username = 'admin'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            
            if (rs.next() && rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO admins (username, password_hash, created_date) VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(insertQuery);
                ps.setString(1, "admin");
                ps.setString(2, "admin123");
                ps.setString(3, LocalDateTime.now().toString());
                ps.executeUpdate();
                ps.close();
            }
            
            rs.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize default admin", e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    public void resetDatabase() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS payments");
            statement.execute("DROP TABLE IF EXISTS fines");
            statement.execute("DROP TABLE IF EXISTS borrowing_records");
            statement.execute("DROP TABLE IF EXISTS admins");
            statement.execute("DROP TABLE IF EXISTS users");
            statement.execute("DROP TABLE IF EXISTS cds");
            statement.execute("DROP TABLE IF EXISTS books");
            statement.close();
            
            initializeDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset database", e);
        }
    }
}
