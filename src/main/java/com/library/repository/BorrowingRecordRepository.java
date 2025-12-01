package com.library.repository;

import com.library.model.BorrowingRecord;
import java.util.List;

public interface BorrowingRecordRepository {
    void save(BorrowingRecord record);
    void update(BorrowingRecord record);
    void delete(int recordId);
    BorrowingRecord findById(int recordId);
    List<BorrowingRecord> findByUserId(int userId);
    List<BorrowingRecord> findByItemId(int itemId);
    List<BorrowingRecord> findAll();
    List<BorrowingRecord> findUnreturnedByUserId(int userId);
    List<BorrowingRecord> findOverdueRecords();
}
