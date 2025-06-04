package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;
import com.toedter.calendar.JDateChooser;

public class LoanManagementPanel extends JPanel {
    private int userId;
    private Connection connection;
    private JTable loanTable;
    private DefaultTableModel tableModel;
    private DefaultTableModel detailModel;
    private DefaultTableModel feeModel;
    private JComboBox<String> statusFilter;
    private ManageSearchFrame parentFrame;

    public LoanManagementPanel(int userId, ManageSearchFrame parentFrame) {
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
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Quản lý khoản vay");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

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
        backPanel.setBackground(Color.WHITE);
        backPanel.add(btnBack);
        add(backPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(0x2E2E5D));

        tabbedPane.addTab("Danh sách khoản vay", createLoanListPanel());
        tabbedPane.addTab("Chi tiết khoản vay", createLoanDetailPanel());
        tabbedPane.addTab("Phí trả chậm", createLoanFeePanel());
        // tabbedPane.addTab("Thanh toán khoản vay", createPayLoanPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createLoanListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Chỉ hiển thị các trường: ID, Mục đích, Loại khoản vay, Số tiền vay, Ngày giải ngân
        String[] columnNames = {"ID", "Mục đích", "Loại khoản vay", "Số tiền vay", "Ngày giải ngân"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loanTable = new JTable(tableModel);
        loanTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loanTable.setRowHeight(30);
        loanTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        loanTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(loanTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create input panel for adding new loan
        JPanel inputPanel = createInputPanel();
        panel.add(inputPanel, BorderLayout.NORTH);

        // Load initial data
        refreshTable();
        return panel;
    }

    private JPanel createLoanDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Thanh trên cùng với nút refresh
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setToolTipText("Lấy dữ liệu mới nhất");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBackground(new Color(0x008BCF));
        btnRefresh.setForeground(Color.WHITE);
        topPanel.add(btnRefresh);
        panel.add(topPanel, BorderLayout.NORTH);

        // Chỉ hiển thị các trường: ID, Lãi suất, Số kỳ, Số tiền/kỳ, Ngày trả, Số kỳ đã trả, Số tiền đã trả, Số tiền còn lại
        String[] detailColumns = {"ID", "Lãi suất", "Số kỳ", "Số tiền/kỳ", "Ngày trả", "Số kỳ đã trả", "Số tiền đã trả", "Số tiền còn lại"};
        detailModel = new DefaultTableModel(detailColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable detailTable = new JTable(detailModel);
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailTable.setRowHeight(30);
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Nút refresh cập nhật lại bảng chi tiết
        btnRefresh.addActionListener(e -> loadLoanDetails(detailModel));

        // Load loan details data
        loadLoanDetails(detailModel);
        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Thêm khoản vay mới"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Purpose selection
        JLabel lblPurpose = new JLabel("Mục đích:");
        JComboBox<String> cmbPurpose = new JComboBox<>();
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblPurpose, gbc);
        gbc.gridx = 1;
        panel.add(cmbPurpose, gbc);

        // Loan form selection
        JLabel lblForm = new JLabel("Loại khoản vay:");
        JComboBox<String> cmbForm = new JComboBox<>(new String[]{"installment_loan", "bullet_loan"});
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblForm, gbc);
        gbc.gridx = 1;
        panel.add(cmbForm, gbc);

        // Amount
        JLabel lblAmount = new JLabel("Số tiền vay:");
        JTextField txtAmount = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblAmount, gbc);
        gbc.gridx = 1;
        panel.add(txtAmount, gbc);

        // Interest rate
        JLabel lblInterest = new JLabel("Lãi suất (%):");
        JTextField txtInterest = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblInterest, gbc);
        gbc.gridx = 1;
        panel.add(txtInterest, gbc);

        // Number of terms
        JLabel lblTerms = new JLabel("Số kỳ:");
        JTextField txtTerms = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblTerms, gbc);
        gbc.gridx = 1;
        panel.add(txtTerms, gbc);

        // Payment date
        JLabel lblPaymentDate = new JLabel("Ngày trả hàng tháng:");
        JTextField txtPaymentDate = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lblPaymentDate, gbc);
        gbc.gridx = 1;
        panel.add(txtPaymentDate, gbc);

        // Add button
        JButton btnAdd = new JButton("Thêm khoản vay");
        btnAdd.setBackground(new Color(0x008BCF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnAdd, gbc);

        // Load purposes
        loadPurposes(cmbPurpose);

        // Add action listener
        btnAdd.addActionListener(e -> {
            try {
                String purposeId = ((String) cmbPurpose.getSelectedItem()).split(" - ")[0];
                String formId = (String) cmbForm.getSelectedItem();
                BigDecimal amount = new BigDecimal(txtAmount.getText().trim());
                BigDecimal interest = new BigDecimal(txtInterest.getText().trim()).divide(new BigDecimal("100"));
                int numTerms = Integer.parseInt(txtTerms.getText().trim());
                int paymentDate = Integer.parseInt(txtPaymentDate.getText().trim());

                if (amount.compareTo(BigDecimal.ZERO) <= 0 || interest.compareTo(BigDecimal.ZERO) <= 0 
                    || numTerms <= 0 || paymentDate < 1 || paymentDate > 31) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hợp lệ");
                    return;
                }

                // Insert new loan record
                String sql = "INSERT INTO LOAN (UserID, PurposeID, FormID, loan_amount, interest, " +
                           "num_term, date_of_payment, disbur_date) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setInt(2, Integer.parseInt(purposeId));
                stmt.setString(3, formId);
                stmt.setBigDecimal(4, amount);
                stmt.setBigDecimal(5, interest);
                stmt.setInt(6, numTerms);
                stmt.setInt(7, paymentDate);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Thêm khoản vay thành công");
                
                // Clear input fields
                txtAmount.setText("");
                txtInterest.setText("");
                txtTerms.setText("");
                txtPaymentDate.setText("");
                
                // Refresh tables
                refreshTable();
                loadLoanDetails(detailModel);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm khoản vay: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ");
            }
        });

        return panel;
    }

    private void loadLoanDetails(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT l.* FROM LOAN l WHERE l.UserID = ? ORDER BY l.disbur_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("LoanID"));
                row.add(rs.getBigDecimal("interest").multiply(new BigDecimal("100")));
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

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            String sql = "SELECT l.LoanID, p.purpose_name, f.description as form_description, l.loan_amount, l.disbur_date " +
                        "FROM LOAN l " +
                        "JOIN PURPOSE p ON l.PurposeID = p.PurposeID AND l.UserID = p.UserID " +
                        "JOIN FORM f ON l.FormID = f.FormID " +
                        "WHERE l.UserID = ? " +
                        "ORDER BY l.disbur_date DESC";
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
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private JPanel createLoanFeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        statusFilter = new JComboBox<>(new String[]{"Tất cả", "Chưa thanh toán", "Đã thanh toán"});
        statusFilter.setPreferredSize(new Dimension(150, 30));
        statusFilter.addActionListener(e -> refreshFeeTable());
        filterPanel.add(new JLabel("Trạng thái:"));
        filterPanel.add(statusFilter);
        panel.add(filterPanel, BorderLayout.NORTH);

        // Tạo bảng hiển thị phí trả chậm
        String[] columnNames = {"ID khoản vay", "Mục đích", "Kỳ hạn", "Số tiền phí", "Trạng thái", "Thanh toán"};
        feeModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Chỉ cho phép chỉnh sửa cột Thanh toán
            }
        };
        JTable feeTable = new JTable(feeModel);
        feeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feeTable.setRowHeight(30);
        feeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        feeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Thêm nút thanh toán vào cột cuối
        feeTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        feeTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), feeModel, statusFilter));

        JScrollPane scrollPane = new JScrollPane(feeTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load dữ liệu phí trả chậm
        loadLoanFees(feeModel, "Tất cả");
        return panel;
    }

    private void loadLoanFees(DefaultTableModel model, String status) {
        model.setRowCount(0);
        try {
            String sql = "SELECT opf.OverPayFeeID, opf.LoanID, p.purpose_name, opf.Term, opf.amount, opf.state " +
                        "FROM OVER_PAY_FEE opf " +
                        "JOIN LOAN l ON opf.LoanID = l.LoanID AND opf.UserID = l.UserID " +
                        "JOIN PURPOSE p ON l.PurposeID = p.PurposeID AND l.UserID = p.UserID " +
                        "WHERE opf.UserID = ? ";
            
            if (!status.equals("Tất cả")) {
                sql += "AND opf.state = ? ";
            }
            
            sql += "ORDER BY opf.LoanID, opf.Term";
            
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            if (!status.equals("Tất cả")) {
                stmt.setBoolean(2, status.equals("Đã thanh toán"));
            }
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("LoanID"));
                row.add(rs.getString("purpose_name"));
                row.add(rs.getInt("Term"));
                row.add(rs.getBigDecimal("amount"));
                row.add(rs.getBoolean("state") ? "Đã thanh toán" : "Chưa thanh toán");
                row.add(rs.getBoolean("state") ? "" : "Thanh toán");
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu phí trả chậm: " + e.getMessage());
        }
    }

    private void refreshFeeTable() {
        if (feeModel != null && statusFilter != null) {
            loadLoanFees(feeModel, (String) statusFilter.getSelectedItem());
        }
    }

    // ButtonRenderer cho nút Thanh toán
    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(0x008BCF));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // ButtonEditor cho nút Thanh toán
    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;
        private DefaultTableModel model;
        private JComboBox<String> statusFilter;

        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, JComboBox<String> statusFilter) {
            super(checkBox);
            this.model = model;
            this.statusFilter = statusFilter;
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(0x008BCF));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 15));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int loanId = (int) model.getValueAt(row, 0);
                String currentStatus = (String) model.getValueAt(row, 4);
                
                if (currentStatus.equals("Chưa thanh toán")) {
                    int confirm = JOptionPane.showConfirmDialog(
                        LoanManagementPanel.this,
                        "Bạn có chắc chắn muốn thanh toán phí trả chậm này?",
                        "Xác nhận thanh toán",
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            // Tạo giao dịch thanh toán phí
                            String sql = "INSERT INTO TRANSACTION (TypeID, UserID, trans_amount, trans_date, OverPayFeeID) " +
                                       "SELECT 'Loan_fee_pay', ?, amount, CURRENT_DATE, OverPayFeeID " +
                                       "FROM OVER_PAY_FEE " +
                                       "WHERE LoanID = ? AND UserID = ? AND state = FALSE";
                            PreparedStatement stmt = connection.prepareStatement(sql);
                            stmt.setInt(1, userId);
                            stmt.setInt(2, loanId);
                            stmt.setInt(3, userId);
                            stmt.executeUpdate();
                            
                            // Refresh bảng
                            refreshFeeTable();
                            
                            JOptionPane.showMessageDialog(
                                LoanManagementPanel.this,
                                "Thanh toán phí trả chậm thành công",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(
                                LoanManagementPanel.this,
                                "Lỗi khi thanh toán phí: " + e.getMessage(),
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
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