package com.library.repository;

import com.library.model.BorrowingRecord;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class BorrowingRecordRepositoryImpl implements BorrowingRecordRepository {
    private Connection connection;

    public BorrowingRecordRepositoryImpl() {
        this.connection = Database.getInstance().getConnection();
    }

    @Override
    public void save(BorrowingRecord record) {
        String query = "INSERT INTO borrowing_records (user_id, item_id, item_type, borrow_date, due_date, return_date, is_returned) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, record.getUserId());
            ps.setInt(2, record.getItemId());
            ps.setString(3, record.getItemType().toString());
            ps.setString(4, record.getBorrowDate().toString());
            ps.setString(5, record.getDueDate().toString());
            ps.setString(6, record.getReturnDate() != null ? record.getReturnDate().toString() : null);
            ps.setInt(7, record.isReturned() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving borrowing record", e);
        }
    }

    @Override
    public void update(BorrowingRecord record) {
        String query = "UPDATE borrowing_records SET user_id=?, item_id=?, item_type=?, borrow_date=?, due_date=?, return_date=?, is_returned=? WHERE record_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, record.getUserId());
            ps.setInt(2, record.getItemId());
            ps.setString(3, record.getItemType().toString());
            ps.setString(4, record.getBorrowDate().toString());
            ps.setString(5, record.getDueDate().toString());
            ps.setString(6, record.getReturnDate() != null ? record.getReturnDate().toString() : null);
            ps.setInt(7, record.isReturned() ? 1 : 0);
            ps.setInt(8, record.getRecordId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating borrowing record", e);
        }
    }

    @Override
    public void delete(int recordId) {
        String query = "DELETE FROM borrowing_records WHERE record_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, recordId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting borrowing record", e);
        }
    }

    @Override
    public BorrowingRecord findById(int recordId) {
        String query = "SELECT * FROM borrowing_records WHERE record_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, recordId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToRecord(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding borrowing record by id", e);
        }
        return null;
    }

    @Override
    public List<BorrowingRecord> findByUserId(int userId) {
        String query = "SELECT * FROM borrowing_records WHERE user_id=?";
        return executeQuery(query, userId);
    }

    @Override
    public List<BorrowingRecord> findByItemId(int itemId) {
        String query = "SELECT * FROM borrowing_records WHERE item_id=?";
        return executeQuery(query, itemId);
    }

    @Override
    public List<BorrowingRecord> findAll() {
        String query = "SELECT * FROM borrowing_records";
        List<BorrowingRecord> records = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all borrowing records", e);
        }
        return records;
    }

    @Override
    public List<BorrowingRecord> findUnreturnedByUserId(int userId) {
        String query = "SELECT * FROM borrowing_records WHERE user_id=? AND is_returned=0";
        List<BorrowingRecord> records = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding unreturned borrowing records", e);
        }
        return records;
    }

    @Override
    public List<BorrowingRecord> findOverdueRecords() {
        String query = "SELECT * FROM borrowing_records WHERE is_returned=0 AND due_date < ?";
        List<BorrowingRecord> records = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, LocalDate.now().toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding overdue records", e);
        }
        return records;
    }

    private List<BorrowingRecord> executeQuery(String query, int userId) {
        List<BorrowingRecord> records = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
        return records;
    }

    private BorrowingRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        BorrowingRecord record = new BorrowingRecord();
        record.setRecordId(rs.getInt("record_id"));
        record.setUserId(rs.getInt("user_id"));
        record.setItemId(rs.getInt("item_id"));
        record.setItemType(BorrowingRecord.ItemType.valueOf(rs.getString("item_type")));
        record.setBorrowDate(LocalDate.parse(rs.getString("borrow_date")));
        record.setDueDate(LocalDate.parse(rs.getString("due_date")));
        String returnDateStr = rs.getString("return_date");
        if (returnDateStr != null) {
            record.setReturnDate(LocalDate.parse(returnDateStr));
        }
        record.setReturned(rs.getInt("is_returned") == 1);
        return record;
    }
}
