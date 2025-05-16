package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.personal.finance.testproject.dao.*;
import com.personal.finance.testproject.model.*;
import com.personal.finance.testproject.dao.impl.*;
import com.personal.finance.testproject.util.DatabaseConnection;

public class FinancialStatusPanel extends JPanel {
    private int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0x000000); // Đen tuyệt đối
    private static final Color PANEL_BG = new Color(0xE0E0E0); // Xám nhạt
    private static final Color BTN_BG = new Color(0x2E2E5D); // Xanh tím đậm
    private static final Color BTN_TEXT = Color.WHITE;
    private static final Color LABEL_TEXT = Color.BLACK;

    public FinancialStatusPanel(int userId) {
        this.userId = userId;
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi kết nối database: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(MAIN_BG);
        JButton btnSpend = new JButton("Trạng thái chi tiêu");
        JButton btnInvest = new JButton("Trạng thái đầu tư");
        JButton btnOverall = new JButton("Trạng thái tài chính");
        for (JButton btn : new JButton[]{btnSpend, btnInvest, btnOverall}) {
            btn.setBackground(BTN_BG);
            btn.setForeground(BTN_TEXT);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        buttonPanel.add(btnSpend);
        buttonPanel.add(btnInvest);
        buttonPanel.add(btnOverall);
        JPanel statusPanel = new JPanel(new GridLayout(1, 1));
        statusPanel.setBackground(MAIN_BG);
        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(PANEL_BG);
        statusLabel.setForeground(LABEL_TEXT);
        statusPanel.add(statusLabel);
        add(buttonPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.CENTER);
        btnSpend.addActionListener(e -> showSpendingStatus(statusLabel));
        btnInvest.addActionListener(e -> showInvestmentStatus(statusLabel));
        btnOverall.addActionListener(e -> showOverallStatus(statusLabel));
    }

    private void showSpendingStatus(JLabel label) {
        try {
            String sql = "SELECT over_max_amount(?) as result";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt("result") == 1) {
                label.setText("Trạng thái chi tiêu: Không an toàn");
                label.setForeground(new Color(220, 53, 69));
            } else {
                label.setText("Trạng thái chi tiêu: An toàn");
                label.setForeground(new Color(40, 167, 69));
            }
        } catch (SQLException e) {
            label.setText("Lỗi khi kiểm tra trạng thái chi tiêu: " + e.getMessage());
            label.setForeground(Color.RED);
        }
    }

    private void showInvestmentStatus(JLabel label) {
        try {
            String sql = "SELECT check_invest_profit(?) as result";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt("result") == 1) {
                label.setText("Trạng thái đầu tư: Không an toàn");
                label.setForeground(new Color(220, 53, 69));
            } else {
                label.setText("Trạng thái đầu tư: An toàn");
                label.setForeground(new Color(40, 167, 69));
            }
        } catch (SQLException e) {
            label.setText("Lỗi khi kiểm tra trạng thái đầu tư: " + e.getMessage());
            label.setForeground(Color.RED);
        }
    }

    private void showOverallStatus(JLabel label) {
        try {
            int spendStatus = 0, investStatus = 0;
            String sql1 = "SELECT over_max_amount(?) as result";
            PreparedStatement stmt1 = connection.prepareStatement(sql1);
            stmt1.setInt(1, userId);
            ResultSet rs1 = stmt1.executeQuery();
            if (rs1.next()) spendStatus = rs1.getInt("result");
            String sql2 = "SELECT check_invest_profit(?) as result";
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setInt(1, userId);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()) investStatus = rs2.getInt("result");
            if (spendStatus == 0 && investStatus == 0) {
                label.setText("Trạng thái tài chính : An toàn");
                label.setForeground(new Color(40, 167, 69));
            } else {
                label.setText("Trạng thái tài chính: Không an toàn");
                label.setForeground(new Color(220, 53, 69));
            }
        } catch (SQLException e) {
            label.setText("Lỗi khi kiểm tra trạng thái tổng: " + e.getMessage());
            label.setForeground(Color.RED);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        super.finalize();
    }
} 