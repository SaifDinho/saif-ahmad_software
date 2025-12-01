package com.library.service.fine;

public interface FineCalculationStrategy {
    double calculateFine(int daysOverdue, double dailyRate);
}
