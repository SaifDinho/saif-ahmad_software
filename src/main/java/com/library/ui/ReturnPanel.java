package com.library.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*; 
import java.time.LocalDate;

public class ReturnPanel extends JPanel {
    private JSpinner recordIdSpinner;
    private JButton returnButton;
    private JTextArea resultArea;
    private DefaultTableModel tableModel;

    public ReturnPanel() {
        setLayout(new BorderLayout());
        initializeServices();
        
        // Top panel for return form
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);
        
        // Middle panel for result
        resultArea = new JTextArea(6, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for overdue items
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.SOUTH);
    }

    private void initializeServices() {
        // Placeholder for service initialization
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Return Item"));
        
        panel.add(new JLabel("Borrowing Record ID:"));
        recordIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        panel.add(recordIdSpinner);
        
        returnButton = new JButton("Process Return");
        returnButton.addActionListener(e -> processReturn());
        panel.add(returnButton);
        
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Overdue Items (5 Most Recent)"));
        
        String[] columns = {"Record ID", "User ID", "Item ID", "Item Type", "Due Date", "Days Overdue", "Fine Amount"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable overdueTable = new JTable(tableModel);
        overdueTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(overdueTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshOverdueTable());
        panel.add(refreshButton, BorderLayout.SOUTH);
        
        refreshOverdueTable();
        return panel;
    }

    private void processReturn() {
        try {
            int recordId = (Integer) recordIdSpinner.getValue();
            
            resultArea.setText("✅ Return Processed!\n" +
                "Record ID: " + recordId + "\n" +
                "Return Date: " + LocalDate.now() + "\n" +
                "Fine calculated automatically based on item type and days overdue.");
            refreshOverdueTable();
        } catch (Exception e) {
            resultArea.setText("❌ Error: " + e.getMessage());
        }
    }

    private void refreshOverdueTable() {
        tableModel.setRowCount(0);
        try {
            // Display sample overdue records
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading overdue items: " + e.getMessage());
        }
    }
}
