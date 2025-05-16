package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;

public class TransactionManagementPanel extends JPanel {
    private final int userId;
    private final JTabbedPane tabbedPane;

    public TransactionManagementPanel(int userId) {
        this.userId = userId;
        setLayout(new GridBagLayout());
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tabbedPane.setPreferredSize(new Dimension(1000, 500));
        tabbedPane.addTab("Thu nhập", new IncomeManagementPanel(userId));
        tabbedPane.addTab("Khoản vay", new LoanManagementPanel(userId));
        tabbedPane.addTab("Tiết kiệm", new SavingManagementPanel(userId));
        tabbedPane.addTab("Đầu tư & Tích trữ", new InvestStoragePanel(userId));
        tabbedPane.addTab("Giao dịch", new TransactionPanel(userId));
        tabbedPane.addTab("Mục đích", new PurposePanel(userId));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(tabbedPane, gbc);

        JButton btnAdd = new JButton("Thêm giao dịch");
        btnAdd.setBackground(new Color(0x2E2E5D));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(0x2E2E5D));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
} 