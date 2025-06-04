package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;

public class SavingManagementPanel extends JPanel {
    private int userId;
    private Connection connection;
    private JTable savingTable;
    private DefaultTableModel tableModel;
    private ManageSearchFrame parentFrame;

    public SavingManagementPanel(int userId, ManageSearchFrame parentFrame) {
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
        setLayout(new GridBagLayout());

        // Create table model
        String[] columnNames = {"ID", "Tên khoản tiết kiệm", "Mục đích", " Số tài khoản ", 
                              "Số tiền", "Số dư còn lại", "Ngày cập nhật", "Ghi chú"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        savingTable = new JTable(tableModel);
        savingTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        savingTable.setRowHeight(32);
        savingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        savingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(savingTable);
        scrollPane.setPreferredSize(new Dimension(750, 220));

        // Create input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Purpose selection
        JLabel lblPurpose = new JLabel("Mục đích:");
        JComboBox<String> cmbPurpose = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(lblPurpose, gbc);
        gbc.gridx = 1;
        inputPanel.add(cmbPurpose, gbc);

        // Bank account selection
        JLabel lblBankAccount = new JLabel("Số tài khoản :");
        JComboBox<String> cmbBankAccount = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(lblBankAccount, gbc);
        gbc.gridx = 1;
        inputPanel.add(cmbBankAccount, gbc);

        // Saving name
        JLabel lblName = new JLabel("Tên khoản tiết kiệm:");
        JTextField txtName = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(lblName, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtName, gbc);

        // Amount
        JLabel lblAmount = new JLabel("Số tiền:");
        JTextField txtAmount = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(lblAmount, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtAmount, gbc);

        // Description
        JLabel lblDescription = new JLabel("Ghi chú:");
        JTextField txtDescription = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(lblDescription, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtDescription, gbc);

        // Add button
        JButton btnAdd = new JButton("Thêm khoản tiết kiệm");
        btnAdd.setBackground(new Color(0x008BCF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        inputPanel.add(btnAdd, gbc);

        // Load purposes and bank accounts
        loadPurposes(cmbPurpose);
        loadBankAccounts(cmbBankAccount);

        // Add action listener
        btnAdd.addActionListener(e -> {
            try {
                String purposeId = ((String) cmbPurpose.getSelectedItem()).split(" - ")[0];
                String bankAccount = (String) cmbBankAccount.getSelectedItem();
                String name = txtName.getText().trim();
                BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
                String description = txtDescription.getText().trim();

                if (name.isEmpty() || amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hợp lệ");
                    return;
                }

                // Insert new saving record
                String sql = "INSERT INTO SAVING (UserID, PurposeID, BankAccountNumber, save_name, " +
                           "save_amount, description, up_date) VALUES (?, ?, ?, ?, ?, ?, CURDATE())";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setInt(2, Integer.parseInt(purposeId));
                stmt.setString(3, bankAccount);
                stmt.setString(4, name);
                stmt.setBigDecimal(5, amount);
                stmt.setString(6, description);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Thêm khoản tiết kiệm thành công");
                
                // Clear input fields
                txtName.setText("");
                txtAmount.setText("");
                txtDescription.setText("");
                
                // Refresh table
                refreshTable();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm khoản tiết kiệm: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ");
            }
        });

        // Thêm nút quay lại ở đầu constructor hoặc initializeUI
        JButton btnBack = new JButton("← Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(new Color(0x008BCF));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.showDashboard();
            }
        });
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(0xF5F5F5));
        backPanel.add(btnBack);
        // Thêm backPanel vào dòng đầu tiên
        GridBagConstraints backGbc = new GridBagConstraints();
        backGbc.gridx = 0;
        backGbc.gridy = 0;
        backGbc.anchor = GridBagConstraints.WEST;
        backGbc.insets = new Insets(0, 0, 0, 0);
        add(backPanel, backGbc);
        // Đẩy các thành phần khác xuống
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.gridx = 0;
        outerGbc.gridy = 1;
        outerGbc.anchor = GridBagConstraints.CENTER;
        outerGbc.insets = new Insets(20, 0, 20, 0);
        add(inputPanel, outerGbc);
        outerGbc.gridy = 2;
        outerGbc.weightx = 1.0;
        outerGbc.weighty = 1.0;
        outerGbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, outerGbc);

        // Load initial data
        refreshTable();
    }

    private void loadPurposes(JComboBox<String> cmbPurpose) {
        try {
            String sql = "SELECT PurposeID, purpose_name FROM PURPOSE WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cmbPurpose.addItem(rs.getInt("PurposeID") + " - " + rs.getString("purpose_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách mục đích: " + e.getMessage());
        }
    }

    private void loadBankAccounts(JComboBox<String> cmbBankAccount) {
        try {
            String sql = "SELECT BankAccountNumber FROM BANK_ACCOUNT WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cmbBankAccount.addItem(rs.getString("BankAccountNumber"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách tài khoản: " + e.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            String sql = "SELECT s.*, p.purpose_name, b.BankName " +
                        "FROM SAVING s " +
                        "JOIN PURPOSE p ON s.PurposeID = p.PurposeID AND s.UserID = p.UserID " +
                        "JOIN BANK_ACCOUNT b ON s.BankAccountNumber = b.BankAccountNumber " +
                        "WHERE s.UserID = ? " +
                        "ORDER BY s.up_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("SaveID"));
                row.add(rs.getString("save_name"));
                row.add(rs.getString("purpose_name"));
                row.add(rs.getString("BankAccountNumber") + " - " + rs.getString("BankName"));
                row.add(rs.getBigDecimal("save_amount"));
                row.add(rs.getBigDecimal("remain_save"));
                row.add(rs.getDate("up_date"));
                row.add(rs.getString("description"));
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