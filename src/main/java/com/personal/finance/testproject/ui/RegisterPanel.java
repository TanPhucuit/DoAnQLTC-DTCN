package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.model.UserInformation;
import com.personal.finance.testproject.service.AuthService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class RegisterPanel extends JPanel {
    private final AuthService authService;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JTextField fullNameField;
    private final JTextField countryField;
    private final JTextField cityField;
    private final JTextField phoneField;
    private final JLabel messageLabel;
    private RegisterListener registerListener;

    public interface RegisterListener {
        void onRegisterSuccess();
        void onBackToLogin();
    }

    public RegisterPanel(AuthService authService) {
        this.authService = authService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel chính chứa form đăng ký
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        confirmPasswordField = new JPasswordField(20);
        formPanel.add(confirmPasswordField, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        fullNameField = new JTextField(20);
        formPanel.add(fullNameField, gbc);

        // Country
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Country:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        countryField = new JTextField(20);
        formPanel.add(countryField, gbc);

        // City
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("City:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        cityField = new JTextField(20);
        formPanel.add(cityField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phone:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);

        // Message label
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        messageLabel = new JLabel(" ");
        messageLabel.setForeground(Color.RED);
        formPanel.add(messageLabel, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton registerButton = new JButton("Đăng ký");
        JButton backButton = new JButton("Quay lại");

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        // Add form panel to main panel
        add(formPanel, BorderLayout.CENTER);

        // Add action listeners
        registerButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> {
            if (registerListener != null) {
                registerListener.onBackToLogin();
            }
        });

        // Add enter key listener
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleRegister();
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
        confirmPasswordField.addKeyListener(enterListener);
        fullNameField.addKeyListener(enterListener);
        countryField.addKeyListener(enterListener);
        cityField.addKeyListener(enterListener);
        phoneField.addKeyListener(enterListener);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String fullName = fullNameField.getText().trim();
        String country = countryField.getText().trim();
        String city = cityField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            fullName.isEmpty() || country.isEmpty()) {
            messageLabel.setText("Vui lòng nhập đầy đủ thông tin bắt buộc!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Mật khẩu xác nhận không khớp!");
            return;
        }

        if (!authService.validateUserName(username)) {
            messageLabel.setText("Username không hợp lệ! (ít nhất 4 ký tự, chỉ chứa chữ và số)");
            return;
        }

        if (!authService.validatePassword(password)) {
            messageLabel.setText("Password không hợp lệ! (ít nhất 6 ký tự, không chứa khoảng trắng)");
            return;
        }

        // Create user and user info objects
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        UserInformation userInfo = new UserInformation();
        userInfo.setFullName(fullName);
        userInfo.setCountry(country);
        userInfo.setCity(city);
        userInfo.setPhoneNumber(phone);

        // Attempt registration
        if (authService.register(user, userInfo)) {
            messageLabel.setText("Đăng ký thành công!");
            if (registerListener != null) {
                registerListener.onRegisterSuccess();
            }
        } else {
            messageLabel.setText("Đăng ký thất bại! Username có thể đã tồn tại.");
        }
    }

    public void setRegisterListener(RegisterListener listener) {
        this.registerListener = listener;
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        fullNameField.setText("");
        countryField.setText("");
        cityField.setText("");
        phoneField.setText("");
        messageLabel.setText(" ");
    }
} 