package com.example.library.repository;

import com.example.library.domain.User;
import com.example.library.util.XmlDataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileUserRepositoryTest {

    @TempDir
    Path tempDir;

    private FileUserRepository repo;

    @BeforeEach
    void setUp() {
        System.setProperty("xml.data.dir", tempDir.toString() + java.io.File.separator);
        // Ensure fresh file
        java.io.File f = new java.io.File(XmlDataManager.getDataFilePath("users.xml"));
        if (f.exists()) {
            f.delete();
        }
        repo = new FileUserRepository();
    }

    @Test
    void saveAndFindByUsername() {
        User user = new User();
        user.setUsername("jdoe");
        user.setPassword("secret");
        user.setEmail("jdoe@example.com");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        User saved = repo.save(user);
        assertNotNull(saved.getUserId());

        Optional<User> found = repo.findByUsername("jdoe");
        assertTrue(found.isPresent());
        assertEquals("jdoe@example.com", found.get().getEmail());
        assertTrue(repo.existsByUsername("jdoe"));
    }

    @Test
    void updateAndDelete() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("p");
        user.setEmail("alice@example.com");
        user.setRole("ADMIN");

        User saved = repo.save(user);
        saved.setEmail("alice2@example.com");
        repo.update(saved);

        Optional<User> loaded = repo.findById(saved.getUserId());
        assertTrue(loaded.isPresent());
        assertEquals("alice2@example.com", loaded.get().getEmail());

        repo.deleteById(saved.getUserId());
        assertFalse(repo.findById(saved.getUserId()).isPresent());
    }

    // Branch/edge coverage tests

    @Test
    void save_generatesId_whenNull() {
        User user = new User();
        user.setUsername("new");
        user.setPassword("p");
        user.setEmail("new@example.com");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        User saved = repo.save(user);
        assertNotNull(saved.getUserId());
    }

    @Test
    void save_keepsId_whenNotNull() {
        User user = new User();
        user.setUserId(99);
        user.setUsername("keep");
        user.setPassword("p");
        user.setEmail("keep@example.com");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        User saved = repo.save(user);
        assertEquals(99, saved.getUserId());
    }

    @Test
    void update_savesNew_whenIdNull() {
        User user = new User();
        user.setUsername("new");
        user.setPassword("p");
        user.setEmail("new@example.com");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        User updated = repo.update(user);
        assertNotNull(updated.getUserId());
    }

    @Test
    void update_savesNew_whenIdNotExists() {
        User user = new User();
        user.setUserId(999);
        user.setUsername("new");
        user.setPassword("p");
        user.setEmail("new@example.com");
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        User updated = repo.update(user);
        assertEquals(999, updated.getUserId());
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        Optional<User> opt = repo.findById(999);
        assertFalse(opt.isPresent());
    }

    @Test
    void findByUsername_returnsEmpty_whenNotFound() {
        Optional<User> opt = repo.findByUsername("notfound");
        assertFalse(opt.isPresent());
    }

    @Test
    void findByEmail_returnsEmpty_whenNotFound() {
        Optional<User> opt = repo.findByEmail("notfound@example.com");
        assertFalse(opt.isPresent());
    }

    @Test
    void findAll_returnsEmpty_whenNoUsers() {
        List<User> users = repo.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void findAll_returnsUsers_whenUsersExist() {
        // Add some users
        repo.save(createUser("user1", "user1@example.com", "USER"));
        repo.save(createUser("user2", "user2@example.com", "ADMIN"));
        
        List<User> users = repo.findAll();
        assertEquals(2, users.size());
        
        // Verify they're clones (not the same objects)
        for (User user : users) {
            assertNotSame(user, repo.findById(user.getUserId()).get());
        }
    }

    @Test
    void findByRole_returnsEmpty_whenNoMatch() {
        repo.save(createUser("u1", "u1@example.com", "USER"));
        List<User> users = repo.findByRole("ADMIN");
        assertTrue(users.isEmpty());
    }

    @Test
    void existsByUsername_returnsFalse_whenNotFound() {
        assertFalse(repo.existsByUsername("notfound"));
    }

    @Test
    void existsByEmail_returnsFalse_whenNotFound() {
        assertFalse(repo.existsByEmail("notfound@example.com"));
    }

    @Test
    void deleteById_doesNothing_whenNotFound() {
        repo.deleteById(999);
        // Should not throw
    }

    @Test
    void update_updatesExisting_whenIdExists() {
        // First save a user
        User user = createUser("existing", "existing@example.com", "USER");
        User saved = repo.save(user);
        Integer originalId = saved.getUserId();
        
        // Modify the user and update
        saved.setEmail("updated@example.com");
        saved.setRole("ADMIN");
        User updated = repo.update(saved);
        
        // Verify it was updated, not saved as new
        assertEquals(originalId, updated.getUserId());
        assertEquals("updated@example.com", updated.getEmail());
        assertEquals("ADMIN", updated.getRole());
        
        // Verify the stored user is also updated
        Optional<User> found = repo.findById(originalId);
        assertTrue(found.isPresent());
        assertEquals("updated@example.com", found.get().getEmail());
        assertEquals("ADMIN", found.get().getRole());
    }

    private User createUser(String username, String email, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("p");
        user.setEmail(email);
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
