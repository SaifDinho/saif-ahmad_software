package com.library.repository;

import com.library.model.Fine;
import java.util.List;

public interface FineRepository {
    void save(Fine fine);
    void update(Fine fine);
    void delete(int fineId);
    Fine findById(int fineId);
    List<Fine> findByUserId(int userId);
    List<Fine> findByRecordId(int recordId);
    List<Fine> findAll();
    List<Fine> findUnpaidByUserId(int userId);
}
