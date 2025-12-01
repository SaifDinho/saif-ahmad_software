package com.library.service;

import com.library.model.BorrowingRecord;
import com.library.model.Book;
import com.library.model.CD;
import com.library.model.Fine;
import com.library.repository.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    private BorrowingRecordRepository borrowingRecordRepository;
    private BookRepository bookRepository;
    private CDRepository cdRepository;
    private FineRepository fineRepository;
    private UserRepository userRepository;

    public ReportService() {
        this.borrowingRecordRepository = new BorrowingRecordRepositoryImpl();
        this.bookRepository = new BookRepositoryImpl();
        this.cdRepository = new CDRepositoryImpl();
        this.fineRepository = new FineRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
    }

    public ReportService(BorrowingRecordRepository borrowingRecordRepository,
                         BookRepository bookRepository,
                         CDRepository cdRepository,
                         FineRepository fineRepository,
                         UserRepository userRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.cdRepository = cdRepository;
        this.fineRepository = fineRepository;
        this.userRepository = userRepository;
    }

    public String generateMixedMediaOverdueReport() {
        List<BorrowingRecord> overdueRecords = borrowingRecordRepository.findOverdueRecords();
        StringBuilder report = new StringBuilder();

        report.append("=== MIXED MEDIA OVERDUE REPORT ===\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n");
        report.append("Total overdue items: ").append(overdueRecords.size()).append("\n\n");

        double totalFines = 0.0;

        for (BorrowingRecord record : overdueRecords) {
            String itemName = getItemName(record);
            String userName = getUserName(record.getUserId());

            List<Fine> fines = fineRepository.findByRecordId(record.getRecordId());
            double recordFine = fines.stream().mapToDouble(Fine::getFineAmount).sum();
            totalFines += recordFine;

            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(record.getDueDate(), LocalDate.now());

            report.append("Item Type: ").append(record.getItemType()).append("\n");
            report.append("Item: ").append(itemName).append("\n");
            report.append("User: ").append(userName).append("\n");
            report.append("Due Date: ").append(record.getDueDate()).append("\n");
            report.append("Days Overdue: ").append(daysOverdue).append("\n");
            report.append("Fine Amount: $").append(String.format("%.2f", recordFine)).append("\n");
            report.append("---\n");
        }

        report.append("\nTotal Fines: $").append(String.format("%.2f", totalFines)).append("\n");

        return report.toString();
    }

    public String generateActiveBorrowingsReport() {
        List<BorrowingRecord> activeRecords = new ArrayList<>();
        List<BorrowingRecord> allRecords = borrowingRecordRepository.findAll();

        for (BorrowingRecord record : allRecords) {
            if (!record.isReturned()) {
                activeRecords.add(record);
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("=== ACTIVE BORROWINGS REPORT ===\n");
        report.append("Generated on: ").append(LocalDate.now()).append("\n");
        report.append("Total active items: ").append(activeRecords.size()).append("\n\n");

        for (BorrowingRecord record : activeRecords) {
            String itemName = getItemName(record);
            String userName = getUserName(record.getUserId());

            report.append("Item: ").append(itemName).append("\n");
            report.append("User: ").append(userName).append("\n");
            report.append("Borrowed: ").append(record.getBorrowDate()).append("\n");
            report.append("Due: ").append(record.getDueDate()).append("\n");
            report.append("---\n");
        }

        return report.toString();
    }

    private String getItemName(BorrowingRecord record) {
        if (record.getItemType() == BorrowingRecord.ItemType.BOOK) {
            Book book = bookRepository.findById(record.getItemId());
            return book != null ? book.getTitle() + " by " + book.getAuthor() : "Unknown Book";
        } else {
            CD cd = cdRepository.findById(record.getItemId());
            return cd != null ? cd.getTitle() + " by " + cd.getArtist() : "Unknown CD";
        }
    }

    private String getUserName(int userId) {
        var user = userRepository.findById(userId);
        return user != null ? user.getName() : "Unknown User";
    }
}
