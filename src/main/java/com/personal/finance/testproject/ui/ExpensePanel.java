package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpensePanel extends JPanel {
    private final int userId;
    private JTable expenseTable;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> categoryComboBox;

    public ExpensePanel(int userId) {
        this.userId = userId;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Quản lý chi tiêu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Amount
        JLabel amountLabel = new JLabel("Số tiền:");
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(amountLabel);
        formPanel.add(Box.createVerticalStrut(5));

        amountField = new JTextField(20);
        amountField.setMaximumSize(new Dimension(300, 30));
        amountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(amountField);
        formPanel.add(Box.createVerticalStrut(20));

        // Description
        JLabel descriptionLabel = new JLabel("Mô tả:");
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descriptionLabel);
        formPanel.add(Box.createVerticalStrut(5));

        descriptionField = new JTextField(20);
        descriptionField.setMaximumSize(new Dimension(300, 30));
        descriptionField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(descriptionField);
        formPanel.add(Box.createVerticalStrut(20));

        // Category
        JLabel categoryLabel = new JLabel("Danh mục:");
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(categoryLabel);
        formPanel.add(Box.createVerticalStrut(5));

        String[] categories = {"Ăn uống", "Di chuyển", "Mua sắm", "Giải trí", "Hóa đơn", "Khác"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setMaximumSize(new Dimension(300, 30));
        categoryComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(categoryComboBox);
        formPanel.add(Box.createVerticalStrut(30));

        // Add Button
        JButton addButton = new JButton("Thêm chi tiêu");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setBackground(new Color(0x2E2E5D));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        formPanel.add(addButton);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnNames = {"Ngày", "Số tiền", "Mô tả", "Danh mục", "Thao tác"};
        Object[][] data = {}; // TODO: Load data from database
        expenseTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Split Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tablePanel);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
    }

    private void addExpense() {
        String amount = amountField.getText();
        String description = descriptionField.getText();
        String category = (String) categoryComboBox.getSelectedItem();

        if (amount.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng điền đầy đủ thông tin",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double amountValue = Double.parseDouble(amount);
            // TODO: Save to database
            JOptionPane.showMessageDialog(this,
                "Thêm chi tiêu thành công",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            // TODO: Refresh table
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Số tiền không hợp lệ",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        amountField.setText("");
        descriptionField.setText("");
        categoryComboBox.setSelectedIndex(0);
    }
} 