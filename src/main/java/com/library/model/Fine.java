package com.library.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Fine implements Serializable {
    private static final long serialVersionUID = 1L;

    private int fineId;
    private int userId;
    private int recordId;
    private double fineAmount;
    private int daysOverdue;
    private boolean isPaid;
    private LocalDateTime calculationDate;

    public Fine() {
    }

    public Fine(int fineId, int userId, int recordId, double fineAmount, int daysOverdue, boolean isPaid, LocalDateTime calculationDate) {
        this.fineId = fineId;
        this.userId = userId;
        this.recordId = recordId;
        this.fineAmount = fineAmount;
        this.daysOverdue = daysOverdue;
        this.isPaid = isPaid;
        this.calculationDate = calculationDate;
    }

    public int getFineId() {
        return fineId;
    }

    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public int getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(int daysOverdue) {
        this.daysOverdue = daysOverdue;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LocalDateTime getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(LocalDateTime calculationDate) {
        this.calculationDate = calculationDate;
    }

    @Override
    public String toString() {
        return "Fine{" +
                "fineId=" + fineId +
                ", userId=" + userId +
                ", recordId=" + recordId +
                ", fineAmount=" + fineAmount +
                ", daysOverdue=" + daysOverdue +
                ", isPaid=" + isPaid +
                ", calculationDate=" + calculationDate +
                '}';
    }
}
