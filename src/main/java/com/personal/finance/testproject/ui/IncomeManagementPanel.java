package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;

public class IncomeManagementPanel extends JPanel {
    private int userId;
    private Connection connection;
    private JTable incomeTable;
    private DefaultTableModel tableModel;

    public IncomeManagementPanel(int userId) {
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

        // Create table model
        String[] columnNames = {"ID", "Tháng", "Tên khoản thu", "Số tiền", "Số dư còn lại"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        incomeTable = new JTable(tableModel);
        incomeTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        incomeTable.setRowHeight(32);
        incomeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        incomeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(incomeTable);
        scrollPane.setPreferredSize(new Dimension(750, 220));

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Month selection
        JLabel lblMonth = new JLabel("Tháng:");
        JComboBox<String> cmbMonth = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(lblMonth, gbc);
        gbc.gridx = 1;
        inputPanel.add(cmbMonth, gbc);

        // Income name
        JLabel lblName = new JLabel("Tên khoản thu:");
        JTextField txtName = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(lblName, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtName, gbc);

        // Amount
        JLabel lblAmount = new JLabel("Số tiền:");
        JTextField txtAmount = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(lblAmount, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtAmount, gbc);

        // Add button
        JButton btnAdd = new JButton("Thêm khoản thu");
        btnAdd.setBackground(new Color(0x2E2E5D));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(btnAdd, gbc);

        // Add action listener
        btnAdd.addActionListener(e -> {
            try {
                String month = (String) cmbMonth.getSelectedItem();
                String name = txtName.getText().trim();
                BigDecimal amount = new BigDecimal(txtAmount.getText().trim());

                if (name.isEmpty() || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hợp lệ");
                    return;
                }

                // Insert new income record
                String sql = "INSERT INTO INCOME (UserID, ic_month, income_name, income_amount) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, month);
                stmt.setString(3, name);
                stmt.setBigDecimal(4, amount);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Thêm khoản thu thành công");
                
                // Clear input fields
                txtName.setText("");
                txtAmount.setText("");
                
                // Refresh table
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm khoản thu: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ");
            }
        });

        // Canh giữa inputPanel và bảng
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerGbc.anchor = GridBagConstraints.CENTER;
        outerGbc.insets = new Insets(20, 0, 20, 0);
        add(inputPanel, outerGbc);
        outerGbc.gridy = 1;
        outerGbc.weightx = 1.0;
        outerGbc.weighty = 1.0;
        outerGbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, outerGbc);

        // Load initial data
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            String sql = "SELECT * FROM INCOME WHERE UserID = ? ORDER BY ic_month, IncomeID";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("IncomeID"));
                row.add(rs.getString("ic_month"));
                row.add(rs.getString("income_name"));
                row.add(rs.getBigDecimal("income_amount"));
                row.add(rs.getBigDecimal("remain_income"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
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