package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private final JButton menuButton;
    private final JLabel titleLabel;
    private final JLabel userLabel;
    private final JButton settingsButton;
    private static final Color HEADER_BG = new Color(0x000000); // Đen tuyệt đối
    private static final Color TEXT_COLOR = Color.WHITE; // #FFFFFF

    // Hàm tiện ích để load icon an toàn
    private ImageIcon loadIcon(String path, String fallback) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            // Nếu không tìm thấy, thử icon fallback
            if (fallback != null) {
                java.net.URL fallbackURL = getClass().getClassLoader().getResource(fallback);
                if (fallbackURL != null) {
                    return new ImageIcon(fallbackURL);
                }
            }
            // Nếu vẫn không có, trả về null hoặc icon rỗng
            System.err.println("Không tìm thấy icon: " + path + ", dùng tạm icon rỗng.");
            return new ImageIcon();
        }
    }

    public HeaderPanel(String username) {
        setLayout(new BorderLayout());
        setBackground(HEADER_BG);
        setPreferredSize(new Dimension(0, 60));

        // Menu button
        menuButton = new JButton("☰");
        menuButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        menuButton.setForeground(TEXT_COLOR);
        menuButton.setBackground(HEADER_BG);
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Title
        titleLabel = new JLabel("Quản lý tài chính & đầu tư cá nhân");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // User info
        userLabel = new JLabel(username);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userLabel.setForeground(TEXT_COLOR);

        // Settings button
        settingsButton = new JButton("⚙");
        settingsButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        settingsButton.setForeground(TEXT_COLOR);
        settingsButton.setBackground(HEADER_BG);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusPainted(false);
        settingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Layout
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(HEADER_BG);
        leftPanel.add(menuButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(HEADER_BG);
        rightPanel.add(userLabel);
        rightPanel.add(settingsButton);

        add(leftPanel, BorderLayout.WEST);
        add(titleLabel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }
} 