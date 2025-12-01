package com.library.ui;

import com.library.service.*;
import com.library.model.*;
import com.library.repository.*;
import com.library.util.TimeProvider;
import com.library.util.SystemTimeProvider;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BorrowingPanel extends JPanel {
    private JComboBox<String> itemTypeCombo;
    private JSpinner itemIdSpinner;
    private JSpinner userIdSpinner;
    private JButton borrowButton;
    private JTextArea resultArea;
    private JTable borrowingTable;
    private DefaultTableModel tableModel;
    private BorrowingService borrowingService;

    public BorrowingPanel() {
        setLayout(new BorderLayout());
        initializeServices();
        
        // Top panel for borrowing form
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);
        
        // Middle panel for result
        resultArea = new JTextArea(5, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for active borrowings
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.SOUTH);
    }

    private void initializeServices() {
        BorrowingRecordRepositoryImpl borrowingRepo = new BorrowingRecordRepositoryImpl();
        BookRepositoryImpl bookRepo = new BookRepositoryImpl();
        CDRepositoryImpl cdRepo = new CDRepositoryImpl();
        FineRepositoryImpl fineRepo = new FineRepositoryImpl();
        UserRepositoryImpl userRepo = new UserRepositoryImpl();
        
        TimeProvider timeProvider = new SystemTimeProvider();
        
        this.borrowingService = new BorrowingService(borrowingRepo, bookRepo, cdRepo, fineRepo, userRepo, timeProvider);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Borrow Item"));
        
        panel.add(new JLabel("Item Type:"));
        itemTypeCombo = new JComboBox<>(new String[]{"BOOK", "CD"});
        panel.add(itemTypeCombo);
        
        panel.add(new JLabel("Item ID:"));
        itemIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        panel.add(itemIdSpinner);
        
        panel.add(new JLabel("User ID:"));
        userIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        panel.add(userIdSpinner);
        
        borrowButton = new JButton("Borrow");
        borrowButton.addActionListener(e -> borrowItem());
        panel.add(borrowButton);
        
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Active Borrowings"));
        
        String[] columns = {"Record ID", "User ID", "Item ID", "Item Type", "Borrow Date", "Due Date", "Days Remaining"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        borrowingTable = new JTable(tableModel);
        borrowingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(borrowingTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshBorrowingTable());
        panel.add(refreshButton, BorderLayout.SOUTH);
        
        refreshBorrowingTable();
        return panel;
    }

    private void borrowItem() {
        try {
            int itemId = (Integer) itemIdSpinner.getValue();
            int userId = (Integer) userIdSpinner.getValue();
            String itemType = (String) itemTypeCombo.getSelectedItem();
            
            BorrowingRecord record = null;
            if ("BOOK".equals(itemType)) {
                record = borrowingService.borrowBook(userId, itemId);
            } else if ("CD".equals(itemType)) {
                record = borrowingService.borrowCD(userId, itemId);
            }
            
            if (record != null) {
                resultArea.setText("✅ Successfully borrowed!\n" +
                    "Record ID: " + record.getRecordId() + "\n" +
                    "Item Type: " + record.getItemType() + "\n" +
                    "Borrow Date: " + record.getBorrowDate() + "\n" +
                    "Due Date: " + record.getDueDate());
                refreshBorrowingTable();
            }
        } catch (Exception e) {
            resultArea.setText("❌ Error: " + e.getMessage());
        }
    }

    private void refreshBorrowingTable() {
        tableModel.setRowCount(0);
        try {
            // Refresh active borrowings
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading borrowings: " + e.getMessage());
        }
    }
}
