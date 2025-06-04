package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;

public class TransactionManagementPanel extends JPanel {
    private final int userId;
    private ManageSearchFrame parentFrame;

    public TransactionManagementPanel(int userId, ManageSearchFrame parentFrame) {
        this.userId = userId;
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(0xF5F5F5));
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0xF5F5F5));
        add(mainPanel, BorderLayout.CENTER);

        String[] titles = {"Thu nhập", "Khoản vay", "Tiết kiệm", "Đầu tư & Tích trữ", "Giao dịch", "Loại giao dịch", "Mục đích"};
        String[] iconFiles = {"income.png", "loan.png", "save.png", "invest.png", "transaction.png", "transaction type.png", "purpose.png"};
        JPanel[] functionPanels = {
            new IncomeManagementPanel(userId, parentFrame),
            new LoanManagementPanel(userId, parentFrame),
            new SavingManagementPanel(userId, parentFrame),
            new InvestStoragePanel(userId, parentFrame),
            new TransactionPanel(userId,parentFrame),
            new TransactionTypePanel(userId, parentFrame),
           new PurposePanel(userId, parentFrame)
        };

        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 32, 32));
        gridPanel.setBackground(new Color(0xF5F5F5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(gridPanel, gbc);

        // Nút quay lại
        JButton btnBack = new JButton("← Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(new Color(0x008BCF));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.showDashboard();
            }
        });
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(0xF5F5F5));
        backPanel.add(btnBack);
        add(backPanel, BorderLayout.NORTH);

        // Hiển thị dashboard (các ô chức năng)
        Runnable showDashboard = () -> {
            removeAll();
            add(mainPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        };
        btnBack.addActionListener(e -> showDashboard.run());

        for (int i = 0; i < titles.length; i++) {
            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.setBackground(Color.WHITE);
            box.setPreferredSize(new Dimension(160, 160));
            box.setMaximumSize(new Dimension(160, 160));
            box.setMinimumSize(new Dimension(120, 120));
            box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE0E0E0), 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
            box.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Icon
            JLabel iconLabel = new JLabel();
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon2/" + iconFiles[i]));
            Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
            // Text
            JLabel textLabel = new JLabel(titles[i]);
            textLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            textLabel.setForeground(Color.BLACK);
            textLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            box.add(iconLabel);
            box.add(textLabel);
            int idx = i;
            box.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    removeAll();
                    add(functionPanels[idx], BorderLayout.CENTER);
                    revalidate();
                    repaint();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    box.setBackground(new Color(0xE0F7FA));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    box.setBackground(Color.WHITE);
                }
            });
            gridPanel.add(box);
        }
    }
} 