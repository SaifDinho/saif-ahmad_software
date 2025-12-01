package com.library.service;

import com.library.model.*;
import com.library.repository.*;
import com.library.util.DateUtil;
import com.library.service.fine.FineCalculationStrategy;
import com.library.service.fine.BookFineStrategy;
import com.library.service.fine.CDFineStrategy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReturnService {
    private BorrowingRecordRepository borrowingRecordRepository;
    private FineRepository fineRepository;
    private BookRepository bookRepository;
    private CDRepository cdRepository;

    public ReturnService() {
        this.borrowingRecordRepository = new BorrowingRecordRepositoryImpl();
        this.fineRepository = new FineRepositoryImpl();
        this.bookRepository = new BookRepositoryImpl();
        this.cdRepository = new CDRepositoryImpl();
    }

    public ReturnService(BorrowingRecordRepository borrowingRecordRepository,
                         FineRepository fineRepository,
                         BookRepository bookRepository,
                         CDRepository cdRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.fineRepository = fineRepository;
        this.bookRepository = bookRepository;
        this.cdRepository = cdRepository;
    }

    public Fine returnItem(int recordId, LocalDate returnDate) {
        BorrowingRecord record = borrowingRecordRepository.findById(recordId);
        if (record == null) {
            throw new IllegalArgumentException("Borrowing record not found");
        }

        if (record.isReturned()) {
            throw new IllegalStateException("Item already returned");
        }

        record.setReturnDate(returnDate);
        record.setReturned(true);
        borrowingRecordRepository.update(record);

        // Update inventory quantity
        if (record.getItemType() == BorrowingRecord.ItemType.BOOK) {
            Book book = bookRepository.findById(record.getItemId());
            if (book != null) {
                book.setQuantityAvailable(book.getQuantityAvailable() + 1);
                bookRepository.update(book);
            }
        } else {
            CD cd = cdRepository.findById(record.getItemId());
            if (cd != null) {
                cd.setQuantityAvailable(cd.getQuantityAvailable() + 1);
                cdRepository.update(cd);
            }
        }

        // Calculate fine if overdue
        Fine fine = calculateFineIfOverdue(record);

        return fine;
    }

    private Fine calculateFineIfOverdue(BorrowingRecord record) {
        LocalDate returnDate = record.getReturnDate();
        LocalDate dueDate = record.getDueDate();

        int daysOverdue = DateUtil.calculateDaysOverdue(dueDate, returnDate);

        if (daysOverdue <= 0) {
            return null; // No fine
        }

        FineCalculationStrategy strategy;
        double dailyRate;

        if (record.getItemType() == BorrowingRecord.ItemType.BOOK) {
            Book book = bookRepository.findById(record.getItemId());
            dailyRate = book.getDailyFineRate();
            strategy = new BookFineStrategy();
        } else {
            CD cd = cdRepository.findById(record.getItemId());
            dailyRate = cd.getDailyFineRate();
            strategy = new CDFineStrategy();
        }

        double fineAmount = strategy.calculateFine(daysOverdue, dailyRate);

        Fine fine = new Fine();
        fine.setUserId(record.getUserId());
        fine.setRecordId(record.getRecordId());
        fine.setFineAmount(fineAmount);
        fine.setDaysOverdue(daysOverdue);
        fine.setPaid(false);
        fine.setCalculationDate(LocalDateTime.now());

        fineRepository.save(fine);

        return fine;
    }

    public List<BorrowingRecord> getOverdueRecords() {
        return borrowingRecordRepository.findOverdueRecords();
    }
}
