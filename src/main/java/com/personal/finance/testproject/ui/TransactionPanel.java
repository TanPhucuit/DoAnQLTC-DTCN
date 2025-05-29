package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;

public class TransactionPanel extends JPanel {
    private final int userId;
    private Connection connection;

    public TransactionPanel(int userId) {
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
        setLayout(new GridBagLayout());

        // --- Panel thêm giao dịch (bên trái) ---
        JPanel addPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAdd = new GridBagConstraints();
        gbcAdd.insets = new Insets(5, 5, 5, 5);
        gbcAdd.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblType = new JLabel("Loại giao dịch:");
        JComboBox<String> typeCombo = new JComboBox<>();
        try {
            String sql = "SELECT type_description FROM TYPE WHERE UserID = ? AND TypeID NOT IN ('InSt_Buy','InSt_Sell','loan_pay','loan_fee_pay')";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                typeCombo.addItem(rs.getString("type_description"));
            }
        } catch (SQLException e) {
            typeCombo.addItem("Lỗi tải loại giao dịch");
        }
        gbcAdd.gridx = 0; gbcAdd.gridy = 0;
        addPanel.add(lblType, gbcAdd);
        gbcAdd.gridx = 1;
        addPanel.add(typeCombo, gbcAdd);

        JLabel lblSource = new JLabel("Nguồn tiền:");
        JComboBox<String> sourceCombo = new JComboBox<>(new String[]{"Lương", "Phụ cấp"});
        gbcAdd.gridx = 0; gbcAdd.gridy = 1;
        addPanel.add(lblSource, gbcAdd);
        gbcAdd.gridx = 1;
        addPanel.add(sourceCombo, gbcAdd);

        JLabel lblAmount = new JLabel("Số tiền:");
        JTextField amountField = new JTextField(10);
        gbcAdd.gridx = 0; gbcAdd.gridy = 2;
        addPanel.add(lblAmount, gbcAdd);
        gbcAdd.gridx = 1;
        addPanel.add(amountField, gbcAdd);

        JLabel lblDate = new JLabel("Ngày giao dịch:");
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        gbcAdd.gridx = 0; gbcAdd.gridy = 3;
        addPanel.add(lblDate, gbcAdd);
        gbcAdd.gridx = 1;
        addPanel.add(dateChooser, gbcAdd);

        JLabel lblNote = new JLabel("Ghi chú:");
        JTextField noteField = new JTextField(20);
        gbcAdd.gridx = 0; gbcAdd.gridy = 4;
        addPanel.add(lblNote, gbcAdd);
        gbcAdd.gridx = 1;
        addPanel.add(noteField, gbcAdd);

        JButton btnAdd = new JButton("Thêm giao dịch");
        btnAdd.setBackground(new Color(0x2E2E5D));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcAdd.gridx = 0; gbcAdd.gridy = 5;
        gbcAdd.gridwidth = 2;
        addPanel.add(btnAdd, gbcAdd);

        // --- Panel tìm kiếm giao dịch (bên phải) ---
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.insets = new Insets(5, 5, 5, 5);
        gbcSearch.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblSearchType = new JLabel("Loại giao dịch:");
        JComboBox<String> cbSearchType = new JComboBox<>();
        cbSearchType.addItem("Tất cả");
        try {
            String sql = "SELECT DISTINCT type_description FROM TYPE WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cbSearchType.addItem(rs.getString("type_description"));
            }
        } catch (SQLException e) {
            cbSearchType.addItem("Lỗi tải loại giao dịch");
        }
        gbcSearch.gridx = 0; gbcSearch.gridy = 0;
        searchPanel.add(lblSearchType, gbcSearch);
        gbcSearch.gridx = 1;
        searchPanel.add(cbSearchType, gbcSearch);

        JLabel lblSearchAmount = new JLabel("Số tiền:");
        JComboBox<String> cbSearchAmount = new JComboBox<>(new String[]{
            "Tất cả", "<1.000.000", "1.000.000–2.000.000", "2.000.000–5.000.000", ">5.000.000"
        });
        gbcSearch.gridx = 0; gbcSearch.gridy = 1;
        searchPanel.add(lblSearchAmount, gbcSearch);
        gbcSearch.gridx = 1;
        searchPanel.add(cbSearchAmount, gbcSearch);

        JLabel lblSearchDate = new JLabel("Ngày giao dịch:");
        JComboBox<String> cbSearchDate = new JComboBox<>();
        cbSearchDate.addItem("Tất cả");
        try {
            String sql = "SELECT DISTINCT trans_date FROM TRANSACTION WHERE UserID = ? ORDER BY trans_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cbSearchDate.addItem(rs.getString("trans_date"));
            }
        } catch (SQLException e) {
            cbSearchDate.addItem("Lỗi tải ngày");
        }
        gbcSearch.gridx = 0; gbcSearch.gridy = 2;
        searchPanel.add(lblSearchDate, gbcSearch);
        gbcSearch.gridx = 1;
        searchPanel.add(cbSearchDate, gbcSearch);

        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(0x2E2E5D));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcSearch.gridx = 0; gbcSearch.gridy = 3;
        gbcSearch.gridwidth = 2;
        searchPanel.add(btnSearch, gbcSearch);

        // --- Gộp hai panel vào formPanel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcFormPanel = new GridBagConstraints();
        gbcFormPanel.insets = new Insets(0, 0, 0, 0);
        gbcFormPanel.gridx = 0; gbcFormPanel.gridy = 0;
        gbcFormPanel.weightx = 0.5;
        gbcFormPanel.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(addPanel, gbcFormPanel);
        gbcFormPanel.gridx = 1;
        formPanel.add(searchPanel, gbcFormPanel);

        // Canh giữa formPanel và bảng
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);
        add(formPanel, gbc);

        // Bảng giao dịch
        String[] columnNames = {"ID", "Loại giao dịch", "Số tiền", "Ngày giao dịch", "Ghi chú"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 220));
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        // Load dữ liệu giao dịch
        loadTransactionData(model);
        btnAdd.addActionListener(e -> {
            try {
                String typeDesc = (String)typeCombo.getSelectedItem();
                String typeId = "";
                String sqlType = "SELECT TypeID FROM TYPE WHERE UserID = ? AND type_description = ?";
                PreparedStatement stmtType = connection.prepareStatement(sqlType);
                stmtType.setInt(1, userId);
                stmtType.setString(2, typeDesc);
                ResultSet rsType = stmtType.executeQuery();
                if (rsType.next()) typeId = rsType.getString("TypeID");
                String source = (String)sourceCombo.getSelectedItem();
                java.util.Date utilDate = dateChooser.getDate();
                if (utilDate == null) throw new Exception("Vui lòng chọn ngày giao dịch");
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                String month = String.valueOf(utilDate.getMonth() + 1);
                String sqlIncome = "SELECT IncomeID, remain_income FROM INCOME WHERE UserID = ? AND ic_month = ? AND income_name = ? ORDER BY IncomeID LIMIT 1";
                PreparedStatement stmtIncome = connection.prepareStatement(sqlIncome);
                stmtIncome.setInt(1, userId);
                stmtIncome.setString(2, month);
                stmtIncome.setString(3, source);
                ResultSet rsIncome = stmtIncome.executeQuery();
                if (!rsIncome.next()) {
                    throw new Exception("Không tìm thấy thu nhập " + source + " trong tháng " + month);
                }
                int incomeId = rsIncome.getInt("IncomeID");
                BigDecimal remain = rsIncome.getBigDecimal("remain_income");
                BigDecimal amount = new BigDecimal(amountField.getText().trim());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new Exception("Số tiền phải lớn hơn 0");
                }
                if (remain.compareTo(amount) < 0) {
                    throw new Exception("Số dư nguồn tiền không đủ");
                }
                // Trừ remain_income
                String sqlUpdate = "UPDATE INCOME SET remain_income = remain_income - ? WHERE IncomeID = ? AND UserID = ?";
                PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate);
                stmtUpdate.setBigDecimal(1, amount);
                stmtUpdate.setInt(2, incomeId);
                stmtUpdate.setInt(3, userId);
                stmtUpdate.executeUpdate();
                // Thêm transaction
                String sql = "INSERT INTO TRANSACTION (UserID, TypeID, trans_amount, trans_date, IncomeID) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, typeId);
                stmt.setBigDecimal(3, amount);
                stmt.setDate(4, sqlDate);
                stmt.setInt(5, incomeId);
                stmt.executeUpdate();
                loadTransactionData(model);
                amountField.setText("");
                dateChooser.setDate(null);
                noteField.setText("");
                JOptionPane.showMessageDialog(this, "Thêm giao dịch thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm giao dịch: " + ex.getMessage());
            }
        });
        btnSearch.addActionListener(e -> {
            // Lọc dữ liệu bảng theo điều kiện tìm kiếm
            String type = (String) cbSearchType.getSelectedItem();
            String amount = (String) cbSearchAmount.getSelectedItem();
            String date = (String) cbSearchDate.getSelectedItem();
            filterTransactionData(model, type, amount, date);
        });
    }

    private void loadTransactionData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT t.TransID, ty.type_description, t.trans_amount, t.trans_date, " +
                        "CASE " +
                        "   WHEN t.LoanID IS NOT NULL THEN 'Thanh toán khoản vay' " +
                        "   WHEN t.InStID IS NOT NULL THEN 'Giao dịch đầu tư' " +
                        "   WHEN t.IncomeID IS NOT NULL THEN 'Thu nhập' " +
                        "   WHEN t.SaveID IS NOT NULL THEN 'Tiết kiệm' " +
                        "   WHEN t.OverPayFeeID IS NOT NULL THEN 'Phí trả chậm' " +
                        "   ELSE ty.type_description " +
                        "END as note " +
                        "FROM TRANSACTION t " +
                        "LEFT JOIN TYPE ty ON t.TypeID = ty.TypeID AND t.UserID = ty.UserID " +
                        "WHERE t.UserID = ? ORDER BY t.trans_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("TransID"));
                row.add(rs.getString("type_description"));
                row.add(rs.getBigDecimal("trans_amount"));
                row.add(rs.getDate("trans_date"));
                row.add(rs.getString("note"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu giao dịch: " + e.getMessage());
        }
    }

    private void filterTransactionData(DefaultTableModel model, String type, String amount, String date) {
        model.setRowCount(0);
        try {
            StringBuilder sql = new StringBuilder("SELECT t.TransID, ty.type_description, t.trans_amount, t.trans_date, " +
                        "CASE " +
                        "   WHEN t.LoanID IS NOT NULL THEN 'Thanh toán khoản vay' " +
                        "   WHEN t.InStID IS NOT NULL THEN 'Giao dịch đầu tư' " +
                        "   WHEN t.IncomeID IS NOT NULL THEN 'Thu nhập' " +
                        "   WHEN t.SaveID IS NOT NULL THEN 'Tiết kiệm' " +
                        "   WHEN t.OverPayFeeID IS NOT NULL THEN 'Phí trả chậm' " +
                        "   ELSE ty.type_description " +
                        "END as note " +
                        "FROM TRANSACTION t " +
                        "LEFT JOIN TYPE ty ON t.TypeID = ty.TypeID AND t.UserID = ty.UserID " +
                        "WHERE t.UserID = ?");
            if (!"Tất cả".equals(type)) {
                sql.append(" AND ty.type_description = ?");
            }
            if (!"Tất cả".equals(amount)) {
                if ("<1.000.000".equals(amount)) {
                    sql.append(" AND t.trans_amount < 1000000");
                } else if ("1.000.000–2.000.000".equals(amount)) {
                    sql.append(" AND t.trans_amount >= 1000000 AND t.trans_amount <= 2000000");
                } else if ("2.000.000–5.000.000".equals(amount)) {
                    sql.append(" AND t.trans_amount > 2000000 AND t.trans_amount <= 5000000");
                } else if (">5.000.000".equals(amount)) {
                    sql.append(" AND t.trans_amount > 5000000");
                }
            }
            if (!"Tất cả".equals(date)) {
                sql.append(" AND t.trans_date = ?");
            }
            sql.append(" ORDER BY t.trans_date DESC");
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            int idx = 1;
            stmt.setInt(idx++, userId);
            if (!"Tất cả".equals(type)) stmt.setString(idx++, type);
            if (!"Tất cả".equals(date)) stmt.setString(idx++, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("TransID"));
                row.add(rs.getString("type_description"));
                row.add(rs.getBigDecimal("trans_amount"));
                row.add(rs.getDate("trans_date"));
                row.add(rs.getString("note"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm giao dịch: " + e.getMessage());
        }
    }
} 