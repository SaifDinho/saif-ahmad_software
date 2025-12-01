package com.library.repository;

import com.library.model.Payment;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PaymentRepositoryImpl implements PaymentRepository {
    private Connection connection;

    public PaymentRepositoryImpl() {
        this.connection = Database.getInstance().getConnection();
    }

    @Override
    public void save(Payment payment) {
        String query = "INSERT INTO payments (fine_id, amount, payment_date, payment_method) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, payment.getFineId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getPaymentDate().toString());
            ps.setString(4, payment.getPaymentMethod());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving payment", e);
        }
    }

    @Override
    public void delete(int paymentId) {
        String query = "DELETE FROM payments WHERE payment_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, paymentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting payment", e);
        }
    }

    @Override
    public Payment findById(int paymentId) {
        String query = "SELECT * FROM payments WHERE payment_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment by id", e);
        }
        return null;
    }

    @Override
    public List<Payment> findByFineId(int fineId) {
        String query = "SELECT * FROM payments WHERE fine_id=?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, fineId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payments by fine id", e);
        }
        return payments;
    }

    @Override
    public List<Payment> findAll() {
        String query = "SELECT * FROM payments";
        List<Payment> payments = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all payments", e);
        }
        return payments;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setFineId(rs.getInt("fine_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentDate(LocalDateTime.parse(rs.getString("payment_date")));
        payment.setPaymentMethod(rs.getString("payment_method"));
        return payment;
    }
}
