package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.service.LoanService;
import com.personal.finance.testproject.model.Loan;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import com.personal.finance.testproject.util.DatabaseConnection;

public class LoanPanel extends JPanel {
    private final LoanService loanService;
    private final int userId;
    private JTable loanTable;
    private DefaultTableModel tableModel;
    private JTextField amountField;
    private JTextField interestRateField;
    private JTextField termField;
    private JTextField descriptionField;
    private JComboBox<String> typeComboBox;
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;
    private JTextField purposeIdField;
    private JComboBox<String> formIdComboBox;
    private JTextField dateOfPaymentField;
    private Connection connection;

    public LoanPanel(LoanService loanService, int userId) {
        this.loanService = loanService;
        this.userId = userId;
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Quản lý khoản vay");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Main content panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel);

        // Add main panel to scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Sau phần tạo mainPanel, thêm tab mới cho thanh toán khoản vay
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Quản lý khoản vay", mainPanel);

        // Tab thanh toán khoản vay
        JPanel payLoanPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcPay = new GridBagConstraints();
        gbcPay.insets = new Insets(5, 5, 5, 5);
        gbcPay.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblLoanId = new JLabel("ID khoản vay:");
        JTextField loanIdField = new JTextField(10);
        gbcPay.gridx = 0; gbcPay.gridy = 0;
        payLoanPanel.add(lblLoanId, gbcPay);
        gbcPay.gridx = 1;
        payLoanPanel.add(loanIdField, gbcPay);

        JLabel lblSource = new JLabel("Nguồn tiền:");
        JComboBox<String> sourceCombo = new JComboBox<>(new String[]{"Lương", "Phụ cấp"});
        gbcPay.gridx = 0; gbcPay.gridy = 1;
        payLoanPanel.add(lblSource, gbcPay);
        gbcPay.gridx = 1;
        payLoanPanel.add(sourceCombo, gbcPay);

        JLabel lblPayDate = new JLabel("Ngày thanh toán (yyyy-MM-dd):");
        JTextField payDateField = new JTextField(10);
        gbcPay.gridx = 0; gbcPay.gridy = 2;
        payLoanPanel.add(lblPayDate, gbcPay);
        gbcPay.gridx = 1;
        payLoanPanel.add(payDateField, gbcPay);

        JButton btnPay = new JButton("Thanh toán khoản vay");
        gbcPay.gridx = 0; gbcPay.gridy = 3;
        gbcPay.gridwidth = 2;
        payLoanPanel.add(btnPay, gbcPay);

        btnPay.addActionListener(e -> {
            try {
                int loanId = Integer.parseInt(loanIdField.getText());
                String source = (String)sourceCombo.getSelectedItem();
                String payDate = payDateField.getText();
                String month = payDate.split("-")[1];
                // Lấy số tiền phải trả kỳ này
                String sqlLoan = "SELECT pay_per_term FROM LOAN WHERE LoanID = ? AND UserID = ?";
                PreparedStatement stmtLoan = connection.prepareStatement(sqlLoan);
                stmtLoan.setInt(1, loanId);
                stmtLoan.setInt(2, userId);
                ResultSet rsLoan = stmtLoan.executeQuery();
                if (!rsLoan.next()) throw new Exception("Không tìm thấy khoản vay");
                BigDecimal payAmount = rsLoan.getBigDecimal("pay_per_term");
                // Tìm IncomeID phù hợp
                String sqlIncome = "SELECT IncomeID, remain_income FROM INCOME WHERE UserID = ? AND ic_month = ? AND income_name = ? ORDER BY IncomeID LIMIT 1";
                PreparedStatement stmtIncome = connection.prepareStatement(sqlIncome);
                stmtIncome.setInt(1, userId);
                stmtIncome.setString(2, month);
                stmtIncome.setString(3, source);
                ResultSet rsIncome = stmtIncome.executeQuery();
                if (!rsIncome.next()) throw new Exception("Không tìm thấy nguồn tiền phù hợp");
                int incomeId = rsIncome.getInt("IncomeID");
                BigDecimal remain = rsIncome.getBigDecimal("remain_income");
                if (remain.compareTo(payAmount) < 0) throw new Exception("Số dư nguồn tiền không đủ");
                // Trừ remain_income
                String sqlUpdate = "UPDATE INCOME SET remain_income = remain_income - ? WHERE IncomeID = ? AND UserID = ?";
                PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate);
                stmtUpdate.setBigDecimal(1, payAmount);
                stmtUpdate.setInt(2, incomeId);
                stmtUpdate.setInt(3, userId);
                stmtUpdate.executeUpdate();
                // Thêm transaction
                String sqlTrans = "INSERT INTO TRANSACTION (UserID, TypeID, trans_amount, trans_date, LoanID, IncomeID) VALUES (?, 'loan_pay', ?, ?, ?, ?)";
                PreparedStatement stmtTrans = connection.prepareStatement(sqlTrans);
                stmtTrans.setInt(1, userId);
                stmtTrans.setBigDecimal(2, payAmount);
                stmtTrans.setDate(3, java.sql.Date.valueOf(payDate));
                stmtTrans.setInt(4, loanId);
                stmtTrans.setInt(5, incomeId);
                stmtTrans.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thanh toán khoản vay thành công!");
                loanIdField.setText("");
                payDateField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thanh toán khoản vay: " + ex.getMessage());
            }
        });

        tabbedPane.addTab("Thanh toán khoản vay", payLoanPanel);
        removeAll();
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Thêm khoản vay mới"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // PurposeID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("PurposeID:"), gbc);
        gbc.gridx = 1;
        purposeIdField = new JTextField(20);
        panel.add(purposeIdField, gbc);
        // FormID
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Loại vay (FormID):"), gbc);
        gbc.gridx = 1;
        formIdComboBox = new JComboBox<>(new String[]{"installment_loan", "bullet_loan"});
        panel.add(formIdComboBox, gbc);
        // Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Số tiền vay:"), gbc);
        gbc.gridx = 1;
        amountField = new JTextField(20);
        panel.add(amountField, gbc);
        // Interest Rate
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Lãi suất (%):"), gbc);
        gbc.gridx = 1;
        interestRateField = new JTextField(20);
        panel.add(interestRateField, gbc);
        // Term
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Kỳ hạn (tháng):"), gbc);
        gbc.gridx = 1;
        termField = new JTextField(20);
        panel.add(termField, gbc);
        // Date of Payment
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Ngày trả hàng tháng (1-31):"), gbc);
        gbc.gridx = 1;
        dateOfPaymentField = new JTextField(20);
        panel.add(dateOfPaymentField, gbc);
        // Description
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        panel.add(descriptionField, gbc);
        // Add Button
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        JButton addButton = new JButton("Thêm");
        addButton.setBackground(new Color(0x2E2E5D));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> addLoan());
        panel.add(addButton, gbc);
        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Danh sách khoản vay"));
        String[] columns = {"ID", "PurposeID", "FormID", "Số tiền", "Lãi suất", "Kỳ hạn", "Ngày trả", "Mô tả", "Ngày giải ngân", "Xóa"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
        };
        loanTable = new JTable(tableModel);
        loanTable.setRowHeight(30);
        loanTable.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        loanTable.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JCheckBox()));
        JScrollPane scrollPane = new JScrollPane(loanTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadData() {
        try {
            List<Loan> loans = loanService.getLoansByUserId(userId);
            tableModel.setRowCount(0);
            for (Loan loan : loans) {
                tableModel.addRow(new Object[]{
                    loan.getLoanId(),
                    loan.getPurposeId(),
                    loan.getFormId(),
                    loan.getAmount(),
                    loan.getInterestRate(),
                    loan.getTerm(),
                    loan.getDateOfPayment(),
                    loan.getDescription()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addLoan() {
        try {
            int purposeId = Integer.parseInt(purposeIdField.getText());
            String formId = (String) formIdComboBox.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());
            double interestRate = Double.parseDouble(interestRateField.getText());
            int term = Integer.parseInt(termField.getText());
            int dateOfPayment = Integer.parseInt(dateOfPaymentField.getText());
            String description = descriptionField.getText();

            Loan loan = new Loan();
            loan.setUserId(userId);
            loan.setPurposeId(purposeId);
            loan.setFormId(formId);
            loan.setAmount(BigDecimal.valueOf(amount));
            loan.setInterestRate(BigDecimal.valueOf(interestRate));
            loan.setTerm(term);
            loan.setDateOfPayment(dateOfPayment);
            loan.setDescription(description);

            loanService.addLoan(loan);
            loadData();
            clearForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khoản vay: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        purposeIdField.setText("");
        amountField.setText("");
        interestRateField.setText("");
        termField.setText("");
        dateOfPaymentField.setText("");
        descriptionField.setText("");
        formIdComboBox.setSelectedIndex(0);
    }

    private void deleteLoan() {
        int row = loanTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khoản vay cần xóa", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khoản vay này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int loanId = (int) tableModel.getValueAt(row, 0);
            try {
                loanService.deleteLoan(loanId, userId);
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(LoanPanel.this, "Lỗi khi xóa khoản vay: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ButtonRenderer cho nút Xóa
    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(0x2E2E5D));
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
    // ButtonEditor cho nút Xóa
    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(0x2E2E5D));
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
                int loanId = (int) tableModel.getValueAt(row, 0);
                try {
                    loanService.deleteLoan(loanId, userId);
                    loadData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoanPanel.this, "Lỗi khi xóa khoản vay: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
} 