package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import com.personal.finance.testproject.util.DatabaseConnection;
import com.personal.finance.testproject.dao.impl.InvestStorageDAOImpl;
import com.personal.finance.testproject.dao.impl.AllocationRatioDAOImpl;
import com.personal.finance.testproject.model.AllocationRatio;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class InvestmentPlanPanel extends JPanel {
    private final int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0xF5F5F5);
    private static final Color PANEL_BG = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x333333);
    private static final Color ACCENT = new Color(0x2196F3);
    private JTable allocationTable;
    private DefaultTableModel allocationModel;
    private JLabel statusLabel;

    public InvestmentPlanPanel(int userId) {
        this.userId = userId;
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PANEL_BG);
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "Kế hoạch đầu tư", 0, 0, new Font("Segoe UI", Font.BOLD, 18), TEXT_DARK));

        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 30, 10));
        summaryPanel.setBackground(PANEL_BG);
        JLabel lblCurInvestProperty = new JLabel();
        JLabel lblEsProfit = new JLabel();
        JLabel lblCumulativePnl = new JLabel();
        JLabel lblWarningLossRate = new JLabel();
        for (JLabel lbl : new JLabel[]{lblCurInvestProperty, lblEsProfit, lblCumulativePnl, lblWarningLossRate}) {
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lbl.setForeground(TEXT_DARK);
        }
        summaryPanel.add(lblCurInvestProperty);
        summaryPanel.add(lblEsProfit);
        summaryPanel.add(lblCumulativePnl);
        summaryPanel.add(lblWarningLossRate);
        mainPanel.add(summaryPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(PANEL_BG);
        JLabel lblInvestorType = new JLabel();
        lblInvestorType.setHorizontalAlignment(SwingConstants.LEFT);
        lblInvestorType.setVerticalAlignment(SwingConstants.CENTER);
        lblInvestorType.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblInvestorType.setText("");
        infoPanel.add(lblInvestorType, BorderLayout.WEST);

        JPanel allocationPanel = new JPanel(new BorderLayout());
        allocationPanel.setBackground(PANEL_BG);
        allocationPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT), "Bảng phân bổ tài sản", 0, 0, new Font("Segoe UI", Font.BOLD, 16), TEXT_DARK));

        String[] columns = {"Mức độ rủi ro", "Tỷ lệ mục tiêu", "Giá trị hiện tại", "Tỷ lệ thực tế"};
        allocationModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        allocationTable = new JTable(allocationModel);
        allocationTable.setRowHeight(32);
        allocationTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        allocationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        allocationTable.getTableHeader().setBackground(PANEL_BG);
        allocationTable.getTableHeader().setForeground(ACCENT);
        allocationTable.setBackground(PANEL_BG);
        allocationTable.setForeground(TEXT_DARK);
        allocationTable.setGridColor(ACCENT);
        allocationTable.setFillsViewportHeight(true);
        allocationTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane allocationScroll = new JScrollPane(allocationTable);
        allocationScroll.setBackground(PANEL_BG);
        allocationScroll.setPreferredSize(new Dimension(750, 150));
        allocationPanel.add(allocationScroll, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout(10, 0));
        statusPanel.setBackground(PANEL_BG);
        JButton checkButton = new JButton("Kiểm tra trạng thái phân bổ");
        checkButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkButton.setBackground(new Color(0x2E2E5D));
        checkButton.setForeground(Color.WHITE);
        checkButton.setFocusPainted(false);
        checkButton.setBorderPainted(false);
        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        statusLabel = new JLabel("", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(TEXT_DARK);
        statusPanel.add(checkButton, BorderLayout.WEST);
        statusPanel.add(statusLabel, BorderLayout.CENTER);
        checkButton.addActionListener(e -> checkAllocationStatus());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PANEL_BG);
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(allocationPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(statusPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        loadInvestmentPlan(lblInvestorType, lblCurInvestProperty, lblEsProfit, lblCumulativePnl, lblWarningLossRate);
        loadAllocationTable();
    }

    private void loadInvestmentPlan(JLabel lblInvestorType, JLabel lblCurInvestProperty, JLabel lblEsProfit, JLabel lblCumulativePnl, JLabel lblWarningLossRate) {
        try {
            String sql = "SELECT fp.Investor_type, fp.warning_loss_rate, fp.cur_invest_property, fp.cur_es_profit, fp.cur_cumulative_pnl FROM FINANCIAL_PLAN fp WHERE fp.UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String investorType = rs.getString("Investor_type");
                BigDecimal warningRate = rs.getBigDecimal("warning_loss_rate");
                BigDecimal curInvestProperty = rs.getBigDecimal("cur_invest_property");
                BigDecimal esProfit = rs.getBigDecimal("cur_es_profit");
                BigDecimal cumulativePnl = rs.getBigDecimal("cur_cumulative_pnl");
                lblInvestorType.setText("<html>Loại nhà đầu tư: <b>" + investorType + "</b></html>");
                lblCurInvestProperty.setText("Tài sản đầu tư hiện tại: " + curInvestProperty + " VND");
                lblEsProfit.setText("Lợi nhuận ước tính: " + esProfit + " VND");
                lblCumulativePnl.setText("Lợi nhuận tích lũy: " + cumulativePnl + " VND");
                lblWarningLossRate.setText("Tỷ lệ cảnh báo lỗ: " + warningRate.multiply(new BigDecimal("100")) + "%");
            } else {
                JPanel parent = (JPanel) lblInvestorType.getParent().getParent();
                parent.removeAll();
                parent.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(20, 20, 20, 20);
                JLabel info = new JLabel("Chưa có kế hoạch đầu tư. Vui lòng chọn loại nhà đầu tư để tạo mới:");
                info.setFont(new Font("Segoe UI", Font.BOLD, 24));
                parent.add(info, gbc);
                gbc.gridy++;
                JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Conservative Investor", "Balanced Investor", "Aggressive Investor"});
                typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                typeCombo.setPreferredSize(new Dimension(400, 40));
                parent.add(typeCombo, gbc);
                gbc.gridy++;
                JButton btnCreate = new JButton("Tạo kế hoạch đầu tư");
                btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 16));
                btnCreate.setBackground(new Color(0x2E2E5D));
                btnCreate.setForeground(Color.WHITE);
                btnCreate.setFocusPainted(false);
                btnCreate.setBorderPainted(false);
                btnCreate.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnCreate.setPreferredSize(new Dimension(300, 50));
                parent.add(btnCreate, gbc);
                btnCreate.addActionListener(e -> {
                    try {
                        String investorType = (String) typeCombo.getSelectedItem();
                        String insertSql = "INSERT INTO FINANCIAL_PLAN (UserID, Investor_type, cur_invest_property, cur_es_profit, cur_cumulative_pnl, warning_loss_rate) VALUES (?, ?, 0, 0, 0, 0)";
                        PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                        insertStmt.setInt(1, userId);
                        insertStmt.setString(2, investorType);
                        insertStmt.executeUpdate();
                        parent.removeAll();
                        parent.setLayout(new BorderLayout());
                        initializeUI();
                        parent.revalidate();
                        parent.repaint();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Lỗi khi tạo kế hoạch đầu tư: " + ex.getMessage());
                    }
                });
                parent.revalidate();
                parent.repaint();
            }
        } catch (SQLException e) {
            lblInvestorType.setText("Lỗi: " + e.getMessage());
        }
    }

    private void loadAllocationTable() {
        allocationModel.setRowCount(0);
        try {
            String sql = "SELECT fp.Investor_type, ar.Lv1_rate, ar.Lv2_rate, ar.Lv3_rate FROM FINANCIAL_PLAN fp JOIN ALLOCATION_RATIO ar ON fp.Investor_type = ar.Investor_type WHERE fp.UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal lv1Target = rs.getBigDecimal("Lv1_rate");
                BigDecimal lv2Target = rs.getBigDecimal("Lv2_rate");
                BigDecimal lv3Target = rs.getBigDecimal("Lv3_rate");

                BigDecimal totalValue = BigDecimal.ZERO;
                InvestStorageDAOImpl investDAO = new InvestStorageDAOImpl(connection);
                
                // Calculate total value for all risk levels including level 4
                for (int i = 1; i <= 4; i++) {
                    BigDecimal value = investDAO.getTotalInvestValueByRiskLevel(userId, i);
                    if (value != null) {
                        totalValue = totalValue.add(value);
                    }
                }

                // Add rows for each risk level
                for (int i = 1; i <= 4; i++) {
                    BigDecimal targetRate;
                    if (i <= 3) {
                        targetRate = i == 1 ? lv1Target : (i == 2 ? lv2Target : lv3Target);
                    } else {
                        targetRate = BigDecimal.ZERO; // Level 4 should have 0% target
                    }
                    
                    BigDecimal currentValue = investDAO.getTotalInvestValueByRiskLevel(userId, i);
                    if (currentValue == null) currentValue = BigDecimal.ZERO;
                    
                    BigDecimal actualRate = totalValue.compareTo(BigDecimal.ZERO) > 0 ? 
                        currentValue.divide(totalValue, 4, BigDecimal.ROUND_HALF_UP) : 
                        BigDecimal.ZERO;
                    
                    Vector<Object> row = new Vector<>();
                    row.add("Mức " + i);
                    row.add(String.format("%.2f%%", targetRate.multiply(new BigDecimal("100"))));
                    row.add(String.format("%,.0f VND", currentValue));
                    row.add(String.format("%.2f%%", actualRate.multiply(new BigDecimal("100"))));
                    allocationModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu phân bổ: " + e.getMessage());
        }
    }

    private void checkAllocationStatus() {
        try {
            String sql = "SELECT fp.Investor_type, ar.Lv1_rate, ar.Lv2_rate, ar.Lv3_rate FROM FINANCIAL_PLAN fp JOIN ALLOCATION_RATIO ar ON fp.Investor_type = ar.Investor_type WHERE fp.UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal lv1Target = rs.getBigDecimal("Lv1_rate");
                BigDecimal lv2Target = rs.getBigDecimal("Lv2_rate");
                BigDecimal lv3Target = rs.getBigDecimal("Lv3_rate");

                BigDecimal totalValue = BigDecimal.ZERO;
                InvestStorageDAOImpl investDAO = new InvestStorageDAOImpl(connection);
                
                // Calculate total value including level 4
                for (int i = 1; i <= 4; i++) {
                    BigDecimal value = investDAO.getTotalInvestValueByRiskLevel(userId, i);
                    if (value != null) {
                        totalValue = totalValue.add(value);
                    }
                }

                boolean isBalanced = true;
                StringBuilder statusText = new StringBuilder();
                statusText.append("<html>Trạng thái phân bổ: ");

                // Check all risk levels including level 4
                for (int i = 1; i <= 4; i++) {
                    BigDecimal targetRate;
                    if (i <= 3) {
                        targetRate = i == 1 ? lv1Target : (i == 2 ? lv2Target : lv3Target);
                    } else {
                        targetRate = BigDecimal.ZERO; // Level 4 should have 0% target
                    }
                    
                    BigDecimal currentValue = investDAO.getTotalInvestValueByRiskLevel(userId, i);
                    if (currentValue == null) currentValue = BigDecimal.ZERO;
                    
                    BigDecimal actualRate = totalValue.compareTo(BigDecimal.ZERO) > 0 ? 
                        currentValue.divide(totalValue, 4, BigDecimal.ROUND_HALF_UP) : 
                        BigDecimal.ZERO;
                    
                    BigDecimal difference = actualRate.subtract(targetRate);
                    BigDecimal absDifference = difference.abs();
                    
                    // For level 4, any non-zero value is considered unbalanced
                    if (i == 4 && actualRate.compareTo(BigDecimal.ZERO) > 0) {
                        isBalanced = false;
                        statusText.append("<br>Mức 4: Không nên có tài sản ở mức rủi ro này");
                    }
                    // For other levels, check if difference is more than 5%
                    else if (i <= 3 && absDifference.compareTo(new BigDecimal("0.05")) > 0) {
                        isBalanced = false;
                        statusText.append("<br>Mức ").append(i).append(": Chênh lệch ")
                                .append(difference.compareTo(BigDecimal.ZERO) > 0 ? "+" : "")
                                .append(String.format("%.2f%%", difference.multiply(new BigDecimal("100"))));
                    }
                }

                if (isBalanced) {
                    statusText.append("<br><font color='green'>✓ Phân bổ tài sản đang cân bằng</font></html>");
                    statusLabel.setForeground(new Color(0x4CAF50));
                } else {
                    statusText.append("<br><font color='orange'>⚠ Cần điều chỉnh phân bổ tài sản</font></html>");
                    statusLabel.setForeground(new Color(0xFF9800));
                }
                
                statusLabel.setText(statusText.toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra trạng thái phân bổ: " + e.getMessage());
        }
    }
} 