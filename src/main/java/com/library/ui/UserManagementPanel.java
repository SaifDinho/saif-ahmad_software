package com.library.ui;

import com.library.service.UserService;
import com.library.model.User;
import com.library.util.ValidationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private UserService userService;
    private JTable usersTable;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField memberIdField;
    private DefaultTableModel tableModel;

    public UserManagementPanel() {
        this.userService = new UserService();
        initializeUI();
        loadUsers();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        // Left: vertical form; Right: users table. Use JSplitPane for resizable columns.
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Register New User"));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel row;
        row = new JPanel(new BorderLayout(5, 5));
        row.add(new JLabel("Name:"), BorderLayout.WEST);
        nameField = new JTextField();
        row.add(nameField, BorderLayout.CENTER);
        formPanel.add(row);

        formPanel.add(Box.createVerticalStrut(6));

        row = new JPanel(new BorderLayout(5, 5));
        row.add(new JLabel("Email:"), BorderLayout.WEST);
        emailField = new JTextField();
        row.add(emailField, BorderLayout.CENTER);
        formPanel.add(row);

        formPanel.add(Box.createVerticalStrut(6));

        row = new JPanel(new BorderLayout(5, 5));
        row.add(new JLabel("Phone:"), BorderLayout.WEST);
        phoneField = new JTextField();
        row.add(phoneField, BorderLayout.CENTER);
        formPanel.add(row);

        formPanel.add(Box.createVerticalStrut(6));

        row = new JPanel(new BorderLayout(5, 5));
        row.add(new JLabel("Member ID:"), BorderLayout.WEST);
        memberIdField = new JTextField();
        row.add(memberIdField, BorderLayout.CENTER);
        formPanel.add(row);

        formPanel.add(Box.createVerticalStrut(10));

        JButton registerButton = new JButton("Register User");
        registerButton.addActionListener(new RegisterUserAction());
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnWrap.add(registerButton);
        formPanel.add(btnWrap);

        // Table on the right
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Member ID");
        tableModel.addColumn("Status");

        usersTable = new JTable(tableModel);
        usersTable.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(usersTable);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tableScroll);
        split.setResizeWeight(0.33);
        split.setOneTouchExpandable(true);
        add(split, BorderLayout.CENTER);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getMemberId(),
                    user.isActive() ? "Active" : "Inactive"
            });
        }
    }

    private class RegisterUserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String memberId = memberIdField.getText().trim();

                // Validate each field with specific error messages
                if (!ValidationUtil.isNotEmpty(name)) {
                    JOptionPane.showMessageDialog(UserManagementPanel.this, 
                        "Error: Name cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!ValidationUtil.isValidEmail(email)) {
                    StringBuilder dbg = new StringBuilder();
                    dbg.append("Email: [").append(email).append("]\n");
                    dbg.append("Length: ").append(email.length()).append("\n");
                    dbg.append("Codes: ");
                    for (int i = 0; i < email.length(); i++) {
                        dbg.append((int) email.charAt(i));
                        if (i < email.length() - 1) dbg.append(",");
                    }

                    JOptionPane.showMessageDialog(UserManagementPanel.this,
                        "Error: Invalid email format (e.g., user@example.com)\n\nDebug:\n" + dbg.toString(),
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!ValidationUtil.isValidPhone(phone)) {
                    JOptionPane.showMessageDialog(UserManagementPanel.this, 
                        "Error: Phone must be exactly 10 digits (no dashes or spaces)", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!ValidationUtil.isNotEmpty(memberId)) {
                    JOptionPane.showMessageDialog(UserManagementPanel.this, 
                        "Error: Member ID cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setMemberId(memberId);

                userService.registerUser(user);

                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                memberIdField.setText("");

                loadUsers();
                JOptionPane.showMessageDialog(UserManagementPanel.this, "User registered successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(UserManagementPanel.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
