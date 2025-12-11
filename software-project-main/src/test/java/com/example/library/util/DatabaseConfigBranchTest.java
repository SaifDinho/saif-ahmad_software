package com.example.library.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConfigBranchTest {

    private String oldUrl, oldUser, oldPass, oldDriver;

    @BeforeEach
    void setUp() {
        oldUrl = System.getProperty("db.url");
        oldUser = System.getProperty("db.username");
        oldPass = System.getProperty("db.password");
        oldDriver = System.getProperty("db.driver");
    }

    @AfterEach
    void tearDown() {
        restore("db.url", oldUrl);
        restore("db.username", oldUser);
        restore("db.password", oldPass);
        restore("db.driver", oldDriver);
    }

    private void restore(String key, String value) {
        if (value == null) {
            System.clearProperty(key);
        } else {
            System.setProperty(key, value);
        }
    }

    @Test
    void getUrl_returnsSystemProperty_WhenSet() {
        System.setProperty("db.url", "jdbc:h2:mem:testdb");
        assertEquals("jdbc:h2:mem:testdb", DatabaseConfig.getUrl());
    }

    @Test
    void getUrl_fallsBackToFile_WhenSystemPropertyEmpty() {
        System.setProperty("db.url", "");
        String url = DatabaseConfig.getUrl();
        assertNotNull(url);
        assertFalse(url.isEmpty());
        assertTrue(url.startsWith("jdbc:"));
    }

    @Test
    void getUsername_returnsSystemProperty_WhenSet() {
        System.setProperty("db.username", "customuser");
        assertEquals("customuser", DatabaseConfig.getUsername());
    }

    @Test
    void getUsername_fallsBackToFile_WhenSystemPropertyEmpty() {
        System.setProperty("db.username", "");
        String username = DatabaseConfig.getUsername();
        assertNotNull(username);
        assertFalse(username.isEmpty());
    }

    @Test
    void getPassword_returnsSystemProperty_WhenSet() {
        System.setProperty("db.password", "custompass");
        assertEquals("custompass", DatabaseConfig.getPassword());
    }

    @Test
    void getPassword_fallsBackToFile_WhenSystemPropertyEmpty() {
        System.setProperty("db.password", "");
        String password = DatabaseConfig.getPassword();
        assertNotNull(password);
        // Allow empty per config
        assertTrue(password.isEmpty());
    }

    @Test
    void useFileDb_returnsTrue_WhenSystemPropertyTrue() {
        System.setProperty("use.file.db", "true");
        assertTrue(DatabaseConfig.useFileDb());
    }

    @Test
    void useFileDb_returnsFalse_WhenSystemPropertyFalse() {
        System.setProperty("use.file.db", "false");
        assertFalse(DatabaseConfig.useFileDb());
    }

    @Test
    void useFileDb_fallsBackToFile_WhenSystemPropertyEmpty() {
        System.setProperty("use.file.db", "");
        // Depends on db.properties; just ensure it doesn't throw
        assertDoesNotThrow(DatabaseConfig::useFileDb);
    }
}
