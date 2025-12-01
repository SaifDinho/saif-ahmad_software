package com.library.repository;

import com.library.model.Book;
import java.util.List;

public interface BookRepository {
    void save(Book book);
    void update(Book book);
    void delete(int bookId);
    Book findById(int bookId);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByISBN(String isbn);
    List<Book> findAll();
}
