package com.example.library.repository;

import com.example.library.DatabaseConnection;
import com.example.library.domain.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcReservationRepositoryTest {

    private JdbcReservationRepository repo;
    private Integer testUserId;
    private Integer testItemId;

    @BeforeEach
    void setUp() throws SQLException {
        repo = new JdbcReservationRepository();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Clean reservations
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM reservation")) {
                pstmt.executeUpdate();
            }

            // Insert test user if not exists
            try (PreparedStatement checkUser = conn.prepareStatement("SELECT COUNT(*) FROM app_user WHERE username = 'testuser'")) {
                var rsUser = checkUser.executeQuery();
                if (rsUser.next() && rsUser.getInt(1) == 0) {
                    try (PreparedStatement insertUser = conn.prepareStatement(
                            "INSERT INTO app_user (username, password, email, role) VALUES (?, ?, ?, ?)",
                            java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        insertUser.setString(1, "testuser");
                        insertUser.setString(2, "p");
                        insertUser.setString(3, "test@example.com");
                        insertUser.setString(4, "USER");
                        insertUser.executeUpdate();
                        try (var keys = insertUser.getGeneratedKeys()) {
                            if (keys.next()) testUserId = keys.getInt(1);
                        }
                    }
                } else {
                    try (PreparedStatement selectUser = conn.prepareStatement("SELECT user_id FROM app_user WHERE username = 'testuser'")) {
                        var rsSelect = selectUser.executeQuery();
                        if (rsSelect.next()) testUserId = rsSelect.getInt("user_id");
                    }
                }
            }

            // Insert test media item if not exists
            try (PreparedStatement checkItem = conn.prepareStatement("SELECT COUNT(*) FROM media_item WHERE isbn = 'TESTISBN'")) {
                var rsItem = checkItem.executeQuery();
                if (rsItem.next() && rsItem.getInt(1) == 0) {
                    try (PreparedStatement insertItem = conn.prepareStatement(
                            "INSERT INTO media_item (title, author, type, isbn, total_copies, available_copies) VALUES (?, ?, ?, ?, ?, ?)",
                            java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        insertItem.setString(1, "Test Item");
                        insertItem.setString(2, "Test Author");
                        insertItem.setString(3, "BOOK");
                        insertItem.setString(4, "TESTISBN");
                        insertItem.setInt(5, 2);
                        insertItem.setInt(6, 2);
                        insertItem.executeUpdate();
                        try (var keys = insertItem.getGeneratedKeys()) {
                            if (keys.next()) testItemId = keys.getInt(1);
                        }
                    }
                } else {
                    try (PreparedStatement selectItem = conn.prepareStatement("SELECT item_id FROM media_item WHERE isbn = 'TESTISBN'")) {
                        var rsSelectItem = selectItem.executeQuery();
                        if (rsSelectItem.next()) testItemId = rsSelectItem.getInt("item_id");
                    }
                }
            }
        }
    }

    @Test
    void save_andFindById() {
        Reservation r = new Reservation();
        r.setUserId(testUserId);
        r.setItemId(testItemId);
        r.setReservationDate(LocalDateTime.now());
        r.setExpiryDate(LocalDateTime.now().plusDays(7));
        r.setStatus("ACTIVE");

        Reservation saved = repo.save(r);
        assertNotNull(saved.getReservationId());

        Optional<Reservation> found = repo.findById(saved.getReservationId());
        assertTrue(found.isPresent());
        assertEquals("ACTIVE", found.get().getStatus());
    }

    // Branch/edge coverage tests

    @Test
    void findById_returnsEmpty_whenNotFound() {
        Optional<Reservation> opt = repo.findById(99999);
        assertFalse(opt.isPresent());
    }

    @Test
    void findAll_returnsEmpty_whenNoReservations() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM reservation")) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            fail("Failed to clean reservations");
        }
        List<Reservation> list = repo.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    void findByUserId_returnsEmpty_whenNone() {
        List<Reservation> list = repo.findByUserId(99999);
        assertTrue(list.isEmpty());
    }

    @Test
    void findByItemId_returnsEmpty_whenNone() {
        List<Reservation> list = repo.findByItemId(99999);
        assertTrue(list.isEmpty());
    }

    @Test
    void update_throws_whenIdNotExists() {
        Reservation r = new Reservation();
        r.setReservationId(99999);
        r.setUserId(testUserId);
        r.setItemId(testItemId);
        r.setReservationDate(LocalDateTime.now());
        r.setExpiryDate(LocalDateTime.now().plusDays(7));
        r.setStatus("ACTIVE");
        assertThrows(RuntimeException.class, () -> repo.update(r));
    }

    @Test
    void deleteById_doesNothing_whenNotFound() {
        assertDoesNotThrow(() -> repo.deleteById(99999));
    }
}
