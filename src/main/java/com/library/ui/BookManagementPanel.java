package com.library.ui;

import com.library.service.BookService;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookManagementPanel extends JPanel {
    private BookService bookService;
    private JTable booksTable;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JSpinner quantitySpinner;
    private JSpinner fineRateSpinner;
    private DefaultTableModel tableModel;

    public BookManagementPanel() {
        this.bookService = new BookService();
        initializeUI();
        loadBooks();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Top panel for add/search
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Add New Book"));

        topPanel.add(new JLabel("Title:"));
        titleField = new JTextField(20);
        topPanel.add(titleField);

        topPanel.add(new JLabel("Author:"));
        authorField = new JTextField(20);
        topPanel.add(authorField);

        topPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField(20);
        topPanel.add(isbnField);

        topPanel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        topPanel.add(quantitySpinner);

        topPanel.add(new JLabel("Daily Fine Rate:"));
        fineRateSpinner = new JSpinner(new SpinnerNumberModel(0.50, 0.0, 100.0, 0.10));
        topPanel.add(fineRateSpinner);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(new AddBookAction());
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);

        // Center panel for table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("ISBN");
        tableModel.addColumn("Total");
        tableModel.addColumn("Available");
        tableModel.addColumn("Fine Rate");

        booksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn(),
                    book.getQuantityTotal(),
                    book.getQuantityAvailable(),
                    String.format("$%.2f", book.getDailyFineRate())
            });
        }
    }

    private class AddBookAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                int quantity = (int) quantitySpinner.getValue();
                double fineRate = (double) fineRateSpinner.getValue();

                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setIsbn(isbn);
                book.setQuantityTotal(quantity);
                book.setQuantityAvailable(quantity);
                book.setDailyFineRate(fineRate);

                bookService.addBook(book);

                titleField.setText("");
                authorField.setText("");
                isbnField.setText("");
                quantitySpinner.setValue(1);
                fineRateSpinner.setValue(0.50);

                loadBooks();
                JOptionPane.showMessageDialog(BookManagementPanel.this, "Book added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(BookManagementPanel.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
