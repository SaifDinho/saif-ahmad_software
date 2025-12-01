package com.library.repository;

import com.library.model.CD;
import java.sql.*;
import java.util.*;

public class CDRepositoryImpl implements CDRepository {
    private Connection connection;

    public CDRepositoryImpl() {
        this.connection = Database.getInstance().getConnection();
    }

    @Override
    public void save(CD cd) {
        String query = "INSERT INTO cds (title, artist, catalog_number, quantity_total, quantity_available, daily_fine_rate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cd.getTitle());
            ps.setString(2, cd.getArtist());
            ps.setString(3, cd.getCatalogNumber());
            ps.setInt(4, cd.getQuantityTotal());
            ps.setInt(5, cd.getQuantityAvailable());
            ps.setDouble(6, cd.getDailyFineRate());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving CD", e);
        }
    }

    @Override
    public void update(CD cd) {
        String query = "UPDATE cds SET title=?, artist=?, catalog_number=?, quantity_total=?, quantity_available=?, daily_fine_rate=? WHERE cd_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cd.getTitle());
            ps.setString(2, cd.getArtist());
            ps.setString(3, cd.getCatalogNumber());
            ps.setInt(4, cd.getQuantityTotal());
            ps.setInt(5, cd.getQuantityAvailable());
            ps.setDouble(6, cd.getDailyFineRate());
            ps.setInt(7, cd.getCdId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating CD", e);
        }
    }

    @Override
    public void delete(int cdId) {
        String query = "DELETE FROM cds WHERE cd_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, cdId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting CD", e);
        }
    }

    @Override
    public CD findById(int cdId) {
        String query = "SELECT * FROM cds WHERE cd_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, cdId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToCD(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding CD by id", e);
        }
        return null;
    }

    @Override
    public List<CD> findByTitle(String title) {
        String query = "SELECT * FROM cds WHERE title LIKE ?";
        return executeQuery(query, "%" + title + "%");
    }

    @Override
    public List<CD> findByArtist(String artist) {
        String query = "SELECT * FROM cds WHERE artist LIKE ?";
        return executeQuery(query, "%" + artist + "%");
    }

    @Override
    public List<CD> findAll() {
        String query = "SELECT * FROM cds";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            List<CD> cds = new ArrayList<>();
            while (rs.next()) {
                cds.add(mapResultSetToCD(rs));
            }
            return cds;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all CDs", e);
        }
    }

    private List<CD> executeQuery(String query, String parameter) {
        List<CD> cds = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, parameter);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cds.add(mapResultSetToCD(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
        return cds;
    }

    private CD mapResultSetToCD(ResultSet rs) throws SQLException {
        CD cd = new CD();
        cd.setCdId(rs.getInt("cd_id"));
        cd.setTitle(rs.getString("title"));
        cd.setArtist(rs.getString("artist"));
        cd.setCatalogNumber(rs.getString("catalog_number"));
        cd.setQuantityTotal(rs.getInt("quantity_total"));
        cd.setQuantityAvailable(rs.getInt("quantity_available"));
        cd.setDailyFineRate(rs.getDouble("daily_fine_rate"));
        return cd;
    }
}
