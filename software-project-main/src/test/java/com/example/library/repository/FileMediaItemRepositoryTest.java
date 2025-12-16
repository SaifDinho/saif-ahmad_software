package com.example.library.repository;

import com.example.library.domain.MediaItem;
import com.example.library.util.XmlDataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FileMediaItemRepositoryTest {

    @TempDir
    Path tempDir;

    private FileMediaItemRepository repo;

    @BeforeEach
    void setUp() {
        System.setProperty("xml.data.dir", tempDir.toString() + java.io.File.separator);
        java.io.File f = new java.io.File(XmlDataManager.getDataFilePath("media_items.xml"));
        if (f.exists()) {
            f.delete();
        }
        repo = new FileMediaItemRepository();
    }

    @Test
    void saveFindAndExists() {
        MediaItem item = new MediaItem();
        item.setTitle("Test Book");
        item.setAuthor("Author");
        item.setType("BOOK");
        item.setIsbn("ISBN-1234");
        item.setPublicationDate(LocalDate.of(2020,1,1));
        item.setPublisher("Pub");
        item.setTotalCopies(5);
        item.setAvailableCopies(5);
        item.setLateFeesPerDay(new BigDecimal("0.50"));

        MediaItem saved = repo.save(item);
        assertNotNull(saved.getItemId());
        assertTrue(repo.existsByIsbn("ISBN-1234"));
        assertTrue(repo.findByIsbn("ISBN-1234").isPresent());
        assertEquals("Test Book", repo.findByIsbn("ISBN-1234").get().getTitle());
    }

    @Test
    void updateAndDelete() {
        MediaItem item = new MediaItem();
        item.setTitle("Book2");
        item.setAuthor("A");
        item.setType("BOOK");
        item.setIsbn("ISBN-DEL");
        item.setTotalCopies(3);
        item.setAvailableCopies(3);

        MediaItem saved = repo.save(item);
        repo.updateAvailableCopies(saved.getItemId(), 1);
        assertEquals(1, repo.findById(saved.getItemId()).get().getAvailableCopies());
        assertTrue(repo.deleteById(saved.getItemId()));
        assertFalse(repo.findById(saved.getItemId()).isPresent());
    }

    // Branch/edge coverage tests

    @Test
    void save_generatesId_whenNull() {
        MediaItem item = new MediaItem();
        item.setTitle("Test");
        item.setType("BOOK");
        MediaItem saved = repo.save(item);
        assertNotNull(saved.getItemId());
    }

    @Test
    void save_keepsId_whenNotNull() {
        MediaItem item = new MediaItem();
        item.setItemId(99);
        item.setTitle("Test");
        item.setType("BOOK");
        MediaItem saved = repo.save(item);
        assertEquals(99, saved.getItemId());
    }

    @Test
    void update_savesNew_whenIdNull() {
        MediaItem item = new MediaItem();
        item.setTitle("New");
        item.setType("BOOK");
        MediaItem updated = repo.update(item);
        assertNotNull(updated.getItemId());
    }

    @Test
    void update_savesNew_whenIdNotExists() {
        MediaItem item = new MediaItem();
        item.setItemId(999);
        item.setTitle("New");
        item.setType("BOOK");
        MediaItem updated = repo.update(item);
        assertEquals(999, updated.getItemId());
    }

    @Test
    void findById_returnsEmpty_whenNotFound() {
        Optional<MediaItem> opt = repo.findById(999);
        assertFalse(opt.isPresent());
    }

    @Test
    void findByIsbn_returnsEmpty_whenNotFound() {
        Optional<MediaItem> opt = repo.findByIsbn("notfound");
        assertFalse(opt.isPresent());
    }

    @Test
    void findAll_returnsEmpty_whenNoItems() {
        List<MediaItem> items = repo.findAll();
        assertTrue(items.isEmpty());
    }

    @Test
    void findByType_returnsEmpty_whenNoMatch() {
        repo.save(createItem("A", "B", "BOOK", "123"));
        List<MediaItem> items = repo.findByType("DVD");
        assertTrue(items.isEmpty());
    }

    @Test
    void findByTitleContaining_returnsEmpty_whenNoMatch() {
        repo.save(createItem("Title", "Author", "BOOK", "123"));
        List<MediaItem> items = repo.findByTitleContaining("NotInTitle");
        assertTrue(items.isEmpty());
    }

    @Test
    void findByTitleContaining_skipsNullTitles() {
        MediaItem item = createItem(null, "Author", "BOOK", "123");
        repo.save(item);
        List<MediaItem> items = repo.findByTitleContaining("anything");
        assertTrue(items.isEmpty());
    }

    @Test
    void findByAuthorContaining_returnsEmpty_whenNoMatch() {
        repo.save(createItem("Title", "Author", "BOOK", "123"));
        List<MediaItem> items = repo.findByAuthorContaining("NotInAuthor");
        assertTrue(items.isEmpty());
    }

    @Test
    void findByAuthorContaining_skipsNullAuthors() {
        MediaItem item = createItem("Title", null, "BOOK", "123");
        repo.save(item);
        List<MediaItem> items = repo.findByAuthorContaining("anything");
        assertTrue(items.isEmpty());
    }

    @Test
    void findAvailableItems_returnsEmpty_whenNoneAvailable() {
        MediaItem item = createItem("Title", "Author", "BOOK", "123");
        item.setAvailableCopies(0);
        repo.save(item);
        List<MediaItem> items = repo.findAvailableItems();
        assertTrue(items.isEmpty());
    }

    @Test
    void findAvailableItems_skipsNullAvailableCopies() {
        MediaItem item = createItem("Title", "Author", "BOOK", "123");
        item.setAvailableCopies(null);
        repo.save(item);
        List<MediaItem> items = repo.findAvailableItems();
        assertTrue(items.isEmpty());
    }

    @Test
    void deleteById_returnsFalse_whenNotFound() {
        boolean deleted = repo.deleteById(999);
        assertFalse(deleted);
    }

    @Test
    void updateAvailableCopies_doesNothing_whenNotFound() {
        repo.updateAvailableCopies(999, 5);
        // Should not throw
    }

    @Test
    void search_returnsEmpty_whenNoMatch() {
        repo.save(createItem("Title", "Author", "BOOK", "123"));
        List<MediaItem> items = repo.search("notfound");
        assertTrue(items.isEmpty());
    }

    @Test
    void search_skipsNullFields() {
        MediaItem item = new MediaItem();
        item.setItemId(1);
        item.setTitle(null);
        item.setAuthor(null);
        item.setIsbn(null);
        item.setType(null);
        repo.save(item);
        List<MediaItem> items = repo.search("any");
        assertTrue(items.isEmpty());
    }

    @Test
    void existsByIsbn_returnsFalse_whenNotFound() {
        assertFalse(repo.existsByIsbn("notfound"));
    }

    private MediaItem createItem(String title, String author, String type, String isbn) {
        MediaItem item = new MediaItem();
        item.setTitle(title);
        item.setAuthor(author);
        item.setType(type);
        item.setIsbn(isbn);
        item.setPublicationDate(LocalDate.now());
        item.setTotalCopies(1);
        item.setAvailableCopies(1);
        item.setLateFeesPerDay(BigDecimal.ONE);
        return item;
    }
}
