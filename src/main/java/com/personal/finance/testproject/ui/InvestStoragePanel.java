package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;
import java.math.BigDecimal;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;
import com.personal.finance.testproject.service.TransactionService;
import java.util.List;
import com.personal.finance.testproject.model.Transaction;

public class InvestStoragePanel extends JPanel {
    private final int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0xF5F5F5);
    private static final Color PANEL_BG = new Color(0xFFFFFF);
    private static final Color TABLE_BG = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x333333);
    private static final Color ACCENT = new Color(0x2196F3);
    private TransactionService transactionService;

    public InvestStoragePanel(int userId) {
        this.userId = userId;
        try {
            this.connection = DatabaseConnection.getConnection();
            this.transactionService = new TransactionService();
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
        
        // Create a container panel for the button and table
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(PANEL_BG);
        containerPanel.add(buttonPanel, BorderLayout.NORTH);
        
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
        containerPanel.add(detailScroll, BorderLayout.CENTER);
        
        detailPanel.add(containerPanel, BorderLayout.CENTER);
        loadInvestStorageDetailData(detailModel);
        tabbedPane.addTab("Thông tin chi tiết tài sản", detailPanel);
        
        // Add refresh button action
        refreshButton.addActionListener(e -> {
            try {
                // Create and use PriceUpdateService
                com.personal.finance.testproject.dao.InvestStorageDetailDAO detailDAO = 
                    new com.personal.finance.testproject.dao.impl.InvestStorageDetailDAOImpl(connection);
                com.personal.finance.testproject.service.PriceUpdateService priceService = 
                    new com.personal.finance.testproject.service.PriceUpdateService(detailDAO);
                priceService.updatePrices();
                
                // Refresh tables
                loadInvestStorageDetailData(detailModel);
                loadInvestStorageData(investModel);
                
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

        // Tab 3: Bán tài sản
        JPanel sellPanel = new JPanel(new GridBagLayout());
        sellPanel.setBackground(PANEL_BG);
        GridBagConstraints gbcSell = new GridBagConstraints();
        gbcSell.insets = new Insets(5, 5, 5, 5);
        gbcSell.fill = GridBagConstraints.HORIZONTAL;

        // Form bán tài sản
        JPanel sellFormPanel = new JPanel(new GridBagLayout());
        sellFormPanel.setBackground(PANEL_BG);
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 5, 5, 5);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;

        // Mã tài sản (combo box)
        JLabel lblAssetId = new JLabel("Mã tài sản:");
        lblAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JComboBox<String> cmbAssetId = new JComboBox<>();
        cmbAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadAssetIds(cmbAssetId);
        gbcForm.gridx = 0; gbcForm.gridy = 0;
        sellFormPanel.add(lblAssetId, gbcForm);
        gbcForm.gridx = 1;
        sellFormPanel.add(cmbAssetId, gbcForm);

        // Số tiền
        JLabel lblAmount = new JLabel("Số tiền:");
        lblAmount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JTextField txtAmount = new JTextField(20);
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbcForm.gridx = 0; gbcForm.gridy = 1;
        sellFormPanel.add(lblAmount, gbcForm);
        gbcForm.gridx = 1;
        sellFormPanel.add(txtAmount, gbcForm);

        // Ngày giao dịch
        JLabel lblDate = new JLabel("Ngày giao dịch:");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateChooser.setPreferredSize(new Dimension(200, 30));
        gbcForm.gridx = 0; gbcForm.gridy = 2;
        sellFormPanel.add(lblDate, gbcForm);
        gbcForm.gridx = 1;
        sellFormPanel.add(dateChooser, gbcForm);

        // Nút thêm giao dịch bán
        JButton btnAddSell = new JButton("Thêm giao dịch bán");
        btnAddSell.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAddSell.setBackground(new Color(0x2E2E5D));
        btnAddSell.setForeground(Color.WHITE);
        btnAddSell.setFocusPainted(false);
        btnAddSell.setBorderPainted(false);
        btnAddSell.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcForm.gridx = 0; gbcForm.gridy = 3;
        gbcForm.gridwidth = 2;
        sellFormPanel.add(btnAddSell, gbcForm);

        // Bảng lịch sử bán
        String[] sellCols = {"Mã tài sản", "Số tiền", "Ngày giao dịch"};
        DefaultTableModel sellModel = new DefaultTableModel(sellCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable sellTable = new JTable(sellModel);
        sellTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sellTable.setRowHeight(32);
        sellTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        sellTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane sellScroll = new JScrollPane(sellTable);
        sellScroll.setPreferredSize(new Dimension(750, 220));

        // Thêm form và bảng vào panel bán
        gbcSell.gridx = 0; gbcSell.gridy = 0;
        sellPanel.add(sellFormPanel, gbcSell);
        gbcSell.gridy = 1;
        sellPanel.add(sellScroll, gbcSell);

        // Xử lý sự kiện thêm giao dịch bán
        btnAddSell.addActionListener(e -> {
            try {
                String assetId = (String)cmbAssetId.getSelectedItem();
                if (assetId == null || assetId.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn mã tài sản");
                    return;
                }
                String amountStr = txtAmount.getText().trim();
                if (amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền");
                    return;
                }
                BigDecimal amount = new BigDecimal(amountStr);
                if (dateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày giao dịch");
                    return;
                }
                java.util.Date transDate = dateChooser.getDate();

                // Thêm giao dịch bán
                transactionService.addSellTransaction(userId, assetId, amount, transDate);

                // Refresh bảng
                loadSellTransactions(sellModel);
                loadInvestStorageData(investModel);

                // Clear form
                cmbAssetId.setSelectedItem(null);
                txtAmount.setText("");
                dateChooser.setDate(null);

                JOptionPane.showMessageDialog(this, "Thêm giao dịch bán thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm giao dịch bán: " + ex.getMessage());
            }
        });

        tabbedPane.addTab("Bán tài sản", sellPanel);

        // Tab 4: Mua tài sản
        JPanel buyPanel = new JPanel(new GridBagLayout());
        buyPanel.setBackground(PANEL_BG);
        GridBagConstraints gbcBuy = new GridBagConstraints();
        gbcBuy.insets = new Insets(5, 5, 5, 5);
        gbcBuy.fill = GridBagConstraints.HORIZONTAL;

        // Form mua tài sản
        JPanel buyFormPanel = new JPanel(new GridBagLayout());
        buyFormPanel.setBackground(PANEL_BG);
        GridBagConstraints gbcBuyForm = new GridBagConstraints();
        gbcBuyForm.insets = new Insets(5, 5, 5, 5);
        gbcBuyForm.fill = GridBagConstraints.HORIZONTAL;

        // Mã tài sản (combo box)
        JLabel lblBuyAssetId = new JLabel("Mã tài sản:");
        lblBuyAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JComboBox<String> cmbBuyAssetId = new JComboBox<>();
        cmbBuyAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadAssetIds(cmbBuyAssetId);
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = 0;
        buyFormPanel.add(lblBuyAssetId, gbcBuyForm);
        gbcBuyForm.gridx = 1;
        buyFormPanel.add(cmbBuyAssetId, gbcBuyForm);

        // Nguồn tiền
        JLabel lblSource = new JLabel("Nguồn tiền:");
        lblSource.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JComboBox<String> cmbSource = new JComboBox<>(new String[]{"Lương", "Phụ cấp"});
        cmbSource.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = 1;
        buyFormPanel.add(lblSource, gbcBuyForm);
        gbcBuyForm.gridx = 1;
        buyFormPanel.add(cmbSource, gbcBuyForm);

        // Ngày giao dịch
        JLabel lblBuyDate = new JLabel("Ngày giao dịch:");
        lblBuyDate.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JDateChooser buyDateChooser = new JDateChooser();
        buyDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        buyDateChooser.setPreferredSize(new Dimension(200, 30));
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = 2;
        buyFormPanel.add(lblBuyDate, gbcBuyForm);
        gbcBuyForm.gridx = 1;
        buyFormPanel.add(buyDateChooser, gbcBuyForm);

        // Nút thêm giao dịch mua
        JButton btnAddBuy = new JButton("Thêm giao dịch mua");
        btnAddBuy.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAddBuy.setBackground(new Color(0x2E2E5D));
        btnAddBuy.setForeground(Color.WHITE);
        btnAddBuy.setFocusPainted(false);
        btnAddBuy.setBorderPainted(false);
        btnAddBuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = 3;
        gbcBuyForm.gridwidth = 2;
        buyFormPanel.add(btnAddBuy, gbcBuyForm);

        // Bảng lịch sử mua
        String[] buyCols = {"Mã tài sản", "Nguồn tiền", "Ngày giao dịch", "Tháng thu nhập"};
        DefaultTableModel buyModel = new DefaultTableModel(buyCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable buyTable = new JTable(buyModel);
        buyTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        buyTable.setRowHeight(32);
        buyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        buyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane buyScroll = new JScrollPane(buyTable);
        buyScroll.setPreferredSize(new Dimension(750, 220));

        // Thêm form và bảng vào panel mua
        gbcBuy.gridx = 0; gbcBuy.gridy = 0;
        buyPanel.add(buyFormPanel, gbcBuy);
        gbcBuy.gridy = 1;
        buyPanel.add(buyScroll, gbcBuy);

        // Xử lý sự kiện thêm giao dịch mua
        btnAddBuy.addActionListener(e -> {
            try {
                String assetId = (String)cmbBuyAssetId.getSelectedItem();
                if (assetId == null || assetId.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn mã tài sản");
                    return;
                }
                String source = (String)cmbSource.getSelectedItem();
                if (source == null || source.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn nguồn tiền");
                    return;
                }
                if (buyDateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày giao dịch");
                    return;
                }
                java.util.Date transDate = buyDateChooser.getDate();
                
                // Xác định tháng thu nhập dựa trên ngày giao dịch
                Calendar cal = Calendar.getInstance();
                cal.setTime(transDate);
                int month = cal.get(Calendar.MONTH) + 1;

                // Lấy giá hiện tại của tài sản
                String sql = "SELECT cur_price FROM INVEST_STORAGE_DETAIL WHERE InStID = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, assetId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin tài sản");
                    return;
                }
                BigDecimal price = rs.getBigDecimal("cur_price");

                // Lấy IncomeID dựa trên nguồn tiền và tháng
                sql = "SELECT IncomeID FROM INCOME WHERE UserID = ? AND ic_month = ? AND ic_name = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setInt(2, month);
                stmt.setString(3, source);
                rs = stmt.executeQuery();
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thu nhập " + source + " trong tháng " + month);
                    return;
                }
                int incomeId = rs.getInt("IncomeID");

                // Thêm giao dịch mua
                transactionService.addBuyTransaction(userId, assetId, price, transDate, incomeId);

                // Refresh bảng
                loadBuyTransactions(buyModel);
                loadInvestStorageData(investModel);

                // Clear form
                cmbBuyAssetId.setSelectedItem(null);
                cmbSource.setSelectedItem(null);
                buyDateChooser.setDate(null);

                JOptionPane.showMessageDialog(this, "Thêm giao dịch mua thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm giao dịch mua: " + ex.getMessage());
            }
        });

        tabbedPane.addTab("Mua tài sản", buyPanel);

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
            String sql = "SELECT isr.InStID, isr.num_unit, isr.buy_price, isr.es_profit, isr.up_date, " +
                        "isd.cur_price, isd.unit " +
                        "FROM INVEST_STORAGE isr " +
                        "LEFT JOIN INVEST_STORAGE_DETAIL isd ON isr.InStID = isd.InStID " +
                        "WHERE isr.UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("InStID"));
                row.add(rs.getBigDecimal("num_unit"));
                row.add(rs.getBigDecimal("buy_price"));
                row.add(rs.getBigDecimal("cur_price"));
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

    private void loadAssetIds(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        try {
            String sql = "SELECT DISTINCT InStID FROM INVEST_STORAGE_DETAIL ORDER BY InStID";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboBox.addItem(rs.getString("InStID"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách mã tài sản: " + e.getMessage());
        }
    }

    private void loadSellTransactions(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Transaction> transactions = transactionService.getTransactionsByUserIdAndType(userId, "InSt_Sell");
            for (Transaction trans : transactions) {
                Vector<Object> row = new Vector<>();
                row.add(trans.getInStId());
                row.add(trans.getTransAmount());
                row.add(trans.getTransDate());
                model.addRow(row);
            }
        } catch (Exception e) {
         
        }
    }

    private void loadBuyTransactions(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Transaction> transactions = transactionService.getTransactionsByUserIdAndType(userId, "InSt_Buy");
            for (Transaction trans : transactions) {
                Vector<Object> row = new Vector<>();
                row.add(trans.getInStId());
                row.add(trans.getTransAmount());
                row.add(trans.getTransDate());
                
                // Lấy tháng thu nhập
                String sql = "SELECT ic_month FROM INCOME WHERE IncomeID = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, trans.getIncomeId());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    row.add(rs.getInt("ic_month"));
                }
                
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lịch sử mua: " + e.getMessage());
        }
    }

    public void dispose() {
        if (transactionService != null) {
            transactionService.close();
        }
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 