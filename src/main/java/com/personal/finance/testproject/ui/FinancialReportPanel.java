package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;

public class FinancialReportPanel extends JPanel {
    private final int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0xF5F5F5);
    private static final Color PANEL_BG = new Color(0xFFFFFF);
    private static final Color TABLE_BG = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x333333);
    private static final Color ACCENT = new Color(0x2196F3);

    public FinancialReportPanel(int userId) {
        this.userId = userId;
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        initializeUI();
    }

    private JComboBox<String> reportTypeCombo;
    private JComboBox<String> valueCombo;
    private DefaultTableModel mainModel;
    private DefaultTableModel detailModel;
    private JPanel detailPanel;

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PANEL_BG);
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "Báo cáo tài chính", 0, 0, new Font("Segoe UI", Font.BOLD, 18), TEXT_DARK));

        // Top controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        controlPanel.setBackground(PANEL_BG);
        reportTypeCombo = new JComboBox<>(new String[]{"Báo cáo tháng", "Báo cáo quý", "Báo cáo năm"});
        valueCombo = new JComboBox<>();
        controlPanel.add(new JLabel("Loại báo cáo:"));
        controlPanel.add(reportTypeCombo);
        controlPanel.add(new JLabel("Chọn:"));
        controlPanel.add(valueCombo);
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Main table
        String[] mainCols = {"Chỉ tiêu", "Giá trị"};
        mainModel = new DefaultTableModel(mainCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable mainTable = new JTable(mainModel);
        mainTable.setRowHeight(30);
        mainTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainTable.getTableHeader().setBackground(PANEL_BG);
        mainTable.getTableHeader().setForeground(ACCENT);
        mainTable.setBackground(TABLE_BG);
        mainTable.setForeground(TEXT_DARK);
        mainTable.setGridColor(ACCENT);
        JScrollPane mainScroll = new JScrollPane(mainTable);
        mainScroll.setBackground(PANEL_BG);
        mainPanel.add(mainScroll, BorderLayout.CENTER);

        // Detail panel (for account_balance, only show when report type is month)
        detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(PANEL_BG);
        String[] detailCols = {"Chỉ tiêu", "Giá trị"};
        detailModel = new DefaultTableModel(detailCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(28);
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        detailTable.getTableHeader().setBackground(PANEL_BG);
        detailTable.getTableHeader().setForeground(ACCENT);
        detailTable.setBackground(TABLE_BG);
        detailTable.setForeground(TEXT_DARK);
        detailTable.setGridColor(ACCENT);
        JScrollPane detailScroll = new JScrollPane(detailTable);
        detailScroll.setBackground(PANEL_BG);
        detailPanel.add(new JLabel("\u2022 Thông tin chi tiết tài khoản tháng đã chọn", SwingConstants.LEFT), BorderLayout.NORTH);
        detailPanel.add(detailScroll, BorderLayout.CENTER);
        detailPanel.setVisible(false);
        mainPanel.add(detailPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Sự kiện thay đổi loại báo cáo hoặc giá trị
        reportTypeCombo.addActionListener(e -> updateValueCombo());
        valueCombo.addActionListener(e -> reloadReportData());
        updateValueCombo(); // Khởi tạo lần đầu
    }

    private void updateValueCombo() {
        valueCombo.removeAllItems();
        String type = (String) reportTypeCombo.getSelectedItem();
        if (type == null) return;
        if (type.equals("Báo cáo tháng")) {
            for (int i = 1; i <= 12; i++) valueCombo.addItem("Tháng " + i);
        } else if (type.equals("Báo cáo quý")) {
            for (int i = 1; i <= 4; i++) valueCombo.addItem("Quý " + i);
        } else if (type.equals("Báo cáo năm")) {
            valueCombo.addItem("2025");
        }
        valueCombo.setSelectedIndex(0);
        reloadReportData();
    }

    private void reloadReportData() {
        mainModel.setRowCount(0);
        detailModel.setRowCount(0);
        detailPanel.setVisible(false);
        String type = (String) reportTypeCombo.getSelectedItem();
        String value = (String) valueCombo.getSelectedItem();
        if (type == null || value == null) return;
        try {
            if (type.equals("Báo cáo tháng")) {
                int month = valueCombo.getSelectedIndex() + 1;
                // Lấy dữ liệu statistic_report
                String sql = "SELECT average_per_day, average_per_week, cumulative_pnl, up_date FROM statistic_report WHERE UserID = ? AND sr_month = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, String.valueOf(month));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    mainModel.addRow(new Object[]{"Chi tiêu TB/ngày", rs.getBigDecimal("average_per_day")});
                    mainModel.addRow(new Object[]{"Chi tiêu TB/tuần", rs.getBigDecimal("average_per_week")});
                    mainModel.addRow(new Object[]{"Lợi nhuận tích lũy", rs.getBigDecimal("cumulative_pnl")});
                    mainModel.addRow(new Object[]{"Ngày cập nhật", rs.getDate("up_date")});
                } else {
                    mainModel.addRow(new Object[]{"Không có dữ liệu cho tháng này", ""});
                }
                // Lấy dữ liệu account_balance
                String sql2 = "SELECT total_remain_income, total_remain_save, total_loan_remain, total_spend, total_invest, total_invest_property, balance, up_date FROM account_balance WHERE UserID = ? AND ab_month = ?";
                PreparedStatement stmt2 = connection.prepareStatement(sql2);
                stmt2.setInt(1, userId);
                stmt2.setString(2, String.valueOf(month));
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) {
                    detailModel.addRow(new Object[]{"Tổng thu nhập còn lại", rs2.getBigDecimal("total_remain_income")});
                    detailModel.addRow(new Object[]{"Tổng tiết kiệm còn lại", rs2.getBigDecimal("total_remain_save")});
                    detailModel.addRow(new Object[]{"Tổng dư nợ khoản vay", rs2.getBigDecimal("total_loan_remain")});
                    detailModel.addRow(new Object[]{"Tổng chi tiêu", rs2.getBigDecimal("total_spend")});
                    detailModel.addRow(new Object[]{"Tổng đầu tư", rs2.getBigDecimal("total_invest")});
                    detailModel.addRow(new Object[]{"Tổng giá trị tài sản đầu tư", rs2.getBigDecimal("total_invest_property")});
                    detailModel.addRow(new Object[]{"Số dư cuối tháng", rs2.getBigDecimal("balance")});
                    detailModel.addRow(new Object[]{"Ngày cập nhật", rs2.getDate("up_date")});
                    detailPanel.setVisible(true);
                }
            } else if (type.equals("Báo cáo quý")) {
                int quarter = valueCombo.getSelectedIndex();
                String[] quarters = {"first", "second", "third", "fourth"};
                String sql = "SELECT average_per_day, average_per_week, cumulative_pnl, up_date FROM quarterly_statistic WHERE UserID = ? AND sr_quarter = ? AND sr_year = 2025";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, quarters[quarter]);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    mainModel.addRow(new Object[]{"Chi tiêu TB/ngày", rs.getBigDecimal("average_per_day")});
                    mainModel.addRow(new Object[]{"Chi tiêu TB/tuần", rs.getBigDecimal("average_per_week")});
                    mainModel.addRow(new Object[]{"Lợi nhuận tích lũy", rs.getBigDecimal("cumulative_pnl")});
                    mainModel.addRow(new Object[]{"Ngày cập nhật", rs.getDate("up_date")});
                } else {
                    mainModel.addRow(new Object[]{"Không có dữ liệu cho quý này", ""});
                }
            } else if (type.equals("Báo cáo năm")) {
                String sql = "SELECT average_per_day, average_per_week, cumulative_pnl, up_date FROM annual_statistic WHERE UserID = ? AND sr_year = 2025";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    mainModel.addRow(new Object[]{"Chi tiêu TB/ngày", rs.getBigDecimal("average_per_day")});
                    mainModel.addRow(new Object[]{"Chi tiêu TB/tuần", rs.getBigDecimal("average_per_week")});
                    mainModel.addRow(new Object[]{"Lợi nhuận tích lũy", rs.getBigDecimal("cumulative_pnl")});
                    mainModel.addRow(new Object[]{"Ngày cập nhật", rs.getDate("up_date")});
                } else {
                    mainModel.addRow(new Object[]{"Không có dữ liệu cho năm này", ""});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu báo cáo tài chính: " + e.getMessage());
        }
    }
} 