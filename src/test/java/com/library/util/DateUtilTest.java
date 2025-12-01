package com.library.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testCalculateDaysOverdueNoOverdue() {
        LocalDate dueDate = LocalDate.of(2024, 12, 15);
        LocalDate returnDate = LocalDate.of(2024, 12, 15);
        int daysOverdue = DateUtil.calculateDaysOverdue(dueDate, returnDate);
        assertEquals(0, daysOverdue);
    }

    @Test
    public void testCalculateDaysOverdueBeforeDueDate() {
        LocalDate dueDate = LocalDate.of(2024, 12, 20);
        LocalDate returnDate = LocalDate.of(2024, 12, 15);
        int daysOverdue = DateUtil.calculateDaysOverdue(dueDate, returnDate);
        assertEquals(0, daysOverdue);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 30})
    public void testCalculateDaysOverdueVariousDays(int days) {
        LocalDate dueDate = LocalDate.of(2024, 12, 1);
        LocalDate returnDate = LocalDate.of(2024, 12, 1).plusDays(days);
        int daysOverdue = DateUtil.calculateDaysOverdue(dueDate, returnDate);
        assertEquals(days, daysOverdue);
    }

    @Test
    public void testCalculateDaysOverdueNullReturnDate() {
        LocalDate dueDate = LocalDate.of(2024, 12, 1);
        int daysOverdue = DateUtil.calculateDaysOverdue(dueDate, null);
        assertTrue(daysOverdue >= 0);
    }

    @Test
    public void testIsOverdue() {
        LocalDate pastDate = LocalDate.now().minusDays(5);
        assertTrue(DateUtil.isOverdue(pastDate));
    }

    @Test
    public void testIsNotOverdue() {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        assertFalse(DateUtil.isOverdue(futureDate));
    }

    @Test
    public void testAddDays() {
        LocalDate date = LocalDate.of(2024, 12, 1);
        LocalDate result = DateUtil.addDays(date, 10);
        assertEquals(LocalDate.of(2024, 12, 11), result);
    }

    @Test
    public void testGetDaysBetween() {
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 11);
        long days = DateUtil.getDaysBetween(startDate, endDate);
        assertEquals(10, days);
    }
}
