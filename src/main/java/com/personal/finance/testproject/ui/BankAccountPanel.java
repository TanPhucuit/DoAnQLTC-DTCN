package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.Vector;
import com.personal.finance.testproject.dao.*;
import com.personal.finance.testproject.model.*;
import com.personal.finance.testproject.dao.impl.*;
import com.personal.finance.testproject.util.DatabaseConnection;

public class BankAccountPanel extends JPanel {
    private int userId;
    private JTabbedPane tabbedPane;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0x000000); // Đen tuyệt đối
    private static final Color PANEL_BG = new Color(0xE0E0E0); // Xám nhạt
    private static final Color TABLE_BG = Color.WHITE;
    private static final Color TEXT_DARK = Color.BLACK;
    private static final Color ACCENT = new Color(0x2E2E5D);

    public BankAccountPanel(int userId) {
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
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(PANEL_BG);
        tabbedPane.setForeground(TEXT_DARK);
        
        // Add tabs for bank account functions
        tabbedPane.addTab("Thông tin tài khoản", createAccountInfoPanel());
        tabbedPane.addTab("Chuyển tiền", createTransferPanel());
        tabbedPane.addTab("Lịch sử chuyển tiền", createTransferHistoryPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createAccountInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        String[] columnNames = {"Số tài khoản", "Ngân hàng", "Số dư", "Ghi chú"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(PANEL_BG);
        table.getTableHeader().setForeground(ACCENT);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(PANEL_BG);

        // Load bank account data
        loadAccountData(model);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTransferPanel() {
        // Tích hợp giao diện chuyển tiền mới
        return new TransferLayeredPanel(userId);
    }

    private JPanel createTransferHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        String[] columnNames = {"Mã giao dịch", "Tài khoản nguồn", "Tài khoản thụ hưởng", "Số tiền", "Ngày chuyển"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(PANEL_BG);
        table.getTableHeader().setForeground(ACCENT);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(PANEL_BG);

        // Load transaction history
        loadTransferHistory(model);

        panel.add(scrollPane, BorderLayout.CENTER);
        // Thêm nút làm mới ở góc dưới cùng bên phải
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(PANEL_BG);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRefresh.setBackground(new Color(0x008BCF));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createLineBorder(ACCENT, 1, true));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadTransferHistory(model));
        bottomPanel.add(btnRefresh);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        panel.setMinimumSize(new Dimension(400, 300));
        scrollPane.setPreferredSize(new Dimension(800, 300));
        panel.revalidate();
        panel.repaint();
        return panel;
    }

    private void loadAccountData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM BANK_ACCOUNT WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("BankAccountNumber"));
                row.add(rs.getString("BankName"));
                row.add(rs.getBigDecimal("Balance"));
                row.add(rs.getString("Note"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu tài khoản ngân hàng: " + e.getMessage());
        }
    }

    private void loadTransferHistory(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM BANK_TRANSFER WHERE SourceBankAccountNumber IN (SELECT BankAccountNumber FROM BANK_ACCOUNT WHERE UserID = ?) OR TargetBankAccountNumber IN (SELECT BankAccountNumber FROM BANK_ACCOUNT WHERE UserID = ?) ORDER BY Transfer_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("BankTransferID"));
                row.add(rs.getString("SourceBankAccountNumber"));
                row.add(rs.getString("TargetBankAccountNumber"));
                row.add(rs.getBigDecimal("Transfer_amount"));
                row.add(rs.getDate("Transfer_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lịch sử chuyển tiền: " + e.getMessage());
        }
    }
} 