package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;

public class InvestStoragePanel extends JPanel {
    private final int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0xF5F5F5);
    private static final Color PANEL_BG = new Color(0xFFFFFF);
    private static final Color TABLE_BG = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x333333);
    private static final Color ACCENT = new Color(0x2196F3);

    public InvestStoragePanel(int userId) {
        this.userId = userId;
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tabbedPane.setBackground(PANEL_BG);
        tabbedPane.setForeground(TEXT_DARK);
        tabbedPane.setPreferredSize(new Dimension(1000, 500));

        // Tab 1: Danh mục đầu tư của tôi
        JPanel myInvestPanel = new JPanel(new BorderLayout());
        myInvestPanel.setBackground(PANEL_BG);
        String[] investCols = {"Mã tài sản", "Số lượng", "Giá mua TB", "Lợi nhuận ước tính", "Ngày cập nhật"};
        DefaultTableModel investModel = new DefaultTableModel(investCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable investTable = new JTable(investModel);
        investTable.setRowHeight(36);
        investTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        investTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        investTable.getTableHeader().setBackground(PANEL_BG);
        investTable.getTableHeader().setForeground(ACCENT);
        investTable.setBackground(TABLE_BG);
        investTable.setForeground(TEXT_DARK);
        investTable.setGridColor(ACCENT);
        investTable.setFillsViewportHeight(true);
        investTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane investScroll = new JScrollPane(investTable);
        investScroll.setBackground(PANEL_BG);
        investScroll.setPreferredSize(new Dimension(750, 220));
        myInvestPanel.add(investScroll, BorderLayout.CENTER);
        loadInvestStorageData(investModel);
        tabbedPane.addTab("Danh mục đầu tư của tôi", myInvestPanel);

        // Tab 2: Thông tin chi tiết các loại tài sản
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(PANEL_BG);
        String[] detailCols = {"Mã tài sản", "Cấp rủi ro", "Giá hiện tại", "Z-Score", "Độ lệch chuẩn", "Đơn vị"};
        DefaultTableModel detailModel = new DefaultTableModel(detailCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(36);
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailTable.getTableHeader().setBackground(PANEL_BG);
        detailTable.getTableHeader().setForeground(ACCENT);
        detailTable.setBackground(TABLE_BG);
        detailTable.setForeground(TEXT_DARK);
        detailTable.setGridColor(ACCENT);
        detailTable.setFillsViewportHeight(true);
        detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane detailScroll = new JScrollPane(detailTable);
        detailScroll.setBackground(PANEL_BG);
        detailScroll.setPreferredSize(new Dimension(750, 220));
        detailPanel.add(detailScroll, BorderLayout.CENTER);
        loadInvestStorageDetailData(detailModel);
        tabbedPane.addTab("Thông tin chi tiết tài sản", detailPanel);

        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(tabbedPane, gbc);
    }

    private void loadInvestStorageData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT InStID, num_unit, buy_price, es_profit, up_date FROM INVEST_STORAGE WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("InStID"));
                row.add(rs.getBigDecimal("num_unit"));
                row.add(rs.getBigDecimal("buy_price"));
                row.add(rs.getBigDecimal("es_profit"));
                row.add(rs.getDate("up_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu danh mục đầu tư: " + e.getMessage());
        }
    }

    private void loadInvestStorageDetailData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM INVEST_STORAGE_DETAIL";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("InStID"));
                row.add(rs.getInt("riskLevel"));
                row.add(rs.getBigDecimal("cur_price"));
                row.add(rs.getBigDecimal("z_score"));
                row.add(rs.getBigDecimal("standard_deviation"));
                row.add(rs.getString("unit"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu chi tiết tài sản: " + e.getMessage());
        }
    }

    private void loadLoanListData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT l.LoanID, p.purpose_name, f.description as form_description, l.loan_amount, l.disbur_date " +
                         "FROM LOAN l " +
                         "JOIN PURPOSE p ON l.PurposeID = p.PurposeID AND l.UserID = p.UserID " +
                         "JOIN FORM f ON l.FormID = f.FormID " +
                         "WHERE l.UserID = ? ORDER BY l.disbur_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("LoanID"));
                row.add(rs.getString("purpose_name"));
                row.add(rs.getString("form_description"));
                row.add(rs.getBigDecimal("loan_amount"));
                row.add(rs.getDate("disbur_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khoản vay: " + e.getMessage());
        }
    }

    private void loadLoanDetailData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT LoanID, interest, num_term, pay_per_term, date_of_payment, num_paid_term, paid_amount, loan_remain " +
                         "FROM LOAN WHERE UserID = ? ORDER BY disbur_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("LoanID"));
                row.add(rs.getBigDecimal("interest").multiply(new java.math.BigDecimal("100")));
                row.add(rs.getInt("num_term"));
                row.add(rs.getBigDecimal("pay_per_term"));
                row.add(rs.getInt("date_of_payment"));
                row.add(rs.getInt("num_paid_term"));
                row.add(rs.getBigDecimal("paid_amount"));
                row.add(rs.getBigDecimal("loan_remain"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết khoản vay: " + e.getMessage());
        }
    }
} 