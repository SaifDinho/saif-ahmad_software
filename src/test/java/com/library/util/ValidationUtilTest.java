package com.library.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    public void testValidEmailValidation() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user.name@domain.co.uk"));
    }

    @Test
    public void testInvalidEmailValidation() {
        assertFalse(ValidationUtil.isValidEmail("invalid.email"));
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "9876543210"})
    public void testValidPhoneValidation(String phone) {
        assertTrue(ValidationUtil.isValidPhone(phone));
    }

    @Test
    public void testInvalidPhoneValidation() {
        assertFalse(ValidationUtil.isValidPhone("12345"));
        assertFalse(ValidationUtil.isValidPhone("123456789012345"));
        assertFalse(ValidationUtil.isValidPhone("abc"));
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone(null));
    }

    @Test
    public void testValidISBNValidation() {
        assertTrue(ValidationUtil.isValidISBN("978-0-123456-78-9"));
        assertTrue(ValidationUtil.isValidISBN("0123456789"));
    }

    @Test
    public void testInvalidISBNValidation() {
        assertFalse(ValidationUtil.isValidISBN("invalid"));
        assertFalse(ValidationUtil.isValidISBN(""));
        assertFalse(ValidationUtil.isValidISBN(null));
    }

    @Test
    public void testIsNotEmpty() {
        assertTrue(ValidationUtil.isNotEmpty("value"));
        assertFalse(ValidationUtil.isNotEmpty(""));
        assertFalse(ValidationUtil.isNotEmpty(null));
        assertFalse(ValidationUtil.isNotEmpty("   "));
    }

    @Test
    public void testIsPositiveInt() {
        assertTrue(ValidationUtil.isPositive(5));
        assertTrue(ValidationUtil.isPositive(1));
        assertFalse(ValidationUtil.isPositive(0));
        assertFalse(ValidationUtil.isPositive(-5));
    }

    @Test
    public void testIsPositiveDouble() {
        assertTrue(ValidationUtil.isPositive(5.5));
        assertTrue(ValidationUtil.isPositive(0.01));
        assertFalse(ValidationUtil.isPositive(0.0));
        assertFalse(ValidationUtil.isPositive(-5.5));
    }

    @Test
    public void testIsNonNegative() {
        assertTrue(ValidationUtil.isNonNegative(5.5));
        assertTrue(ValidationUtil.isNonNegative(0.0));
        assertFalse(ValidationUtil.isNonNegative(-5.5));
    }
}
