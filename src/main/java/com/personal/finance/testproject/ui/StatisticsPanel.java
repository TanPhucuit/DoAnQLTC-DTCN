package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;
import java.util.Locale;
import com.personal.finance.testproject.util.DatabaseConnection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class StatisticsPanel extends JPanel {
    private static final Color MAIN_BG = new Color(0xF5F5F5);
    private static final Color PANEL_BG = new Color(0xFFFFFF);
    private static final Color CARD_BG = new Color(0xFFFFFF);
    private static final Color TABLE_BG = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x333333);
    private static final Color ACCENT = new Color(0x2196F3);
    private static final Color PROFIT_COLOR = new Color(0x4CAF50);
    private static final Color LOSS_COLOR = new Color(0xF44336);
    private static final Color WARNING_COLOR = new Color(0xFFC107);
    private static final Color ERROR_COLOR = new Color(0xF44336);

    private final int userId;
    private Connection connection = null;
    private final NumberFormat currencyFormat;
    private final NumberFormat numberFormat;

    public StatisticsPanel(int userId) {
        this.userId = userId;
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        this.numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        this.numberFormat.setMaximumFractionDigits(2);
        
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
        setLayout(new GridBagLayout());
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Panel chọn tháng và thông số
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        filterPanel.setBackground(MAIN_BG);
        JLabel lblMonth = new JLabel("Chọn tháng:");
        lblMonth.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JComboBox<String> monthCombo = new JComboBox<>();
        monthCombo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        monthCombo.setPreferredSize(new Dimension(80, 40));
        try {
            String sql = "SELECT DISTINCT ab_month FROM account_balance WHERE UserID = ? ORDER BY ab_month";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                monthCombo.addItem(rs.getString("ab_month"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải tháng: " + e.getMessage());
        }
        JLabel lblStat = new JLabel("Chọn thông số:");
        lblStat.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JComboBox<String> statCombo = new JComboBox<>(new String[]{
            "Thống kê tổng thu nhập còn lại",
            "Thống kê tổng tiết kiệm còn lại",
            "Thống kê tổng dư nợ vay",
            "Thống kê tổng chi tiêu",
            "Thống kê tổng đầu tư",
            "Thống kê tổng tài sản đầu tư",
            "Thống kê số dư cuối",
            "Thống kê ngày cập nhật"
        });
        statCombo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        statCombo.setPreferredSize(new Dimension(320, 40));
        JButton btnStat = new JButton("Thống kê");
        btnStat.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnStat.setBackground(new Color(0x008BCF));
        btnStat.setForeground(Color.WHITE);
        btnStat.setFocusPainted(false);
        btnStat.setBorderPainted(false);
        btnStat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnStat.setPreferredSize(new Dimension(150, 45));
        filterPanel.add(lblMonth);
        filterPanel.add(monthCombo);
        filterPanel.add(lblStat);
        filterPanel.add(statCombo);
        filterPanel.add(btnStat);
        gbc.gridwidth = 1;
        add(filterPanel, gbc);

        // Kết quả
        gbc.gridy = 1;
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        resultLabel.setForeground(TEXT_DARK);
        resultLabel.setPreferredSize(new Dimension(900, 80));
        add(resultLabel, gbc);

        btnStat.addActionListener(e -> {
            String abMonth = (String) monthCombo.getSelectedItem();
            int statIdx = statCombo.getSelectedIndex();
            String[] fields = {"total_remain_income", "total_remain_save", "total_loan_remain", "total_spend", "total_invest", "total_invest_property", "balance", "up_date"};
            String field = fields[statIdx];
            String label = (String) statCombo.getSelectedItem();
            try {
                String sql = "SELECT " + field + " FROM account_balance WHERE ab_month = ? AND UserID = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, abMonth);
                stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Object value = rs.getObject(1);
                    resultLabel.setText("<html><b>" + label + ": " + (value != null ? value.toString() : "Không có dữ liệu") + "</b></html>");
                } else {
                    resultLabel.setText("<html><b>" + label + ": Không có dữ liệu</b></html>");
                }
            } catch (SQLException ex) {
                resultLabel.setText("<html><b>Lỗi khi thống kê: " + ex.getMessage() + "</b></html>");
            }
        });
    }

    private void loadAccountBalanceData(DefaultTableModel model, int month) {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE UserID = ? AND ab_month = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, String.valueOf(month));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ab_month"));
                row.add(rs.getBigDecimal("total_remain_income"));
                row.add(rs.getBigDecimal("total_remain_save"));
                row.add(rs.getBigDecimal("total_loan_remain"));
                row.add(rs.getBigDecimal("total_spend"));
                row.add(rs.getBigDecimal("total_invest"));
                row.add(rs.getBigDecimal("total_invest_property"));
                row.add(rs.getBigDecimal("balance"));
                row.add(rs.getDate("up_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu account_balance: " + e.getMessage());
        }
    }
} 