package com.example.library.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MediaItemTest {

    @Test
    void testGettersSetters() {
        MediaItem item = new MediaItem();
        item.setItemId(10);
        item.setTitle("Effective Java");
        item.setAuthor("Joshua Bloch");
        item.setType("BOOK");
        item.setIsbn("978-0134685991");
        item.setPublicationDate(LocalDate.of(2018, 1, 1));
        item.setPublisher("Addison-Wesley");
        item.setTotalCopies(5);
        item.setAvailableCopies(3);
        item.setLateFeesPerDay(new BigDecimal("2.00"));

        assertEquals(10, item.getItemId());
        assertEquals("Effective Java", item.getTitle());
        assertEquals("Joshua Bloch", item.getAuthor());
        assertEquals("BOOK", item.getType());
        assertEquals("978-0134685991", item.getIsbn());
        assertEquals(LocalDate.of(2018, 1, 1), item.getPublicationDate());
        assertEquals("Addison-Wesley", item.getPublisher());
        assertEquals(5, item.getTotalCopies());
        assertEquals(3, item.getAvailableCopies());
        assertEquals(0, new BigDecimal("2.00").compareTo(item.getLateFeesPerDay()));
    }

    @Test
    void testToString() {
        MediaItem item = new MediaItem();
        item.setItemId(1);
        item.setTitle("Clean Code");
        item.setAuthor("Robert C. Martin");

        String s = item.toString();
        assertTrue(s.contains("Clean Code"));
        assertTrue(s.contains("Robert C. Martin"));
    }
}
