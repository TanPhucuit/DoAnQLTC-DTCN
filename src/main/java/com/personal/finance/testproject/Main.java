package com.personal.finance.testproject;

import com.personal.finance.testproject.dao.UserDAO;
import com.personal.finance.testproject.dao.impl.UserDAOImpl;
import com.personal.finance.testproject.service.AuthService;
import com.personal.finance.testproject.service.impl.AuthServiceImpl;
import com.personal.finance.testproject.ui.LoginFrame;
import com.personal.finance.testproject.util.DatabaseConnection;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            // Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Lỗi khởi tạo giao diện: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        try {
            // Get database connection
            Connection connection = DatabaseConnection.getConnection();

            // Khởi tạo UserDAO và AuthService
            UserDAO userDAO = new UserDAOImpl(connection);
            AuthService authService = new AuthServiceImpl(userDAO);

            // Create and show login window
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Lỗi kết nối database: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Lỗi khởi tạo ứng dụng: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
} 