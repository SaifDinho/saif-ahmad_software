package com.library.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardWindow extends JFrame {
    private JTabbedPane tabbedPane;

    public DashboardWindow() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Library Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Add tabs
        tabbedPane.addTab("Books", new BookManagementPanel());
        tabbedPane.addTab("CDs", new CDManagementPanel());
        tabbedPane.addTab("Users", new UserManagementPanel());
        tabbedPane.addTab("Borrowing", new BorrowingPanel());
        tabbedPane.addTab("Returns", new ReturnPanel());
        tabbedPane.addTab("Fines", new FineManagementPanel());
        tabbedPane.addTab("Reports", new ReportsPanel());

        // Logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new LogoutAction());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(logoutButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        topPanel.add(tabbedPane, BorderLayout.CENTER);

        add(topPanel);
    }

    private class LogoutAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new LoginWindow().setVisible(true);
        }
    }
}
