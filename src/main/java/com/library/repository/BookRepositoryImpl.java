package com.library.repository;

import com.library.model.Book;
import java.sql.*;
import java.util.*;

public class BookRepositoryImpl implements BookRepository {
    private Connection connection;

    public BookRepositoryImpl() {
        this.connection = Database.getInstance().getConnection();
    }

    @Override
    public void save(Book book) {
        String query = "INSERT INTO books (title, author, isbn, quantity_total, quantity_available, daily_fine_rate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getQuantityTotal());
            ps.setInt(5, book.getQuantityAvailable());
            ps.setDouble(6, book.getDailyFineRate());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving book", e);
        }
    }

    @Override
    public void update(Book book) {
        String query = "UPDATE books SET title=?, author=?, isbn=?, quantity_total=?, quantity_available=?, daily_fine_rate=? WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getQuantityTotal());
            ps.setInt(5, book.getQuantityAvailable());
            ps.setDouble(6, book.getDailyFineRate());
            ps.setInt(7, book.getBookId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating book", e);
        }
    }

    @Override
    public void delete(int bookId) {
        String query = "DELETE FROM books WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book", e);
        }
    }

    @Override
    public Book findById(int bookId) {
        String query = "SELECT * FROM books WHERE book_id=?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding book by id", e);
        }
        return null;
    }

    @Override
    public List<Book> findByTitle(String title) {
        String query = "SELECT * FROM books WHERE title LIKE ?";
        return executeQuery(query, "%" + title + "%");
    }

    @Override
    public List<Book> findByAuthor(String author) {
        String query = "SELECT * FROM books WHERE author LIKE ?";
        return executeQuery(query, "%" + author + "%");
    }

    @Override
    public List<Book> findByISBN(String isbn) {
        String query = "SELECT * FROM books WHERE isbn=?";
        return executeQuery(query, isbn);
    }

    @Override
    public List<Book> findAll() {
        String query = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            return books;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all books", e);
        }
    }

    private List<Book> executeQuery(String query, String parameter) {
        List<Book> books = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, parameter);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
        return books;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setIsbn(rs.getString("isbn"));
        book.setQuantityTotal(rs.getInt("quantity_total"));
        book.setQuantityAvailable(rs.getInt("quantity_available"));
        book.setDailyFineRate(rs.getDouble("daily_fine_rate"));
        return book;
    }
}
