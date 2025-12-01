package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.repository.BookRepositoryImpl;
import java.util.List;

public class BookService {
    private BookRepository bookRepository;

    public BookService() {
        this.bookRepository = new BookRepositoryImpl();
    }

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }
        if (book.getQuantityTotal() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (book.getDailyFineRate() < 0) {
            throw new IllegalArgumentException("Daily fine rate cannot be negative");
        }

        bookRepository.save(book);
    }

    public void updateBook(Book book) {
        if (book == null || book.getBookId() <= 0) {
            throw new IllegalArgumentException("Invalid book");
        }
        bookRepository.update(book);
    }

    public void deleteBook(int bookId) {
        bookRepository.delete(bookId);
    }

    public Book getBook(int bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByTitle(title);
    }

    public List<Book> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByAuthor(author);
    }

    public List<Book> searchByISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return bookRepository.findAll();
        }
        return bookRepository.findByISBN(isbn);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
