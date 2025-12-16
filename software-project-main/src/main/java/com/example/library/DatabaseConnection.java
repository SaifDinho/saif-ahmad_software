package com.example.library;

import com.example.library.util.DatabaseConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseConnection {
    private static Connection connection = null;
    private static boolean schemaInitialized = false;
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DatabaseConfig.getDriver());
                connection = DriverManager.getConnection(
                    DatabaseConfig.getUrl(),
                    DatabaseConfig.getUsername(),
                    DatabaseConfig.getPassword()
                );
                initializeSchemaIfNeeded(connection);
                System.out.println("Connected to PostgreSQL-compatible database successfully!");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
        return connection;
    }

    private static synchronized void initializeSchemaIfNeeded(Connection conn) {
        if (schemaInitialized) {
            return;
        }
        try {
            // Check if the app_user table already exists
            boolean userTableExists;
            try (java.sql.ResultSet rs = conn.getMetaData().getTables(null, null, "APP_USER", null)) {
                userTableExists = rs.next();
            }
            if (userTableExists) {
                schemaInitialized = true;
                return;
            }
            // Table does not exist, run the full schema (including drops)
            try (InputStream in = DatabaseConnection.class.getClassLoader().getResourceAsStream("schema.sql")) {
                if (in == null) {
                    schemaInitialized = true;
                    return;
                }
                String sql = new BufferedReader(new InputStreamReader(in))
                        .lines()
                        .collect(Collectors.joining("\n"));
                // Split on semicolons to execute individual statements
                String[] statements = sql.split(";\\s*\n");
                try (Statement stmt = conn.createStatement()) {
                    for (String s : statements) {
                        String trimmed = s.trim();
                        if (!trimmed.isEmpty()) {
                            stmt.execute(trimmed);
                        }
                    }
                }
            }
            schemaInitialized = true;
        } catch (Exception e) {
            System.err.println("Failed to initialize schema from schema.sql");
            e.printStackTrace();
        }
    }
    
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
