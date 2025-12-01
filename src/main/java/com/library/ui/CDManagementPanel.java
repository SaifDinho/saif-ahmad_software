package com.library.ui;

import com.library.service.CDService;
import com.library.model.CD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CDManagementPanel extends JPanel {
    private CDService cdService;
    private JTable cdsTable;
    private JTextField titleField;
    private JTextField artistField;
    private JTextField catalogField;
    private JSpinner quantitySpinner;
    private JSpinner fineRateSpinner;
    private DefaultTableModel tableModel;

    public CDManagementPanel() {
        this.cdService = new CDService();
        initializeUI();
        loadCDs();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Add New CD"));

        topPanel.add(new JLabel("Title:"));
        titleField = new JTextField(20);
        topPanel.add(titleField);

        topPanel.add(new JLabel("Artist:"));
        artistField = new JTextField(20);
        topPanel.add(artistField);

        topPanel.add(new JLabel("Catalog #:"));
        catalogField = new JTextField(20);
        topPanel.add(catalogField);

        topPanel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        topPanel.add(quantitySpinner);

        topPanel.add(new JLabel("Daily Fine Rate:"));
        fineRateSpinner = new JSpinner(new SpinnerNumberModel(1.00, 0.0, 100.0, 0.10));
        topPanel.add(fineRateSpinner);

        JButton addButton = new JButton("Add CD");
        addButton.addActionListener(new AddCDAction());
        topPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Artist");
        tableModel.addColumn("Catalog #");
        tableModel.addColumn("Total");
        tableModel.addColumn("Available");
        tableModel.addColumn("Fine Rate");

        cdsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cdsTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadCDs() {
        tableModel.setRowCount(0);
        List<CD> cds = cdService.getAllCDs();
        for (CD cd : cds) {
            tableModel.addRow(new Object[]{
                    cd.getCdId(),
                    cd.getTitle(),
                    cd.getArtist(),
                    cd.getCatalogNumber(),
                    cd.getQuantityTotal(),
                    cd.getQuantityAvailable(),
                    String.format("$%.2f", cd.getDailyFineRate())
            });
        }
    }

    private class AddCDAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String title = titleField.getText();
                String artist = artistField.getText();
                String catalogNumber = catalogField.getText();
                int quantity = (int) quantitySpinner.getValue();
                double fineRate = (double) fineRateSpinner.getValue();

                CD cd = new CD();
                cd.setTitle(title);
                cd.setArtist(artist);
                cd.setCatalogNumber(catalogNumber);
                cd.setQuantityTotal(quantity);
                cd.setQuantityAvailable(quantity);
                cd.setDailyFineRate(fineRate);

                cdService.addCD(cd);

                titleField.setText("");
                artistField.setText("");
                catalogField.setText("");
                quantitySpinner.setValue(1);
                fineRateSpinner.setValue(1.00);

                loadCDs();
                JOptionPane.showMessageDialog(CDManagementPanel.this, "CD added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CDManagementPanel.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
