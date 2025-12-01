package com.library.repository;

import com.library.model.Fine;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class FineRepositoryImpl implements FineRepository {
    private Connection connection;

    public FineRepositoryImpl() {
        this.connection = Database.getInstance().getConnection();
    }

    @Override
    public void save(Fine fine) {
        String query = "INSERT INTO fines (user_id, record_id, fine_amount, days_overdue, is_paid, calculation_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fine.getUserId());
            ps.setInt(2, fine.getRecordId());
            ps.setDouble(3, fine.getFineAmount());
            ps.setInt(4, fine.getDaysOverdue());
            ps.setInt(5, fine.isPaid() ? 1 : 0);
            ps.setString(6, fine.getCalculationDate().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving fine", e);
        }
    }

    @Override
    public void update(Fine fine) {
        String query = "UPDATE fines SET user_id=?, record_id=?, fine_amount=?, days_overdue=?, is_paid=?, calculation_date=? WHERE fine_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fine.getUserId());
            ps.setInt(2, fine.getRecordId());
            ps.setDouble(3, fine.getFineAmount());
            ps.setInt(4, fine.getDaysOverdue());
            ps.setInt(5, fine.isPaid() ? 1 : 0);
            ps.setString(6, fine.getCalculationDate().toString());
            ps.setInt(7, fine.getFineId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating fine", e);
        }
    }

    @Override
    public void delete(int fineId) {
        String query = "DELETE FROM fines WHERE fine_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fineId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting fine", e);
        }
    }

    @Override
    public Fine findById(int fineId) {
        String query = "SELECT * FROM fines WHERE fine_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fineId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToFine(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding fine by id", e);
        }
        return null;
    }

    @Override
    public List<Fine> findByUserId(int userId) {
        String query = "SELECT * FROM fines WHERE user_id=?";
        return executeQuery(query, userId);
    }

    @Override
    public List<Fine> findByRecordId(int recordId) {
        String query = "SELECT * FROM fines WHERE record_id=?";
        return executeQuery(query, recordId);
    }

    @Override
    public List<Fine> findAll() {
        String query = "SELECT * FROM fines";
        List<Fine> fines = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                fines.add(mapResultSetToFine(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all fines", e);
        }
        return fines;
    }

    @Override
    public List<Fine> findUnpaidByUserId(int userId) {
        String query = "SELECT * FROM fines WHERE user_id=? AND is_paid=0";
        List<Fine> fines = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fines.add(mapResultSetToFine(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding unpaid fines", e);
        }
        return fines;
    }

    private List<Fine> executeQuery(String query, int userId) {
        List<Fine> fines = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fines.add(mapResultSetToFine(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
        return fines;
    }

    private Fine mapResultSetToFine(ResultSet rs) throws SQLException {
        Fine fine = new Fine();
        fine.setFineId(rs.getInt("fine_id"));
        fine.setUserId(rs.getInt("user_id"));
        fine.setRecordId(rs.getInt("record_id"));
        fine.setFineAmount(rs.getDouble("fine_amount"));
        fine.setDaysOverdue(rs.getInt("days_overdue"));
        fine.setPaid(rs.getInt("is_paid") == 1);
        fine.setCalculationDate(LocalDateTime.parse(rs.getString("calculation_date")));
        return fine;
    }
}
