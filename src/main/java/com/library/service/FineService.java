package com.library.service;

import com.library.model.Fine;
import com.library.model.Payment;
import com.library.repository.*;
import java.time.LocalDateTime;
import java.util.List;

public class FineService {
    private FineRepository fineRepository;
    private PaymentRepository paymentRepository;

    public FineService() {
        this.fineRepository = new FineRepositoryImpl();
        this.paymentRepository = new PaymentRepositoryImpl();
    }

    public FineService(FineRepository fineRepository, PaymentRepository paymentRepository) {
        this.fineRepository = fineRepository;
        this.paymentRepository = paymentRepository;
    }

    public Fine getFine(int fineId) {
        return fineRepository.findById(fineId);
    }

    public List<Fine> getUserFines(int userId) {
        return fineRepository.findByUserId(userId);
    }

    public List<Fine> getUserUnpaidFines(int userId) {
        return fineRepository.findUnpaidByUserId(userId);
    }

    public double getTotalUnpaidFines(int userId) {
        List<Fine> unpaidFines = getUserUnpaidFines(userId);
        return unpaidFines.stream().mapToDouble(Fine::getFineAmount).sum();
    }

    public Payment payFine(int fineId, double amount, String paymentMethod) throws IllegalArgumentException {
        Fine fine = getFine(fineId);
        if (fine == null) {
            throw new IllegalArgumentException("Fine not found");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        if (amount > fine.getFineAmount()) {
            throw new IllegalArgumentException("Payment amount exceeds fine amount");
        }

        Payment payment = new Payment();
        payment.setFineId(fineId);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(paymentMethod);

        paymentRepository.save(payment);

        // Mark fine as paid if full payment
        if (amount == fine.getFineAmount()) {
            fine.setPaid(true);
            fineRepository.update(fine);
        }

        return payment;
    }

    public List<Payment> getFinePayments(int fineId) {
        return paymentRepository.findByFineId(fineId);
    }

    public double getTotalPaymentForFine(int fineId) {
        List<Payment> payments = getFinePayments(fineId);
        return payments.stream().mapToDouble(Payment::getAmount).sum();
    }
}
