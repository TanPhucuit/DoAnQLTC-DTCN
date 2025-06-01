package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final int userId;

    public DashboardPanel(int userId) {
        this.userId = userId;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Tổng quan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Income Card
        JPanel incomeCard = createCard("Thu nhập", "0 VND", "Tổng thu nhập trong tháng");
        contentPanel.add(incomeCard);

        // Expense Card
        JPanel expenseCard = createCard("Chi tiêu", "0 VND", "Tổng chi tiêu trong tháng");
        contentPanel.add(expenseCard);

        // Loan Card
        JPanel loanCard = createCard("Khoản vay", "0 VND", "Tổng số tiền đang vay");
        contentPanel.add(loanCard);

        // Save Card
        JPanel saveCard = createCard("Tiết kiệm", "0 VND", "Tổng số tiền đã tiết kiệm");
        contentPanel.add(saveCard);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String amount, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));

        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        amountLabel.setForeground(new Color(0, 120, 212));
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(amountLabel);
        card.add(Box.createVerticalStrut(10));

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionLabel.setForeground(Color.GRAY);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(descriptionLabel);

        return card;
    }
} 