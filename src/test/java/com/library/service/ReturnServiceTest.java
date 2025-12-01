package com.library.service;

import com.library.model.BorrowingRecord;
import com.library.model.Fine;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.BookRepository;
import com.library.repository.CDRepository;
import com.library.repository.FineRepository;
import com.library.model.Book;
import com.library.model.CD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReturnServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CDRepository cdRepository;

    private ReturnService returnService;

    @BeforeEach
    public void setUp() {
        returnService = new ReturnService(
                borrowingRecordRepository,
                fineRepository,
                bookRepository,
                cdRepository
        );
    }

    @Test
    public void testReturnBookOnTime() {
        int recordId = 1;
        LocalDate dueDate = LocalDate.of(2024, 12, 15);
        LocalDate returnDate = LocalDate.of(2024, 12, 10);

        BorrowingRecord record = new BorrowingRecord();
        record.setRecordId(recordId);
        record.setItemType(BorrowingRecord.ItemType.BOOK);
        record.setItemId(1);
        record.setDueDate(dueDate);
        record.setReturned(false);

        Book book = new Book();
        book.setBookId(1);
        book.setQuantityAvailable(5);

        when(borrowingRecordRepository.findById(recordId)).thenReturn(record);
        when(bookRepository.findById(1)).thenReturn(book);

        Fine fine = returnService.returnItem(recordId, returnDate);

        assertNull(fine);
        verify(borrowingRecordRepository).update(any(BorrowingRecord.class));
        verify(bookRepository).update(any(Book.class));
    }

    @Test
    public void testReturnBookOverdue() {
        int recordId = 1;
        LocalDate dueDate = LocalDate.of(2024, 12, 10);
        LocalDate returnDate = LocalDate.of(2024, 12, 20);

        BorrowingRecord record = new BorrowingRecord();
        record.setRecordId(recordId);
        record.setUserId(1);
        record.setItemType(BorrowingRecord.ItemType.BOOK);
        record.setItemId(1);
        record.setDueDate(dueDate);
        record.setReturned(false);

        Book book = new Book();
        book.setBookId(1);
        book.setQuantityAvailable(5);
        book.setDailyFineRate(0.50);

        when(borrowingRecordRepository.findById(recordId)).thenReturn(record);
        when(bookRepository.findById(1)).thenReturn(book);

        Fine fine = returnService.returnItem(recordId, returnDate);

        assertNotNull(fine);
        assertEquals(1, fine.getUserId());
        assertEquals(10, fine.getDaysOverdue());
        assertTrue(fine.getFineAmount() > 0);

        verify(borrowingRecordRepository).update(any(BorrowingRecord.class));
        verify(bookRepository).update(any(Book.class));
        verify(fineRepository).save(any(Fine.class));
    }

    @Test
    public void testReturnCDOverdue() {
        int recordId = 1;
        LocalDate dueDate = LocalDate.of(2024, 12, 10);
        LocalDate returnDate = LocalDate.of(2024, 12, 15);

        BorrowingRecord record = new BorrowingRecord();
        record.setRecordId(recordId);
        record.setUserId(1);
        record.setItemType(BorrowingRecord.ItemType.CD);
        record.setItemId(1);
        record.setDueDate(dueDate);
        record.setReturned(false);

        CD cd = new CD();
        cd.setCdId(1);
        cd.setQuantityAvailable(3);
        cd.setDailyFineRate(1.00);

        when(borrowingRecordRepository.findById(recordId)).thenReturn(record);
        when(cdRepository.findById(1)).thenReturn(cd);

        Fine fine = returnService.returnItem(recordId, returnDate);

        assertNotNull(fine);
        assertEquals(1, fine.getUserId());
        assertEquals(5, fine.getDaysOverdue());
        assertEquals(5.00, fine.getFineAmount());

        verify(borrowingRecordRepository).update(any(BorrowingRecord.class));
        verify(cdRepository).update(any(CD.class));
        verify(fineRepository).save(any(Fine.class));
    }

    @Test
    public void testReturnAlreadyReturnedItem() {
        int recordId = 1;

        BorrowingRecord record = new BorrowingRecord();
        record.setRecordId(recordId);
        record.setReturned(true);

        when(borrowingRecordRepository.findById(recordId)).thenReturn(record);

        assertThrows(IllegalStateException.class, () -> returnService.returnItem(recordId, LocalDate.now()));
    }

    @Test
    public void testGetOverdueRecords() {
        java.util.List<BorrowingRecord> overdueRecords = new java.util.ArrayList<>();
        BorrowingRecord record = new BorrowingRecord();
        overdueRecords.add(record);

        when(borrowingRecordRepository.findOverdueRecords()).thenReturn(overdueRecords);

        java.util.List<BorrowingRecord> result = returnService.getOverdueRecords();

        assertEquals(1, result.size());
    }
}
