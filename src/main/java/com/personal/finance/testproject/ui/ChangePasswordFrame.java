package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.service.AuthService;
import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {
    private final AuthService authService;
    private final String username;
    private final JPasswordField oldPasswordField;
    private final JPasswordField newPasswordField;
    private final JPasswordField confirmPasswordField;
    private final JLabel messageLabel;
    private Runnable changePasswordListener;

    public ChangePasswordFrame(AuthService authService, String username) {
        this.authService = authService;
        this.username = username;
        
        setTitle("Quản lý tài chính & đầu tư cá nhân - Đổi mật khẩu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Đổi mật khẩu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Old password field
        oldPasswordField = new JPasswordField(20);
        oldPasswordField.setMaximumSize(new Dimension(300, 40));
        addFieldWithLabel(mainPanel, "Mật khẩu cũ:", oldPasswordField);

        // New password field
        newPasswordField = new JPasswordField(20);
        newPasswordField.setMaximumSize(new Dimension(300, 40));
        addFieldWithLabel(mainPanel, "Mật khẩu mới:", newPasswordField);

        // Confirm password field
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setMaximumSize(new Dimension(300, 40));
        addFieldWithLabel(mainPanel, "Xác nhận mật khẩu mới:", confirmPasswordField);

        // Message label
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        // Change password button
        JButton changeButton = new JButton("Đổi mật khẩu");
        changeButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        changeButton.setBackground(new Color(0x2E2E5D));
        changeButton.setForeground(Color.WHITE);
        changeButton.setFocusPainted(false);
        changeButton.setBorderPainted(false);
        changeButton.setPreferredSize(new Dimension(150, 40));
        changeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Back button
        JButton backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        backButton.setBackground(new Color(0x2E2E5D));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(changeButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel);

        // Add action listeners
        changeButton.addActionListener(e -> {
            // Clear previous message
            messageLabel.setText("");
            messageLabel.setForeground(Color.RED);

            // Get input values
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Validate input
            if (oldPassword.isEmpty()) {
                messageLabel.setText("Vui lòng nhập mật khẩu cũ");
                oldPasswordField.requestFocus();
                return;
            }
            if (newPassword.isEmpty()) {
                messageLabel.setText("Vui lòng nhập mật khẩu mới");
                newPasswordField.requestFocus();
                return;
            }
            if (confirmPassword.isEmpty()) {
                messageLabel.setText("Vui lòng xác nhận mật khẩu mới");
                confirmPasswordField.requestFocus();
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                messageLabel.setText("Mật khẩu mới không khớp");
                confirmPasswordField.setText("");
                confirmPasswordField.requestFocus();
                return;
            }

            // Disable button and show processing state
            changeButton.setEnabled(false);
            changeButton.setText("Đang xử lý...");
            changeButton.setBackground(new Color(108, 117, 125));

            // Process change password in background
            SwingUtilities.invokeLater(() -> {
                try {
                    authService.changePassword(username, oldPassword, newPassword);
                    if (changePasswordListener != null) {
                        changePasswordListener.run();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Đổi mật khẩu thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        SwingUtilities.invokeLater(() -> {
                            LoginFrame loginFrame = new LoginFrame();
                            loginFrame.setVisible(true);
                        });
                    }
                } catch (IllegalArgumentException ex) {
                    messageLabel.setText(ex.getMessage());
                    if (ex.getMessage().contains("mật khẩu cũ")) {
                        oldPasswordField.setText("");
                        oldPasswordField.requestFocus();
                    } else {
                        newPasswordField.setText("");
                        confirmPasswordField.setText("");
                        newPasswordField.requestFocus();
                    }
                } catch (Exception ex) {
                    messageLabel.setText("Lỗi đổi mật khẩu: " + ex.getMessage());
                } finally {
                    // Re-enable button
                    changeButton.setEnabled(true);
                    changeButton.setText("Đổi mật khẩu");
                    changeButton.setBackground(new Color(0, 123, 255));
                }
            });
        });

        backButton.addActionListener(e -> {
            if (changePasswordListener != null) {
                changePasswordListener.run();
            } else {
                dispose();
                SwingUtilities.invokeLater(() -> {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                });
            }
        });

        add(mainPanel);
    }

    private void addFieldWithLabel(JPanel panel, String labelText, JPasswordField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));

        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
    }

    public void setChangePasswordListener(Runnable listener) {
        this.changePasswordListener = listener;
    }

    // For backward compatibility
    public ChangePasswordFrame(AuthService authService) {
        this(authService, null);
    }
} 