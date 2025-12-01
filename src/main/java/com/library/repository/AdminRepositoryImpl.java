package com.library.repository;

import com.library.model.Admin;
import java.sql.*;
import java.time.LocalDateTime;

public class AdminRepositoryImpl implements AdminRepository {
    private Connection connection;

    public AdminRepositoryImpl() {
        this.connection = Database.getInstance().getConnection();
    }

    @Override
    public Admin findByUsername(String username) {
        String query = "SELECT * FROM admins WHERE username=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToAdmin(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding admin by username", e);
        }
        return null;
    }

    @Override
    public void save(Admin admin) {
        String query = "INSERT INTO admins (username, password_hash, created_date) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPasswordHash());
            ps.setString(3, admin.getCreatedDate().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving admin", e);
        }
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(rs.getInt("admin_id"));
        admin.setUsername(rs.getString("username"));
        admin.setPasswordHash(rs.getString("password_hash"));
        admin.setCreatedDate(LocalDateTime.parse(rs.getString("created_date")));
        return admin;
    }
}
