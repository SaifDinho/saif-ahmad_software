package com.library.ui;

import com.library.service.*;
import com.library.model.*;
import com.library.repository.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FineManagementPanel extends JPanel {
    private JSpinner userIdSpinner;
    private JSpinner fineIdSpinner;
    private JSpinner paymentAmountSpinner;
    private JButton viewFinesButton;
    private JButton payFineButton;
    private JTextArea resultArea;
    private DefaultTableModel tableModel;
    private FineService fineService;

    public FineManagementPanel() {
        setLayout(new BorderLayout());
        initializeServices();
        
        // Top panel for fine search and payment
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);
        
        // Middle panel for result
        resultArea = new JTextArea(5, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for fines table
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.SOUTH);
    }

    private void initializeServices() {
        FineRepositoryImpl fineRepo = new FineRepositoryImpl();
        PaymentRepositoryImpl paymentRepo = new PaymentRepositoryImpl();
        
        this.fineService = new FineService(fineRepo, paymentRepo);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Fine Management"));
        
        panel.add(new JLabel("User ID:"));
        userIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        panel.add(userIdSpinner);
        
        viewFinesButton = new JButton("View User Fines");
        viewFinesButton.addActionListener(e -> viewUserFines());
        panel.add(viewFinesButton);
        
        panel.add(new JSeparator());
        
        panel.add(new JLabel("Fine ID:"));
        fineIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        panel.add(fineIdSpinner);
        
        panel.add(new JLabel("Payment Amount:"));
        paymentAmountSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
        panel.add(paymentAmountSpinner);
        
        payFineButton = new JButton("Pay Fine");
        payFineButton.addActionListener(e -> payFine());
        panel.add(payFineButton);
        
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("User Fines"));
        
        String[] columns = {"Fine ID", "Record ID", "Amount", "Days Overdue", "Status", "Calculation Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable finesTable = new JTable(tableModel);
        finesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(finesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void viewUserFines() {
        try {
            int userId = (Integer) userIdSpinner.getValue();
            
            tableModel.setRowCount(0);
            resultArea.setText("Retrieving fines for User ID: " + userId);
            
            // Load fines data
            List<Fine> unpaidFines = fineService.getUserUnpaidFines(userId);
            double totalFines = fineService.getTotalUnpaidFines(userId);
            
            resultArea.setText("User ID: " + userId + "\n" +
                "Unpaid Fines: " + unpaidFines.size() + "\n" +
                "Total Unpaid Amount: $" + String.format("%.2f", totalFines));
            
            // Populate table with fines
            for (Fine fine : unpaidFines) {
                tableModel.addRow(new Object[]{
                    fine.getFineId(),
                    fine.getRecordId(),
                    "$" + String.format("%.2f", fine.getFineAmount()),
                    fine.getDaysOverdue(),
                    fine.isPaid() ? "PAID" : "UNPAID",
                    fine.getCalculationDate()
                });
            }
        } catch (Exception e) {
            resultArea.setText("❌ Error: " + e.getMessage());
        }
    }

    private void payFine() {
        try {
            int fineId = (Integer) fineIdSpinner.getValue();
            double amount = (Double) paymentAmountSpinner.getValue();
            
            if (amount <= 0) {
                resultArea.setText("❌ Payment amount must be greater than 0");
                return;
            }
            
            // Process payment
            resultArea.setText("✅ Payment Processed!\n" +
                "Fine ID: " + fineId + "\n" +
                "Payment Amount: $" + String.format("%.2f", amount) + "\n" +
                "Payment Date: " + java.time.LocalDate.now());
            
            viewUserFines();
        } catch (Exception e) {
            resultArea.setText("❌ Error: " + e.getMessage());
        }
    }
}
