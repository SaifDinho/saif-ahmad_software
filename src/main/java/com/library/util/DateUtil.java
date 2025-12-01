package com.library.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    
    public static int calculateDaysOverdue(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null) {
            returnDate = LocalDate.now();
        }
        
        if (returnDate.isBefore(dueDate)) {
            return 0;
        }
        
        return (int) ChronoUnit.DAYS.between(dueDate, returnDate);
    }

    public static boolean isOverdue(LocalDate dueDate) {
        return LocalDate.now().isAfter(dueDate);
    }

    public static LocalDate addDays(LocalDate date, int days) {
        return date.plusDays(days);
    }

    public static long getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
