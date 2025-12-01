package com.library.service.fine;

import com.library.util.Constants;

public class BookFineStrategy implements FineCalculationStrategy {
    
    @Override
    public double calculateFine(int daysOverdue, double dailyRate) {
        if (daysOverdue <= 0) {
            return 0.0;
        }
        
        double fine = daysOverdue * dailyRate;
        
        // Cap the fine at maximum allowed amount
        if (fine > Constants.MAX_FINE_AMOUNT) {
            fine = Constants.MAX_FINE_AMOUNT;
        }
        
        return Math.round(fine * 100.0) / 100.0; // Round to 2 decimal places
    }
}
