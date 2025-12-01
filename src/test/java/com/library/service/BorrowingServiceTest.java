package com.library.service;

import com.library.model.BorrowingRecord;
import com.library.repository.BorrowingRecordRepository;
import com.library.repository.BookRepository;
import com.library.repository.CDRepository;
import com.library.repository.FineRepository;
import com.library.repository.UserRepository;
import com.library.model.Book;
import com.library.model.CD;
import com.library.model.User;
import com.library.model.Fine;
import com.library.exception.BorrowingRestrictionException;
import com.library.exception.InsufficientStockException;
import com.library.util.Constants;
import com.library.util.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CDRepository cdRepository;

    @Mock
    private FineRepository fineRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimeProvider timeProvider;

    private BorrowingService borrowingService;

    @BeforeEach
    public void setUp() {
        borrowingService = new BorrowingService(
                borrowingRecordRepository,
                bookRepository,
                cdRepository,
                fineRepository,
                userRepository,
                timeProvider
        );
    }

    @Test
    public void testBorrowBookSuccessfully() throws Exception {
        int userId = 1;
        int bookId = 1;
        LocalDate borrowDate = LocalDate.of(2024, 12, 1);

        User user = new User();
        user.setUserId(userId);
        user.setActive(true);

        Book book = new Book();
        book.setBookId(bookId);
        book.setQuantityAvailable(5);

        when(userRepository.findById(userId)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(book);
        when(timeProvider.getCurrentDate()).thenReturn(borrowDate);
        when(fineRepository.findUnpaidByUserId(userId)).thenReturn(new ArrayList<>());
        when(borrowingRecordRepository.findUnreturnedByUserId(userId)).thenReturn(new ArrayList<>());

        BorrowingRecord record = borrowingService.borrowBook(userId, bookId);

        assertNotNull(record);
        assertEquals(userId, record.getUserId());
        assertEquals(bookId, record.getItemId());
        assertEquals(borrowDate, record.getBorrowDate());
        assertEquals(borrowDate.plusDays(Constants.BOOK_LOAN_PERIOD_DAYS), record.getDueDate());
        assertFalse(record.isReturned());

        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
        verify(bookRepository).update(any(Book.class));
    }

    @Test
    public void testBorrowCDSuccessfully() throws Exception {
        int userId = 1;
        int cdId = 1;
        LocalDate borrowDate = LocalDate.of(2024, 12, 1);

        User user = new User();
        user.setUserId(userId);
        user.setActive(true);

        CD cd = new CD();
        cd.setCdId(cdId);
        cd.setQuantityAvailable(3);

        when(userRepository.findById(userId)).thenReturn(user);
        when(cdRepository.findById(cdId)).thenReturn(cd);
        when(timeProvider.getCurrentDate()).thenReturn(borrowDate);
        when(fineRepository.findUnpaidByUserId(userId)).thenReturn(new ArrayList<>());
        when(borrowingRecordRepository.findUnreturnedByUserId(userId)).thenReturn(new ArrayList<>());

        BorrowingRecord record = borrowingService.borrowCD(userId, cdId);

        assertNotNull(record);
        assertEquals(userId, record.getUserId());
        assertEquals(cdId, record.getItemId());
        assertEquals(borrowDate, record.getBorrowDate());
        assertEquals(borrowDate.plusDays(Constants.CD_LOAN_PERIOD_DAYS), record.getDueDate());
        assertFalse(record.isReturned());

        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
        verify(cdRepository).update(any(CD.class));
    }

    @Test
    public void testBorrowBookOutOfStock() {
        int userId = 1;
        int bookId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setActive(true);

        Book book = new Book();
        book.setBookId(bookId);
        book.setQuantityAvailable(0);

        when(userRepository.findById(userId)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(book);
        when(fineRepository.findUnpaidByUserId(userId)).thenReturn(new ArrayList<>());
        when(borrowingRecordRepository.findUnreturnedByUserId(userId)).thenReturn(new ArrayList<>());

        assertThrows(InsufficientStockException.class, () -> borrowingService.borrowBook(userId, bookId));
    }

    @Test
    public void testBorrowRestrictionUnpaidFines() {
        int userId = 1;
        int bookId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setActive(true);

        Fine fine = new Fine();
        fine.setFineAmount(Constants.MAX_FINE_THRESHOLD + 10);

        when(userRepository.findById(userId)).thenReturn(user);
        when(fineRepository.findUnpaidByUserId(userId)).thenReturn(Collections.singletonList(fine));
        when(borrowingRecordRepository.findUnreturnedByUserId(userId)).thenReturn(new ArrayList<>());

        assertThrows(BorrowingRestrictionException.class, () -> borrowingService.borrowBook(userId, bookId));
    }

    @Test
    public void testBorrowRestrictionMaxItems() {
        int userId = 1;
        int bookId = 1;

        User user = new User();
        user.setUserId(userId);
        user.setActive(true);

        java.util.List<BorrowingRecord> maxedItems = new ArrayList<>();
        for (int i = 0; i < Constants.MAX_ITEMS_PER_USER; i++) {
            BorrowingRecord record = new BorrowingRecord();
            record.setUserId(userId);
            maxedItems.add(record);
        }

        when(userRepository.findById(userId)).thenReturn(user);
        when(borrowingRecordRepository.findUnreturnedByUserId(userId)).thenReturn(maxedItems);

        assertThrows(BorrowingRestrictionException.class, () -> borrowingService.borrowBook(userId, bookId));
    }

    @Test
    public void testGetUserUnreturnedItems() {
        int userId = 1;
        java.util.List<BorrowingRecord> records = new ArrayList<>();

        BorrowingRecord record1 = new BorrowingRecord();
        record1.setUserId(userId);
        record1.setReturned(false);
        records.add(record1);

        when(borrowingRecordRepository.findUnreturnedByUserId(userId)).thenReturn(records);

        java.util.List<BorrowingRecord> result = borrowingService.getUserUnreturnedItems(userId);

        assertEquals(1, result.size());
    }
}
