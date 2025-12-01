package com.library.repository;

import com.library.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class UserRepositoryImpl implements UserRepository {
    private Connection connection;

    public UserRepositoryImpl() {
        this.connection = Database.getInstance().getConnection();
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO users (name, email, phone, member_id, registration_date, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getMemberId());
            ps.setString(5, user.getRegistrationDate().toString());
            ps.setInt(6, user.isActive() ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }

    @Override
    public void update(User user) {
        String query = "UPDATE users SET name=?, email=?, phone=?, member_id=?, registration_date=?, is_active=? WHERE user_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getMemberId());
            ps.setString(5, user.getRegistrationDate().toString());
            ps.setInt(6, user.isActive() ? 1 : 0);
            ps.setInt(7, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public void delete(int userId) {
        String query = "DELETE FROM users WHERE user_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public User findById(int userId) {
        String query = "SELECT * FROM users WHERE user_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id", e);
        }
        return null;
    }

    @Override
    public User findByMemberId(String memberId) {
        String query = "SELECT * FROM users WHERE member_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by member id", e);
        }
        return null;
    }

    @Override
    public List<User> findByName(String name) {
        String query = "SELECT * FROM users WHERE name LIKE ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding users by name", e);
        }
        return users;
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        }
        return users;
    }

    @Override
    public List<User> findActive() {
        String query = "SELECT * FROM users WHERE is_active=1";
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding active users", e);
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setMemberId(rs.getString("member_id"));
        user.setRegistrationDate(LocalDateTime.parse(rs.getString("registration_date")));
        user.setActive(rs.getInt("is_active") == 1);
        return user;
    }
}
