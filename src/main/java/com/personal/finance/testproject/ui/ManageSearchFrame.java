package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.Vector;
import com.personal.finance.testproject.dao.*;
import com.personal.finance.testproject.model.*;
import com.personal.finance.testproject.dao.impl.*;
import com.personal.finance.testproject.util.DatabaseConnection;
import com.personal.finance.testproject.service.PriceUpdateService;

public class ManageSearchFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0x000000); // Đen tuyệt đối
    private static final Color PANEL_BG = new Color(0xE0E0E0); // Xám nhạt
    private static final Color TABLE_BG = Color.WHITE;
    private static final Color TEXT_DARK = Color.BLACK;
    private static final Color ACCENT = new Color(0x2E2E5D);

    public ManageSearchFrame(int userId) {
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
        // Giới hạn chiều cao tab
        UIManager.put("TabbedPane.tabHeight", 36);
        setTitle("Quản lý & Tra cứu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(MAIN_BG);
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(PANEL_BG);
        tabbedPane.setForeground(TEXT_DARK);
        
        // Add tabs
        tabbedPane.addTab("Thu nhập", createIncomePanel());
        tabbedPane.addTab("Khoản vay", createLoanPanel());
        tabbedPane.addTab("Tiết kiệm", createSavingPanel());
        tabbedPane.addTab("Đầu tư & Tích trữ", createInvestPanel());
        tabbedPane.addTab("Giao dịch", createTransactionPanel());
        tabbedPane.addTab("Mục đích", createPurposePanel());

        add(tabbedPane);
    }

    private JPanel createIncomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        String[] columnNames = {"ID", "Tháng", "Tên khoản thu", "Số tiền", "Số dư còn lại"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(PANEL_BG);
        table.getTableHeader().setForeground(ACCENT);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(PANEL_BG);
        panel.add(scrollPane, BorderLayout.CENTER);
        loadIncomeData(model);
        // Form thêm mới
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBackground(PANEL_BG);
        JComboBox<String> monthCombo = new JComboBox<>(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
        JTextField nameField = new JTextField(12);
        JTextField amountField = new JTextField(10);
        JButton btnAdd = new JButton("Thêm khoản thu");
        btnAdd.setBackground(ACCENT);
        btnAdd.setForeground(Color.WHITE);
        formPanel.add(new JLabel("Tháng:"));
        formPanel.add(monthCombo);
        formPanel.add(new JLabel("Tên khoản thu:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Số tiền:"));
        formPanel.add(amountField);
        formPanel.add(btnAdd);
        panel.add(formPanel, BorderLayout.NORTH);
        btnAdd.addActionListener(e -> {
            try {
                String sql = "INSERT INTO INCOME (UserID, ic_month, income_name, income_amount) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, (String) monthCombo.getSelectedItem());
                stmt.setString(3, nameField.getText());
                stmt.setBigDecimal(4, new java.math.BigDecimal(amountField.getText()));
                stmt.executeUpdate();
                loadIncomeData(model);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm khoản thu: " + ex.getMessage());
            }
        });
        return panel;
    }

    private JPanel createLoanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Tên mục đích", "Số tiền vay", "Lãi suất", "Số kỳ", "Đã trả", "Còn lại", "Ngày giải ngân"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        loadLoanData(model);
        // Form thêm mới
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField purposeField = new JTextField(12);
        JTextField amountField = new JTextField(10);
        JTextField interestField = new JTextField(5);
        JTextField numTermField = new JTextField(5);
        JTextField disburDateField = new JTextField(10);
        JButton btnAdd = new JButton("Thêm khoản vay");
        formPanel.add(new JLabel("Tên mục đích:"));
        formPanel.add(purposeField);
        formPanel.add(new JLabel("Số tiền vay:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Lãi suất:"));
        formPanel.add(interestField);
        formPanel.add(new JLabel("Số kỳ:"));
        formPanel.add(numTermField);
        formPanel.add(new JLabel("Ngày giải ngân (yyyy-MM-dd):"));
        formPanel.add(disburDateField);
        formPanel.add(btnAdd);
        panel.add(formPanel, BorderLayout.NORTH);
        btnAdd.addActionListener(e -> {
            try {
                // Thêm mục đích nếu chưa có
                String sqlPurpose = "INSERT IGNORE INTO PURPOSE (UserID, purpose_name) VALUES (?, ?)";
                PreparedStatement stmtPurpose = connection.prepareStatement(sqlPurpose);
                stmtPurpose.setInt(1, userId);
                stmtPurpose.setString(2, purposeField.getText());
                stmtPurpose.executeUpdate();
                // Lấy PurposeID
                String sqlGetPurpose = "SELECT PurposeID FROM PURPOSE WHERE UserID = ? AND purpose_name = ?";
                PreparedStatement stmtGetPurpose = connection.prepareStatement(sqlGetPurpose);
                stmtGetPurpose.setInt(1, userId);
                stmtGetPurpose.setString(2, purposeField.getText());
                ResultSet rs = stmtGetPurpose.executeQuery();
                int purposeId = 0;
                if (rs.next()) purposeId = rs.getInt(1);
                // Thêm khoản vay
                String sql = "INSERT INTO LOAN (UserID, PurposeID, loan_amount, interest, num_term, disbur_date) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setInt(2, purposeId);
                stmt.setBigDecimal(3, new java.math.BigDecimal(amountField.getText()));
                stmt.setBigDecimal(4, new java.math.BigDecimal(interestField.getText()));
                stmt.setInt(5, Integer.parseInt(numTermField.getText()));
                stmt.setDate(6, java.sql.Date.valueOf(disburDateField.getText()));
                stmt.executeUpdate();
                loadLoanData(model);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm khoản vay: " + ex.getMessage());
            }
        });
        return panel;
    }

    private JPanel createSavingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"ID", "Tên tiết kiệm", "Ngân hàng", "Số tiền gửi", "Còn lại", "Ngày cập nhật"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        loadSavingData(model);
        // Form thêm mới
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(12);
        JTextField bankField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JButton btnAdd = new JButton("Thêm tiết kiệm");
        formPanel.add(new JLabel("Tên tiết kiệm:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Ngân hàng:"));
        formPanel.add(bankField);
        formPanel.add(new JLabel("Số tiền gửi:"));
        formPanel.add(amountField);
        formPanel.add(btnAdd);
        panel.add(formPanel, BorderLayout.NORTH);
        btnAdd.addActionListener(e -> {
            try {
                // Lấy BankAccountNumber
                String sqlGetBank = "SELECT BankAccountNumber FROM BANK_ACCOUNT WHERE UserID = ? AND BankName = ? LIMIT 1";
                PreparedStatement stmtGetBank = connection.prepareStatement(sqlGetBank);
                stmtGetBank.setInt(1, userId);
                stmtGetBank.setString(2, bankField.getText());
                ResultSet rs = stmtGetBank.executeQuery();
                String bankAccount = null;
                if (rs.next()) bankAccount = rs.getString(1);
                if (bankAccount == null) throw new Exception("Không tìm thấy tài khoản ngân hàng");
                // Thêm tiết kiệm
                String sql = "INSERT INTO SAVING (UserID, save_name, BankAccountNumber, save_amount, remain_save, up_date, PurposeID) VALUES (?, ?, ?, ?, ?, CURDATE(), 1)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, nameField.getText());
                stmt.setString(3, bankAccount);
                stmt.setBigDecimal(4, new java.math.BigDecimal(amountField.getText()));
                stmt.setBigDecimal(5, new java.math.BigDecimal(amountField.getText()));
                stmt.executeUpdate();
                loadSavingData(model);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm tiết kiệm: " + ex.getMessage());
            }
        });
        return panel;
    }

    private JPanel createInvestPanel() {
        JPanel panel = new JPanel(new GridLayout(2,1,0,16));
        panel.setBackground(PANEL_BG);
        
        // Add refresh button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PANEL_BG);
        JButton refreshButton = new JButton("Cập nhật giá");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        refreshButton.setBackground(ACCENT);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonPanel.add(refreshButton);
        
        // Create a container panel for the button and tables
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(PANEL_BG);
        containerPanel.add(buttonPanel, BorderLayout.NORTH);
        
        // Bảng 1: Danh mục đầu tư của tôi
        String[] myInvestCols = {"ID", "Tên tài sản", "Số lượng", "Giá mua", "Giá hiện tại", "Lợi nhuận ước tính", "Ngày cập nhật"};
        DefaultTableModel myInvestModel = new DefaultTableModel(myInvestCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable myInvestTable = new JTable(myInvestModel);
        myInvestTable.setRowHeight(32);
        myInvestTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        myInvestTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        myInvestTable.getTableHeader().setBackground(PANEL_BG);
        myInvestTable.getTableHeader().setForeground(ACCENT);
        JScrollPane myInvestScroll = new JScrollPane(myInvestTable);
        myInvestScroll.setBackground(PANEL_BG);
        loadInvestData(myInvestModel);
        
        // Bảng 2: Thông tin chi tiết tài sản
        String[] detailCols = {"Mã tài sản", "Cấp rủi ro", "Giá hiện tại", "Z-Score", "Độ lệch chuẩn", "Đơn vị"};
        DefaultTableModel detailModel = new DefaultTableModel(detailCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(32);
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        detailTable.getTableHeader().setBackground(PANEL_BG);
        detailTable.getTableHeader().setForeground(ACCENT);
        JScrollPane detailScroll = new JScrollPane(detailTable);
        detailScroll.setBackground(PANEL_BG);
        loadInvestStorageDetailData(detailModel);
        
        // Add tables to container panel
        JPanel tablesPanel = new JPanel(new GridLayout(2,1,0,16));
        tablesPanel.setBackground(PANEL_BG);
        tablesPanel.add(myInvestScroll);
        tablesPanel.add(detailScroll);
        containerPanel.add(tablesPanel, BorderLayout.CENTER);
        
        // Add container panel to main panel
        panel.add(containerPanel);
        
        // Add refresh button action
        refreshButton.addActionListener(e -> {
            try {
                // Create and use PriceUpdateService
                InvestStorageDetailDAO detailDAO = new InvestStorageDetailDAOImpl(connection);
                PriceUpdateService priceService = new PriceUpdateService(detailDAO);
                priceService.updatePrices();
                
                // Refresh tables
                loadInvestData(myInvestModel);
                loadInvestStorageDetailData(detailModel);
                
                JOptionPane.showMessageDialog(this, 
                    "Cập nhật giá thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi cập nhật giá: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }

    private void loadIncomeData(DefaultTableModel model) {
        model.setRowCount(0);
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
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private void loadLoanData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT l.*, p.purpose_name FROM LOAN l JOIN PURPOSE p ON l.PurposeID = p.PurposeID AND l.UserID = p.UserID WHERE l.UserID = ? ORDER BY l.disbur_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("LoanID"));
                row.add(rs.getString("purpose_name"));
                row.add(rs.getBigDecimal("loan_amount"));
                row.add(rs.getBigDecimal("interest"));
                row.add(rs.getInt("num_term"));
                row.add(rs.getInt("num_paid_term"));
                row.add(rs.getBigDecimal("loan_remain"));
                row.add(rs.getDate("disbur_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khoản vay: " + e.getMessage());
        }
    }

    private void loadSavingData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM SAVING WHERE UserID = ? ORDER BY up_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("SaveID"));
                row.add(rs.getString("save_name"));
                row.add(rs.getString("BankAccountNumber"));
                row.add(rs.getBigDecimal("save_amount"));
                row.add(rs.getBigDecimal("remain_save"));
                row.add(rs.getDate("up_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu tiết kiệm: " + e.getMessage());
        }
    }

    private void loadInvestData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT isr.InStID, isd.unit, isr.num_unit, isr.buy_price, isd.cur_price, isr.es_profit, isr.up_date FROM INVEST_STORAGE isr JOIN INVEST_STORAGE_DETAIL isd ON isr.InStID = isd.InStID WHERE isr.UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("InStID"));
                row.add(rs.getString("unit"));
                row.add(rs.getBigDecimal("num_unit"));
                row.add(rs.getBigDecimal("buy_price"));
                row.add(rs.getBigDecimal("cur_price"));
                row.add(rs.getBigDecimal("es_profit"));
                row.add(rs.getDate("up_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu đầu tư: " + e.getMessage());
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

    private JPanel createPurposePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        String[] columnNames = {"ID", "Tên mục đích", "Mô tả", "Ngày tạo"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(PANEL_BG);
        table.getTableHeader().setForeground(ACCENT);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(PANEL_BG);
        panel.add(scrollPane, BorderLayout.CENTER);
        loadPurposeData(model);

        // Form thêm mới
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBackground(PANEL_BG);
        JTextField nameField = new JTextField(15);
        JTextField descField = new JTextField(20);
        JButton btnAdd = new JButton("Thêm mục đích");
        btnAdd.setBackground(ACCENT);
        btnAdd.setForeground(Color.WHITE);
        formPanel.add(new JLabel("Tên mục đích:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Mô tả:"));
        formPanel.add(descField);
        formPanel.add(btnAdd);
        panel.add(formPanel, BorderLayout.NORTH);

        btnAdd.addActionListener(e -> {
            try {
                String sql = "INSERT INTO PURPOSE (UserID, purpose_name, purpose_description, create_date) VALUES (?, ?, ?, CURRENT_DATE)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, nameField.getText());
                stmt.setString(3, descField.getText());
                stmt.executeUpdate();
                loadPurposeData(model);
                nameField.setText("");
                descField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm mục đích: " + ex.getMessage());
            }
        });
        return panel;
    }

    private void loadPurposeData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT * FROM PURPOSE WHERE UserID = ? ORDER BY create_date DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("PurposeID"));
                row.add(rs.getString("purpose_name"));
                row.add(rs.getString("purpose_description"));
                row.add(rs.getDate("create_date"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu mục đích: " + e.getMessage());
        }
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        // Form thêm/tìm kiếm
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBackground(new Color(0xE0E0E0));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Thu nhập", "Chi tiêu", "Chuyển khoản"});
        JComboBox<String> purposeCombo = new JComboBox<>();
        JTextField amountField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField noteField = new JTextField(20);
        JButton btnAdd = new JButton("Thêm giao dịch");
        btnAdd.setBackground(ACCENT);
        btnAdd.setForeground(Color.WHITE);
        formPanel.add(new JLabel("Loại giao dịch:"));
        formPanel.add(typeCombo);
        formPanel.add(new JLabel("Mục đích:"));
        formPanel.add(purposeCombo);
        formPanel.add(new JLabel("Số tiền:"));
        formPanel.add(amountField);
        formPanel.add(new JLabel("Ngày (yyyy-MM-dd):"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Ghi chú:"));
        formPanel.add(noteField);
        formPanel.add(btnAdd);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(formPanel, gbc);
        // Bảng dữ liệu
        String[] columnNames = {"ID", "Loại giao dịch", "Mục đích", "Số tiền", "Ngày giao dịch", "Ghi chú"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(model);
        table.setBackground(TABLE_BG);
        table.setForeground(TEXT_DARK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(PANEL_BG);
        table.getTableHeader().setForeground(ACCENT);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 220));
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPane, gbc);
        // Thêm padding dưới cùng nếu cần
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        // Load dữ liệu mục đích
        loadPurposes(purposeCombo);
        // Load dữ liệu giao dịch
        loadTransactionData(model);
        btnAdd.addActionListener(e -> {
            try {
                String typeId = "";
                switch((String)typeCombo.getSelectedItem()) {
                    case "Thu nhập": typeId = "INCOME"; break;
                    case "Chi tiêu": typeId = "SP_Food"; break;
                    case "Chuyển khoản": typeId = "TRANSFER"; break;
                }
                String sql = "INSERT INTO TRANSACTION (UserID, TypeID, trans_amount, trans_date) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, typeId);
                stmt.setBigDecimal(3, new java.math.BigDecimal(amountField.getText()));
                stmt.setDate(4, java.sql.Date.valueOf(dateField.getText()));
                stmt.executeUpdate();
                loadTransactionData(model);
                amountField.setText("");
                dateField.setText("");
                noteField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Lỗi thêm giao dịch: " + ex.getMessage());
            }
        });
        return panel;
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
                row.add(""); // Không có mục đích
                row.add(rs.getBigDecimal("trans_amount"));
                row.add(rs.getDate("trans_date"));
                row.add(rs.getString("note"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu giao dịch: " + e.getMessage());
        }
    }

    private void loadPurposes(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        try {
            String sql = "SELECT PurposeID, purpose_name FROM PURPOSE WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getInt("PurposeID") + " - " + rs.getString("purpose_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách mục đích: " + e.getMessage());
        }
    }
} 