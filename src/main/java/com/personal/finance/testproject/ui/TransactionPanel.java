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
import javax.swing.Box;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TransactionPanel extends JPanel {
    private final int userId;
    private Connection connection;
  private ManageSearchFrame parentFrame;
    public TransactionPanel(int userId, ManageSearchFrame parentFrame) {
        this.userId = userId;
        this.parentFrame = parentFrame;
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
        setBackground(new Color(0xF5F5F5));
        // Nút quay lại
        JButton btnBack = new JButton("← Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(new Color(0x008BCF));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            if (parentFrame != null) parentFrame.showDashboard();
        });
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(0xF5F5F5));
        backPanel.add(btnBack);
        add(backPanel, BorderLayout.NORTH);

        // Panel thêm giao dịch (bên trái)
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBackground(Color.WHITE);
        addPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        addPanel.setMaximumSize(new Dimension(350, 400));
        addPanel.setPreferredSize(new Dimension(340, 400));
        GridBagConstraints gbcAdd = new GridBagConstraints();
        gbcAdd.insets = new Insets(6, 6, 6, 6);
        gbcAdd.anchor = GridBagConstraints.EAST;
        gbcAdd.fill = GridBagConstraints.HORIZONTAL;
        int addRow = 0;
        // Loại giao dịch (lấy từ DB TYPE)
        JLabel lblType = new JLabel("Loại giao dịch:");
        lblType.setPreferredSize(new Dimension(110, 28));
        JComboBox<String> cbType = new JComboBox<>();
        cbType.setPreferredSize(new Dimension(160, 28));
        try {
            String sql = "SELECT DISTINCT type_description FROM TYPE WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) cbType.addItem(rs.getString("type_description"));
        } catch (SQLException e) { cbType.addItem("Lỗi tải loại giao dịch"); }
        gbcAdd.gridx = 0; gbcAdd.gridy = addRow; addPanel.add(lblType, gbcAdd);
        gbcAdd.gridx = 1; addPanel.add(cbType, gbcAdd);
        addRow++;
        // Nguồn tiền
        JLabel lblSource = new JLabel("Nguồn tiền:");
        lblSource.setPreferredSize(new Dimension(110, 28));
        JComboBox<String> cbSource = new JComboBox<>(new String[]{"Lương", "Phụ cấp"});
        cbSource.setPreferredSize(new Dimension(160, 28));
        gbcAdd.gridx = 0; gbcAdd.gridy = addRow; addPanel.add(lblSource, gbcAdd);
        gbcAdd.gridx = 1; addPanel.add(cbSource, gbcAdd);
        addRow++;
        // Số tiền
        JLabel lblAmount = new JLabel("Số tiền:");
        lblAmount.setPreferredSize(new Dimension(110, 28));
        JTextField tfAmount = new JTextField();
        tfAmount.setPreferredSize(new Dimension(160, 28));
        gbcAdd.gridx = 0; gbcAdd.gridy = addRow; addPanel.add(lblAmount, gbcAdd);
        gbcAdd.gridx = 1; addPanel.add(tfAmount, gbcAdd);
        addRow++;
        // Ngày giao dịch
        JLabel lblDate = new JLabel("Ngày giao dịch:");
        lblDate.setPreferredSize(new Dimension(110, 28));
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setPreferredSize(new Dimension(160, 28));
        dateChooser.setDateFormatString("yyyy-MM-dd");
        gbcAdd.gridx = 0; gbcAdd.gridy = addRow; addPanel.add(lblDate, gbcAdd);
        gbcAdd.gridx = 1; addPanel.add(dateChooser, gbcAdd);
        addRow++;
        // Ghi chú
        JLabel lblNote = new JLabel("Ghi chú:");
        lblNote.setPreferredSize(new Dimension(110, 28));
        JTextField tfNote = new JTextField();
        tfNote.setPreferredSize(new Dimension(160, 28));
        gbcAdd.gridx = 0; gbcAdd.gridy = addRow; addPanel.add(lblNote, gbcAdd);
        gbcAdd.gridx = 1; addPanel.add(tfNote, gbcAdd);
        addRow++;
        // Nút thêm giao dịch
        JButton btnAdd = new JButton("Thêm giao dịch");
        btnAdd.setBackground(new Color(0x008BCF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(180, 32));
        gbcAdd.gridx = 0; gbcAdd.gridy = addRow; gbcAdd.gridwidth = 2; gbcAdd.anchor = GridBagConstraints.CENTER;
        addPanel.add(btnAdd, gbcAdd);
        gbcAdd.gridwidth = 1;

        // Panel tìm kiếm (bên phải)
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        searchPanel.setMaximumSize(new Dimension(350, 400));
        searchPanel.setPreferredSize(new Dimension(340, 400));
        GridBagConstraints gbcSearch = new GridBagConstraints();
        gbcSearch.insets = new Insets(6, 6, 6, 6);
        gbcSearch.anchor = GridBagConstraints.EAST;
        gbcSearch.fill = GridBagConstraints.NONE;
        gbcSearch.weightx = 0;
        int searchRow = 0;
        // Loại giao dịch (filter, lấy từ DB TYPE)
        JLabel lblTypeFilter = new JLabel("Loại giao dịch:");
        lblTypeFilter.setPreferredSize(new Dimension(110, 28));
        JComboBox<String> cbTypeFilter = new JComboBox<>();
        cbTypeFilter.addItem("Tất cả");
        try {
            String sql = "SELECT DISTINCT type_description FROM TYPE WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) cbTypeFilter.addItem(rs.getString("type_description"));
        } catch (SQLException e) { cbTypeFilter.addItem("Lỗi tải loại giao dịch"); }
        cbTypeFilter.setPreferredSize(new Dimension(160, 28));
        gbcSearch.gridx = 0; gbcSearch.gridy = searchRow; searchPanel.add(lblTypeFilter, gbcSearch);
        gbcSearch.gridx = 1; gbcSearch.anchor = GridBagConstraints.WEST; searchPanel.add(cbTypeFilter, gbcSearch);
        searchRow++;
        // Số tiền
        JLabel lblAmountFilter = new JLabel("Số tiền:");
        lblAmountFilter.setPreferredSize(new Dimension(110, 28));
        JComboBox<String> cbAmountFilter = new JComboBox<>(getAmountFilterList());
        cbAmountFilter.setPreferredSize(new Dimension(180, 28)); cbAmountFilter.setMaximumSize(new Dimension(180, 28));
        gbcSearch.gridx = 0; gbcSearch.gridy = searchRow; gbcSearch.anchor = GridBagConstraints.EAST; searchPanel.add(lblAmountFilter, gbcSearch);
        gbcSearch.gridx = 1; gbcSearch.anchor = GridBagConstraints.WEST; searchPanel.add(cbAmountFilter, gbcSearch);
        searchRow++;
        // Tháng giao dịch
        JLabel lblMonthFilter = new JLabel("Tháng giao dịch:");
        lblMonthFilter.setPreferredSize(new Dimension(110, 28));
        JComboBox<String> cbMonthFilter = new JComboBox<>(getMonthFilterList());
        cbMonthFilter.setPreferredSize(new Dimension(180, 28)); cbMonthFilter.setMaximumSize(new Dimension(180, 28));
        gbcSearch.gridx = 0; gbcSearch.gridy = searchRow; gbcSearch.anchor = GridBagConstraints.EAST; searchPanel.add(lblMonthFilter, gbcSearch);
        gbcSearch.gridx = 1; gbcSearch.anchor = GridBagConstraints.WEST; searchPanel.add(cbMonthFilter, gbcSearch);
        searchRow++;
        // Nút tìm kiếm
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(0x2E2E5D));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setPreferredSize(new Dimension(180, 32)); btnSearch.setMaximumSize(new Dimension(180, 32));
        gbcSearch.gridx = 0; gbcSearch.gridy = searchRow; gbcSearch.gridwidth = 2; gbcSearch.anchor = GridBagConstraints.CENTER;
        searchPanel.add(btnSearch, gbcSearch);
        gbcSearch.gridwidth = 1;

        // Bảng giao dịch (ở giữa)
        String[] columnNames = {"ID", "Loại giao dịch", "Số tiền", "Ngày giao dịch", "Ghi chú"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0x00AEEF));
        table.getTableHeader().setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 220));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xD3D3D3), 1, true));

        add(addPanel, BorderLayout.WEST);
        add(searchPanel, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);

        // Load dữ liệu ban đầu
        loadTransactionData(model);
        // Thêm giao dịch
        btnAdd.addActionListener(e -> {
            String typeDesc = cbType.getSelectedItem() != null ? cbType.getSelectedItem().toString() : "";
            String source = cbSource.getSelectedItem() != null ? cbSource.getSelectedItem().toString() : "";
            String amountStr = tfAmount.getText().trim();
            java.util.Date date = dateChooser.getDate();
            String note = tfNote.getText().trim();
            if (typeDesc.isEmpty() || source.isEmpty() || amountStr.isEmpty() || date == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }
            BigDecimal amount;
            try {
                amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0");
                    return;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ");
                return;
            }
            try {
                // Lấy TypeID từ TYPE
                String typeId = null;
                String sqlType = "SELECT TypeID FROM TYPE WHERE UserID = ? AND type_description = ?";
                PreparedStatement stmtType = connection.prepareStatement(sqlType);
                stmtType.setInt(1, userId);
                stmtType.setString(2, typeDesc);
                ResultSet rsType = stmtType.executeQuery();
                if (rsType.next()) typeId = rsType.getString("TypeID");
                if (typeId == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy loại giao dịch");
                    return;
                }
                // Xác định tháng giao dịch (ic_month)
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(date);
                int icMonth = cal.get(java.util.Calendar.MONTH) + 1;
                String monthStr = String.valueOf(icMonth);
                int sourceIncomeId = -1;
                // Tự động nhận diện nguồn tiền như logic mua tài sản đầu tư
                if (source.equals("Lương") || source.equals("Phụ cấp")) {
                    String sql = "SELECT IncomeID, remain_income FROM INCOME WHERE UserID = ? AND ic_month = ? AND income_name = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    stmt.setString(2, monthStr);
                    stmt.setString(3, source);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy nguồn " + source + " tháng " + monthStr);
                        return;
                    }
                    sourceIncomeId = rs.getInt("IncomeID");
                    BigDecimal remain = rs.getBigDecimal("remain_income");
                    if (remain.compareTo(amount) < 0) {
                        JOptionPane.showMessageDialog(this, "Số dư " + source + " không đủ");
                        return;
                    }
                    // Trừ tiền
                    String sqlUpdate = "UPDATE INCOME SET remain_income = remain_income - ? WHERE IncomeID = ?";
                    PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate);
                    stmtUpdate.setBigDecimal(1, amount);
                    stmtUpdate.setInt(2, sourceIncomeId);
                    stmtUpdate.executeUpdate();
                } else if (source.equals("Tiết kiệm")) {
                    String sql = "SELECT SaveID, remain_save FROM SAVING WHERE UserID = ? AND save_month = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    stmt.setString(2, monthStr);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy nguồn Tiết kiệm tháng " + monthStr);
                        return;
                    }
                    int saveId = rs.getInt("SaveID");
                    BigDecimal remain = rs.getBigDecimal("remain_save");
                    if (remain.compareTo(amount) < 0) {
                        JOptionPane.showMessageDialog(this, "Số dư Tiết kiệm không đủ");
                        return;
                    }
                    // Trừ tiền
                    String sqlUpdate = "UPDATE SAVING SET remain_save = remain_save - ? WHERE SaveID = ?";
                    PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate);
                    stmtUpdate.setBigDecimal(1, amount);
                    stmtUpdate.setInt(2, saveId);
                    stmtUpdate.executeUpdate();
                    sourceIncomeId = saveId;
                }
                // Thêm giao dịch vào TRANSACTION (dùng typeId)
                String sqlTrans = "INSERT INTO TRANSACTION (UserID, TypeID, trans_amount, trans_date, IncomeID) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmtTrans = connection.prepareStatement(sqlTrans);
                stmtTrans.setInt(1, userId);
                stmtTrans.setString(2, typeId);
                stmtTrans.setBigDecimal(3, amount);
                stmtTrans.setDate(4, new java.sql.Date(date.getTime()));
                if (sourceIncomeId != -1) stmtTrans.setInt(5, sourceIncomeId); else stmtTrans.setNull(5, java.sql.Types.INTEGER);
                stmtTrans.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm giao dịch thành công!");
                tfAmount.setText("");
                tfNote.setText("");
                dateChooser.setDate(null);
                loadTransactionData(model);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm giao dịch: " + ex.getMessage());
            }
        });
        // Tìm kiếm giao dịch
        btnSearch.addActionListener(e -> {
            // Lọc theo loại, số tiền, tháng giao dịch
            filterTransactionData(model, cbTypeFilter.getSelectedItem().toString(), cbAmountFilter.getSelectedItem().toString(), cbMonthFilter.getSelectedItem().toString());
        });
    }

    // Các hàm lấy danh sách cho combobox (giả lập, cần lấy từ DB nếu có)
    private String[] getSourceList() {
        return new String[]{"Lương", "Tiết kiệm", "Đầu tư", "Khác"};
    }
    private String[] getAmountFilterList() {
        return new String[]{"Tất cả", "<1.000.000", "1.000.000–2.000.000", "2.000.000–5.000.000", ">5.000.000"};
    }
    private String[] getMonthFilterList() {
        String[] months = new String[13];
        months[0] = "Tất cả";
        for (int i = 1; i <= 12; i++) months[i] = String.valueOf(i);
        return months;
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

    private void filterTransactionData(DefaultTableModel model, String type, String amount, String month) {
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
            if (!"Tất cả".equals(type)) sql.append(" AND ty.type_description = ?");
            if (!"Tất cả".equals(amount)) {
                if ("<1.000.000".equals(amount)) sql.append(" AND t.trans_amount < 1000000");
                else if ("1.000.000–2.000.000".equals(amount)) sql.append(" AND t.trans_amount >= 1000000 AND t.trans_amount <= 2000000");
                else if ("2.000.000–5.000.000".equals(amount)) sql.append(" AND t.trans_amount > 2000000 AND t.trans_amount <= 5000000");
                else if (">5.000.000".equals(amount)) sql.append(" AND t.trans_amount > 5000000");
            }
            if (!"Tất cả".equals(month)) sql.append(" AND MONTH(t.trans_date) = ?");
            sql.append(" ORDER BY t.trans_date DESC");
            PreparedStatement stmt = connection.prepareStatement(sql.toString());
            int idx = 1;
            stmt.setInt(idx++, userId);
            if (!"Tất cả".equals(type)) stmt.setString(idx++, type);
            if (!"Tất cả".equals(month)) stmt.setInt(idx++, Integer.parseInt(month));
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
            JOptionPane.showMessageDialog(this, "Lỗi khi lọc dữ liệu giao dịch: " + e.getMessage());
        }
    }
} 