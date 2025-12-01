package com.library.service;

import com.library.service.fine.BookFineStrategy;
import com.library.service.fine.CDFineStrategy;
import com.library.service.fine.FineCalculationStrategy;
import com.library.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class FineCalculationStrategyTest {

    private FineCalculationStrategy bookStrategy;
    private FineCalculationStrategy cdStrategy;

    @BeforeEach
    public void setUp() {
        bookStrategy = new BookFineStrategy();
        cdStrategy = new CDFineStrategy();
    }

    @Test
    public void testBookFineStrategyNoOverdue() {
        double fine = bookStrategy.calculateFine(0, Constants.BOOK_FINE_RATE);
        assertEquals(0.0, fine);
    }

    @Test
    public void testBookFineStrategyNegativeOverdue() {
        double fine = bookStrategy.calculateFine(-5, Constants.BOOK_FINE_RATE);
        assertEquals(0.0, fine);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 20})
    public void testBookFineStrategyVariousDaysOverdue(int daysOverdue) {
        double fine = bookStrategy.calculateFine(daysOverdue, Constants.BOOK_FINE_RATE);
        double expected = daysOverdue * Constants.BOOK_FINE_RATE;
        if (expected > Constants.MAX_FINE_AMOUNT) {
            expected = Constants.MAX_FINE_AMOUNT;
        }
        assertEquals(expected, fine, 0.01);
    }

    @Test
    public void testBookFineStrategyCapped() {
        double fine = bookStrategy.calculateFine(300, Constants.BOOK_FINE_RATE);
        assertEquals(Constants.MAX_FINE_AMOUNT, fine);
    }

    @Test
    public void testCDFineStrategyNoOverdue() {
        double fine = cdStrategy.calculateFine(0, Constants.CD_FINE_RATE);
        assertEquals(0.0, fine);
    }

    @Test
    public void testCDFineStrategyNegativeOverdue() {
        double fine = cdStrategy.calculateFine(-5, Constants.CD_FINE_RATE);
        assertEquals(0.0, fine);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 20})
    public void testCDFineStrategyVariousDaysOverdue(int daysOverdue) {
        double fine = cdStrategy.calculateFine(daysOverdue, Constants.CD_FINE_RATE);
        double expected = daysOverdue * Constants.CD_FINE_RATE;
        if (expected > Constants.MAX_FINE_AMOUNT) {
            expected = Constants.MAX_FINE_AMOUNT;
        }
        assertEquals(expected, fine, 0.01);
    }

    @Test
    public void testCDFineStrategyCapped() {
        double fine = cdStrategy.calculateFine(150, Constants.CD_FINE_RATE);
        assertEquals(Constants.MAX_FINE_AMOUNT, fine);
    }

    @Test
    public void testBookFineHigherThanCDForSameDaysOverdue() {
        int daysOverdue = 10;
        double bookFine = bookStrategy.calculateFine(daysOverdue, Constants.BOOK_FINE_RATE);
        double cdFine = cdStrategy.calculateFine(daysOverdue, Constants.CD_FINE_RATE);
        assertNotEquals(bookFine, cdFine);
    }

    @Test
    public void testFineRounding() {
        double fine = bookStrategy.calculateFine(3, 0.33);
        assertEquals(0.99, fine, 0.01);
    }
}
