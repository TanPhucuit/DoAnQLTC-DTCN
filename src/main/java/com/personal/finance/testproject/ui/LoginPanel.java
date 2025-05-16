package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.service.AuthService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private final AuthService authService;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JButton changePasswordButton;
    private final JLabel messageLabel;
    private LoginListener loginListener;

    public interface LoginListener {
        void onLoginSuccess(User user);
        void onRegisterClick();
        void onChangePasswordClick();
    }

    public LoginPanel(AuthService authService) {
        this.authService = authService;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Title
        JLabel titleLabel = new JLabel("ĐĂNG NHẬP");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Username field
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(usernameLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(300, 35));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(15));

        // Password field
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300, 35));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(20));

        // Message label for feedback
        messageLabel = new JLabel("");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Color.RED);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Login button
        loginButton = new JButton("Đăng nhập");
        loginButton.setBackground(new Color(0x2E2E5D));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setMaximumSize(new Dimension(300, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(15));

        // Register button
        registerButton = new JButton("Đăng ký tài khoản mới");
        registerButton.setBackground(new Color(0x2E2E5D));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(300, 30));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(registerButton);
        mainPanel.add(Box.createVerticalStrut(10));

        // Change password button
        changePasswordButton = new JButton("Quên mật khẩu?");
        changePasswordButton.setBackground(new Color(0x2E2E5D));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        changePasswordButton.setFocusPainted(false);
        changePasswordButton.setBorderPainted(false);
        changePasswordButton.setMaximumSize(new Dimension(300, 30));
        changePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(changePasswordButton);

        // Add main panel to center
        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        setupActionListeners();
        setupFocusListeners();
    }

    private void setupActionListeners() {
        // Login button action
        loginButton.addActionListener(e -> {
            // Clear previous message
            messageLabel.setText("");
            messageLabel.setForeground(Color.RED);

            // Get input values
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            // Validate input
            if (username.isEmpty()) {
                messageLabel.setText("Vui lòng nhập tên đăng nhập");
                usernameField.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                messageLabel.setText("Vui lòng nhập mật khẩu");
                passwordField.requestFocus();
                return;
            }

            // Disable login button and show processing state
            loginButton.setEnabled(false);
            loginButton.setText("Đang xử lý...");
            loginButton.setBackground(new Color(108, 117, 125));

            // Process login in background
            SwingUtilities.invokeLater(() -> {
                try {
                    User user = authService.login(username, password);
                    if (loginListener != null) {
                        loginListener.onLoginSuccess(user);
                    }
                } catch (IllegalArgumentException ex) {
                    messageLabel.setText(ex.getMessage());
                    if (ex.getMessage().contains("mật khẩu")) {
                        passwordField.setText("");
                        passwordField.requestFocus();
                    } else {
                        usernameField.requestFocus();
                    }
                } catch (Exception ex) {
                    messageLabel.setText("Lỗi đăng nhập: " + ex.getMessage());
                } finally {
                    // Re-enable login button
                    loginButton.setEnabled(true);
                    loginButton.setText("Đăng nhập");
                    loginButton.setBackground(new Color(0x2E2E5D));
                }
            });
        });

        // Register button action
        registerButton.addActionListener(e -> {
            if (loginListener != null) {
                loginListener.onRegisterClick();
            }
        });

        // Change password button action
        changePasswordButton.addActionListener(e -> {
            if (loginListener != null) {
                loginListener.onChangePasswordClick();
            }
        });

        // Add enter key listener for password field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
    }

    private void setupFocusListeners() {
        // Username field focus listener
        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));
                if (messageLabel.getText().contains("tên đăng nhập")) {
                    messageLabel.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                usernameField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));
            }
        });

        // Password field focus listener
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 123, 255), 2),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));
                if (messageLabel.getText().contains("mật khẩu")) {
                    messageLabel.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)
                ));
            }
        });
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
        usernameField.requestFocus();
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public void requestUsernameFocus() {
        usernameField.requestFocus();
    }

    
} 