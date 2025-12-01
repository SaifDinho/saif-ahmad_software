package com.library.repository;

import com.library.model.Payment;
import java.util.List;

public interface PaymentRepository {
    void save(Payment payment);
    void delete(int paymentId);
    Payment findById(int paymentId);
    List<Payment> findByFineId(int fineId);
    List<Payment> findAll();
}
