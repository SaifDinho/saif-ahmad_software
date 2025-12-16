package com.example.library.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersSetters() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("alice");
        user.setPassword("secret");
        user.setEmail("alice@example.com");
        user.setRole("STUDENT");
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        assertEquals(1, user.getUserId());
        assertEquals("alice", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("alice@example.com", user.getEmail());
        assertEquals("STUDENT", user.getRole());
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("alice");
        user.setEmail("alice@example.com");

        String s = user.toString();
        assertTrue(s.contains("alice"));
        assertTrue(s.contains("alice@example.com"));
    }
}
