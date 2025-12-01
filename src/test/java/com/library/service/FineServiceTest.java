package com.library.service;

import com.library.model.Fine;
import com.library.repository.FineRepository;
import com.library.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FineServiceTest {

    @Mock
    private FineRepository fineRepository;

    @Mock
    private PaymentRepository paymentRepository;

    private FineService fineService;

    @BeforeEach
    public void setUp() {
        fineService = new FineService(fineRepository, paymentRepository);
    }

    @Test
    public void testGetFine() {
        Fine fine = new Fine();
        fine.setFineId(1);

        when(fineRepository.findById(1)).thenReturn(fine);

        Fine result = fineService.getFine(1);

        assertEquals(fine, result);
    }

    @Test
    public void testGetUserFines() {
        List<Fine> fines = new ArrayList<>();
        Fine fine1 = new Fine();
        fine1.setFineId(1);
        fines.add(fine1);

        when(fineRepository.findByUserId(1)).thenReturn(fines);

        List<Fine> result = fineService.getUserFines(1);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetUserUnpaidFines() {
        List<Fine> unpaidFines = new ArrayList<>();
        Fine fine = new Fine();
        fine.setFineId(1);
        fine.setPaid(false);
        fine.setFineAmount(50.0);
        unpaidFines.add(fine);

        when(fineRepository.findUnpaidByUserId(1)).thenReturn(unpaidFines);

        List<Fine> result = fineService.getUserUnpaidFines(1);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isPaid());
    }

    @Test
    public void testGetTotalUnpaidFines() {
        List<Fine> unpaidFines = new ArrayList<>();
        Fine fine1 = new Fine();
        fine1.setFineAmount(25.0);
        unpaidFines.add(fine1);

        Fine fine2 = new Fine();
        fine2.setFineAmount(30.0);
        unpaidFines.add(fine2);

        when(fineRepository.findUnpaidByUserId(1)).thenReturn(unpaidFines);

        double total = fineService.getTotalUnpaidFines(1);

        assertEquals(55.0, total);
    }

    @Test
    public void testPayFineSuccessfully() {
        Fine fine = new Fine();
        fine.setFineId(1);
        fine.setFineAmount(50.0);
        fine.setPaid(false);

        when(fineRepository.findById(1)).thenReturn(fine);

        var payment = fineService.payFine(1, 50.0, "CASH");

        assertNotNull(payment);
        assertEquals(50.0, payment.getAmount());
        verify(paymentRepository).save(any());
        verify(fineRepository).update(fine);
    }

    @Test
    public void testPayFineInvalidAmount() {
        Fine fine = new Fine();
        fine.setFineId(1);
        fine.setFineAmount(50.0);

        when(fineRepository.findById(1)).thenReturn(fine);

        assertThrows(IllegalArgumentException.class, () -> fineService.payFine(1, -10.0, "CASH"));
    }

    @Test
    public void testPayFineExceedsAmount() {
        Fine fine = new Fine();
        fine.setFineId(1);
        fine.setFineAmount(50.0);

        when(fineRepository.findById(1)).thenReturn(fine);

        assertThrows(IllegalArgumentException.class, () -> fineService.payFine(1, 60.0, "CASH"));
    }
}
