package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SidebarPanel extends JPanel {
    private final Map<String, JButton> menuButtons = new HashMap<>();
    private SidebarListener sidebarListener;
    private static final int ICON_SIZE = 24;
    private static final int ICON_GAP = 16;
    private static final int BUTTON_PADDING = 10;

    public interface SidebarListener {
        void onMenuSelected(String menuKey);
    }

    // Phương thức tiện ích để load icon
    private ImageIcon createImageIcon(String path) {
        try {
            java.net.URL imgURL = getClass().getResource("/" + path);
            if (imgURL == null) {
                System.err.println("Couldn't find icon: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Error loading icon " + path + ": " + e.getMessage());
            return null;
        }
    }

    public SidebarPanel() {
        System.out.println("DEBUG ICON PATH: " + getClass().getResource("/icons/dashboard.png"));
        setLayout(new GridLayout(0, 1, 0, 10));
        setBackground(new Color(245, 247, 250));
        setPreferredSize(new Dimension(200, 0));

        addMenuButton("dashboard", "Dashboard", createImageIcon("icons/dashboard.png"));
        addMenuButton("transaction", "Giao dịch", createImageIcon("icons/transaction.png"));
        addMenuButton("income", "Thu nhập", createImageIcon("icons/income.png"));
        addMenuButton("saving", "Tiết kiệm", createImageIcon("icons/saving.png"));
        addMenuButton("investment", "Đầu tư", createImageIcon("icons/invest&storage.png"));
        addMenuButton("loan", "Khoản vay", createImageIcon("icons/loan.png"));
        addMenuButton("report", "Báo cáo", createImageIcon("icons/report.png"));
        addMenuButton("bank", "Tài khoản ngân hàng", createImageIcon("icons/bank_account.png"));
        addMenuButton("logout", "Đăng xuất", createImageIcon("icons/logout.png"));
    }

    private void addMenuButton(String key, String text, ImageIcon icon) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(245, 247, 250));
        button.setForeground(new Color(44, 62, 80));
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(ICON_GAP);
        button.setBorder(BorderFactory.createEmptyBorder(BUTTON_PADDING, 20, BUTTON_PADDING, BUTTON_PADDING));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (icon != null) {
            button.setIcon(icon);
        }

        button.addActionListener(e -> {
            if (sidebarListener != null) sidebarListener.onMenuSelected(key);
        });
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 230, 250));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 247, 250));
            }
        });
        add(button);
        menuButtons.put(key, button);
    }

    public void setSidebarListener(SidebarListener listener) {
        this.sidebarListener = listener;
    }

    public void setActiveMenu(String key) {
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            if (entry.getKey().equals(key)) {
                entry.getValue().setBackground(new Color(200, 220, 255));
            } else {
                entry.getValue().setBackground(new Color(245, 247, 250));
            }
        }
    }
} 