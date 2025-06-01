package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.service.AuthService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChangePasswordPanel extends JPanel {
    private final AuthService authService;
    private final int userId;
    private final JPasswordField oldPasswordField;
    private final JPasswordField newPasswordField;
    private final JPasswordField confirmPasswordField;
    private final JLabel messageLabel;
    private ChangePasswordListener changePasswordListener;

    public interface ChangePasswordListener {
        void onChangePasswordSuccess();
        void onBackToLogin();
    }

    public ChangePasswordPanel(AuthService authService, int userId) {
        this.authService = authService;
        this.userId = userId;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel chính chứa form đổi mật khẩu
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Old Password
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Mật khẩu cũ:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        oldPasswordField = new JPasswordField(20);
        formPanel.add(oldPasswordField, gbc);

        // New Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Mật khẩu mới:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        newPasswordField = new JPasswordField(20);
        formPanel.add(newPasswordField, gbc);

        // Confirm New Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Xác nhận mật khẩu mới:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        formPanel.add(messageLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton changeButton = new JButton("Đổi mật khẩu");
        JButton backButton = new JButton("Quay lại");

        buttonPanel.add(changeButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Add form panel to main panel
        add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        changeButton.addActionListener(e -> handleChangePassword());
        backButton.addActionListener(e -> {
            if (changePasswordListener != null) {
                changePasswordListener.onBackToLogin();
            }
        });

        // Add enter key listener
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleChangePassword();
                }
            }
        };
        oldPasswordField.addKeyListener(enterListener);
        newPasswordField.addKeyListener(enterListener);
        confirmPasswordField.addKeyListener(enterListener);
    }

    private void handleChangePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validate input
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Mật khẩu mới và xác nhận không khớp!");
            return;
        }

        if (!authService.validatePassword(newPassword)) {
            messageLabel.setText("Mật khẩu mới không hợp lệ! (ít nhất 6 ký tự, không chứa khoảng trắng)");
            return;
        }

        // Attempt to change password
        if (authService.changePassword(userId, oldPassword, newPassword)) {
            messageLabel.setText("Đổi mật khẩu thành công!");
            if (changePasswordListener != null) {
                changePasswordListener.onChangePasswordSuccess();
            }
        } else {
            messageLabel.setText("Đổi mật khẩu thất bại! Mật khẩu cũ không đúng.");
        }
    }

    public void setChangePasswordListener(ChangePasswordListener listener) {
        this.changePasswordListener = listener;
    }

    public void clearFields() {
        oldPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");
        messageLabel.setText(" ");
    }
} 