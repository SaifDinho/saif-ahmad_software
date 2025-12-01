package com.library.ui;

import com.library.service.ReportService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportsPanel extends JPanel {
    private ReportService reportService;
    private JTextArea reportArea;

    public ReportsPanel() {
        this.reportService = new ReportService();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        JButton overdueButton = new JButton("Overdue Items Report");
        JButton activeBorrowingsButton = new JButton("Active Borrowings Report");

        overdueButton.addActionListener(new GenerateOverdueReportAction());
        activeBorrowingsButton.addActionListener(new GenerateActiveBorrowingsReportAction());

        buttonPanel.add(overdueButton);
        buttonPanel.add(activeBorrowingsButton);

        add(buttonPanel, BorderLayout.NORTH);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private class GenerateOverdueReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String report = reportService.generateMixedMediaOverdueReport();
            reportArea.setText(report);
        }
    }

    private class GenerateActiveBorrowingsReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String report = reportService.generateActiveBorrowingsReport();
            reportArea.setText(report);
        }
    }
}
