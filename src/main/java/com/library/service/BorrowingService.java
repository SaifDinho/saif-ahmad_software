package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import com.library.exception.BorrowingRestrictionException;
import com.library.exception.InsufficientStockException;
import com.library.util.Constants;
import com.library.util.TimeProvider;
import com.library.util.SystemTimeProvider;
import java.time.LocalDate;
import java.util.List;

public class BorrowingService {
    private BorrowingRecordRepository borrowingRecordRepository;
    private BookRepository bookRepository;
    private CDRepository cdRepository;
    private FineRepository fineRepository;
    private UserRepository userRepository;
    private TimeProvider timeProvider;

    public BorrowingService() {
        this.borrowingRecordRepository = new BorrowingRecordRepositoryImpl();
        this.bookRepository = new BookRepositoryImpl();
        this.cdRepository = new CDRepositoryImpl();
        this.fineRepository = new FineRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
        this.timeProvider = new SystemTimeProvider();
    }

    public BorrowingService(BorrowingRecordRepository borrowingRecordRepository,
                           BookRepository bookRepository,
                           CDRepository cdRepository,
                           FineRepository fineRepository,
                           UserRepository userRepository,
                           TimeProvider timeProvider) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.cdRepository = cdRepository;
        this.fineRepository = fineRepository;
        this.userRepository = userRepository;
        this.timeProvider = timeProvider;
    }

    public BorrowingRecord borrowBook(int userId, int bookId) throws BorrowingRestrictionException, InsufficientStockException {
        validateBorrowingEligibility(userId);

        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new InsufficientStockException("Book not found");
        }
        if (book.getQuantityAvailable() <= 0) {
            throw new InsufficientStockException("Book out of stock");
        }

        LocalDate borrowDate = timeProvider.getCurrentDate();
        LocalDate dueDate = borrowDate.plusDays(Constants.BOOK_LOAN_PERIOD_DAYS);

        BorrowingRecord record = new BorrowingRecord();
        record.setUserId(userId);
        record.setItemId(bookId);
        record.setItemType(BorrowingRecord.ItemType.BOOK);
        record.setBorrowDate(borrowDate);
        record.setDueDate(dueDate);
        record.setReturned(false);

        borrowingRecordRepository.save(record);

        // Decrement available quantity
        book.setQuantityAvailable(book.getQuantityAvailable() - 1);
        bookRepository.update(book);

        return record;
    }

    public BorrowingRecord borrowCD(int userId, int cdId) throws BorrowingRestrictionException, InsufficientStockException {
        validateBorrowingEligibility(userId);

        CD cd = cdRepository.findById(cdId);
        if (cd == null) {
            throw new InsufficientStockException("CD not found");
        }
        if (cd.getQuantityAvailable() <= 0) {
            throw new InsufficientStockException("CD out of stock");
        }

        LocalDate borrowDate = timeProvider.getCurrentDate();
        LocalDate dueDate = borrowDate.plusDays(Constants.CD_LOAN_PERIOD_DAYS);

        BorrowingRecord record = new BorrowingRecord();
        record.setUserId(userId);
        record.setItemId(cdId);
        record.setItemType(BorrowingRecord.ItemType.CD);
        record.setBorrowDate(borrowDate);
        record.setDueDate(dueDate);
        record.setReturned(false);

        borrowingRecordRepository.save(record);

        // Decrement available quantity
        cd.setQuantityAvailable(cd.getQuantityAvailable() - 1);
        cdRepository.update(cd);

        return record;
    }

    public List<BorrowingRecord> getUserBorrowingHistory(int userId) {
        return borrowingRecordRepository.findByUserId(userId);
    }

    public List<BorrowingRecord> getUserUnreturnedItems(int userId) {
        return borrowingRecordRepository.findUnreturnedByUserId(userId);
    }

    public int getUnreturnedItemCount(int userId) {
        return getUserUnreturnedItems(userId).size();
    }

    private void validateBorrowingEligibility(int userId) throws BorrowingRestrictionException {
        User user = userRepository.findById(userId);
        if (user == null || !user.isActive()) {
            throw new BorrowingRestrictionException("User is not active");
        }

        // Check if user has exceeded borrowing limit
        int unreturnedCount = getUnreturnedItemCount(userId);
        if (unreturnedCount >= Constants.MAX_ITEMS_PER_USER) {
            throw new BorrowingRestrictionException("User has reached maximum borrowing limit");
        }

        // Check if user has unpaid fines
        List<Fine> unpaidFines = fineRepository.findUnpaidByUserId(userId);
        double totalUnpaidFines = unpaidFines.stream().mapToDouble(Fine::getFineAmount).sum();
        if (totalUnpaidFines > Constants.MAX_FINE_THRESHOLD) {
            throw new BorrowingRestrictionException("User has outstanding fines exceeding limit of $" + Constants.MAX_FINE_THRESHOLD);
        }
    }
}
