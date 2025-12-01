package com.library.util;

import java.time.LocalDate;

public class SystemTimeProvider implements TimeProvider {
    
    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
