package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.dao.impl.UserDAOImpl;
import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.service.AuthService;
import com.personal.finance.testproject.service.impl.AuthServiceImpl;
import com.personal.finance.testproject.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private AuthService authService;
    private LoginPanel loginPanel;
    private MainFrame mainFrame;
    private RegisterFrame registerFrame;
    private ChangePasswordFrame changePasswordFrame;

    public LoginFrame() {
        try {
            // Initialize database connection and services
            Connection connection = DatabaseConnection.getConnection();
            authService = new AuthServiceImpl(new UserDAOImpl(connection));

            // Setup frame
            setTitle("Đăng nhập - Quản Lý tài chính và đầu tư");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 600);
            setLocationRelativeTo(null);
            setResizable(false);

            // Create login panel
            loginPanel = new LoginPanel(authService);
            loginPanel.setLoginListener(new LoginPanel.LoginListener() {
                @Override
                public void onLoginSuccess(User user) {
                    SwingUtilities.invokeLater(() -> {
                        // Hide login frame
                        setVisible(false);

                        // Create and show main frame
                        if (mainFrame == null) {
                            mainFrame = new MainFrame(user, authService);
                            mainFrame.setLogoutListener(() -> {
                                // Handle logout
                                mainFrame.dispose();
                                mainFrame = null;
                                loginPanel.clearFields();
                                setVisible(true);
                            });
                        }
                        mainFrame.setVisible(true);
                    });
                }

                @Override
                public void onRegisterClick() {
                    SwingUtilities.invokeLater(() -> {
                        // Hide login frame
                        setVisible(false);

                        // Create and show register frame
                        if (registerFrame == null) {
                            registerFrame = new RegisterFrame(authService);
                            registerFrame.setRegisterListener(() -> {
                                // Handle register success
                                registerFrame.dispose();
                                registerFrame = null;
                                loginPanel.clearFields();
                                setVisible(true);
                            });
                        }
                        registerFrame.setVisible(true);
                    });
                }

                @Override
                public void onChangePasswordClick() {
                    SwingUtilities.invokeLater(() -> {
                        // Get username from login panel
                        String username = loginPanel.getUsername();
                        if (username == null || username.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(LoginFrame.this,
                                "Vui lòng nhập tên đăng nhập trước khi đổi mật khẩu",
                                "Thông báo",
                                JOptionPane.WARNING_MESSAGE);
                            loginPanel.requestUsernameFocus();
                            return;
                        }

                        // Create and show change password frame
                        if (changePasswordFrame == null) {
                            changePasswordFrame = new ChangePasswordFrame(authService, username.trim());
                            changePasswordFrame.setChangePasswordListener(() -> {
                                // Handle change password success
                                changePasswordFrame.dispose();
                                changePasswordFrame = null;
                                loginPanel.clearFields();
                                setVisible(true);
                            });
                        }
                        changePasswordFrame.setVisible(true);
                    });
                }
            });

            // Add login panel to frame
            add(loginPanel);

            // Show frame
            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi kết nối database: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
} 