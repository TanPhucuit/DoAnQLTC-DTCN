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
import com.personal.finance.testproject.service.PriceUpdateService;

public class InvestStoragePanel extends JPanel {
    private final int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0xF5F5F5);
    private static final Color PANEL_BG = new Color(0xFFFFFF);
    private static final Color TABLE_BG = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x333333);
    private static final Color ACCENT = new Color(0x2196F3);
    private TransactionService transactionService;
    private ManageSearchFrame parentFrame;
    private DefaultTableModel investModel;

    public InvestStoragePanel(int userId, ManageSearchFrame parentFrame) {
        this.userId = userId;
        this.parentFrame = parentFrame;
        try {
            this.connection = DatabaseConnection.getConnection();
            this.transactionService = new TransactionService();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(MAIN_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(PANEL_BG);
        tabbedPane.setForeground(TEXT_DARK);

        // Thêm nút quay lại ở đầu initializeUI()
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
        backPanel.setBackground(MAIN_BG);
        backPanel.add(btnBack);
        add(backPanel, BorderLayout.NORTH);

        // Tab 1: Danh mục đầu tư của tôi
        JPanel myInvestPanel = new JPanel(new BorderLayout());
        myInvestPanel.setBackground(PANEL_BG);
        String[] investCols = {"Mã tài sản", "Số lượng", "Giá mua TB", "Lợi nhuận ước tính", "Ngày cập nhật"};
        investModel = new DefaultTableModel(investCols, 0) {
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
        JTable detailTable = new JTable(detailModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setForeground(Color.BLACK);
                return c;
            }
        };
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

        // Tab 3: Bán tài sản
        JPanel sellPanel = new JPanel(new BorderLayout());
        sellPanel.setBackground(PANEL_BG);
        JPanel sellFormPanel = new JPanel(new GridBagLayout());
        sellFormPanel.setBackground(PANEL_BG);
        GridBagConstraints gbcSellForm = new GridBagConstraints();
        gbcSellForm.insets = new Insets(8, 8, 8, 8);
        gbcSellForm.fill = GridBagConstraints.HORIZONTAL;
        gbcSellForm.anchor = GridBagConstraints.WEST;
        int sellRow = 0;
        JLabel lblAssetId = new JLabel("Mã tài sản:");
        lblAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> cmbAssetId = new JComboBox<>();
        cmbAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadAssetIds(cmbAssetId);
        gbcSellForm.gridx = 0; gbcSellForm.gridy = sellRow; sellFormPanel.add(lblAssetId, gbcSellForm);
        gbcSellForm.gridx = 1; sellFormPanel.add(cmbAssetId, gbcSellForm);
        sellRow++;
        JLabel lblAmount = new JLabel("Số tiền:");
        lblAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtAmount = new JTextField(15);
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbcSellForm.gridx = 0; gbcSellForm.gridy = sellRow; sellFormPanel.add(lblAmount, gbcSellForm);
        gbcSellForm.gridx = 1; sellFormPanel.add(txtAmount, gbcSellForm);
        sellRow++;
        JLabel lblDate = new JLabel("Ngày giao dịch:");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbcSellForm.gridx = 0; gbcSellForm.gridy = sellRow; sellFormPanel.add(lblDate, gbcSellForm);
        gbcSellForm.gridx = 1; sellFormPanel.add(dateChooser, gbcSellForm);
        sellRow++;
        JButton btnAddSell = new JButton("Thêm giao dịch bán");
        btnAddSell.setBackground(new Color(0x008BCF));
        btnAddSell.setForeground(Color.WHITE);
        btnAddSell.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAddSell.setFocusPainted(false);
        btnAddSell.setBorderPainted(false);
        btnAddSell.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcSellForm.gridx = 0; gbcSellForm.gridy = sellRow; gbcSellForm.gridwidth = 2;
        sellFormPanel.add(btnAddSell, gbcSellForm);
        sellPanel.add(sellFormPanel, BorderLayout.NORTH);

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
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0");
                    return;
                }
                if (dateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày giao dịch");
                    return;
                }
                java.util.Date transDate = dateChooser.getDate();

                // Thêm giao dịch bán
                transactionService.addSellTransaction(userId, assetId, amount, transDate);

                // Clear form
                cmbAssetId.setSelectedItem(null);
                txtAmount.setText("");
                dateChooser.setDate(null);

                // Sau khi thêm giao dịch, reload bảng danh mục đầu tư
                loadInvestStorageData(investModel);

                JOptionPane.showMessageDialog(this, "Thêm giao dịch bán thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm giao dịch bán: " + ex.getMessage());
            }
        });

        tabbedPane.addTab("Bán tài sản", sellPanel);

        // Tab 4: Mua tài sản
        JPanel buyPanel = new JPanel(new BorderLayout());
        buyPanel.setBackground(PANEL_BG);
        JPanel buyFormPanel = new JPanel(new GridBagLayout());
        buyFormPanel.setBackground(PANEL_BG);
        GridBagConstraints gbcBuyForm = new GridBagConstraints();
        gbcBuyForm.insets = new Insets(8, 8, 8, 8);
        gbcBuyForm.fill = GridBagConstraints.HORIZONTAL;
        gbcBuyForm.anchor = GridBagConstraints.WEST;
        int buyRow = 0;
        JLabel lblBuyAssetId = new JLabel("Mã tài sản:");
        lblBuyAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> cmbBuyAssetId = new JComboBox<>();
        cmbBuyAssetId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loadAssetIds(cmbBuyAssetId);
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = buyRow; buyFormPanel.add(lblBuyAssetId, gbcBuyForm);
        gbcBuyForm.gridx = 1; buyFormPanel.add(cmbBuyAssetId, gbcBuyForm);
        buyRow++;
        JLabel lblBuyAmount = new JLabel("Số tiền:");
        lblBuyAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtBuyAmount = new JTextField(15);
        txtBuyAmount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = buyRow; buyFormPanel.add(lblBuyAmount, gbcBuyForm);
        gbcBuyForm.gridx = 1; buyFormPanel.add(txtBuyAmount, gbcBuyForm);
        buyRow++;
        JLabel lblSource = new JLabel("Nguồn tiền:");
        lblSource.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> cmbSource = new JComboBox<>(new String[]{"Lương", "Phụ cấp"});
        cmbSource.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = buyRow; buyFormPanel.add(lblSource, gbcBuyForm);
        gbcBuyForm.gridx = 1; buyFormPanel.add(cmbSource, gbcBuyForm);
        buyRow++;
        JLabel lblBuyDate = new JLabel("Ngày giao dịch:");
        lblBuyDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JDateChooser buyDateChooser = new JDateChooser();
        buyDateChooser.setDateFormatString("yyyy-MM-dd");
        buyDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = buyRow; buyFormPanel.add(lblBuyDate, gbcBuyForm);
        gbcBuyForm.gridx = 1; buyFormPanel.add(buyDateChooser, gbcBuyForm);
        buyRow++;
        JButton btnAddBuy = new JButton("Thêm giao dịch mua");
        btnAddBuy.setBackground(new Color(0x008BCF));
        btnAddBuy.setForeground(Color.WHITE);
        btnAddBuy.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAddBuy.setFocusPainted(false);
        btnAddBuy.setBorderPainted(false);
        btnAddBuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcBuyForm.gridx = 0; gbcBuyForm.gridy = buyRow; gbcBuyForm.gridwidth = 2;
        buyFormPanel.add(btnAddBuy, gbcBuyForm);
        buyPanel.add(buyFormPanel, BorderLayout.NORTH);

        btnAddBuy.addActionListener(e -> {
            try {
                String assetId = (String)cmbBuyAssetId.getSelectedItem();
                if (assetId == null || assetId.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn mã tài sản");
                    return;
                }
                String amountStr = txtBuyAmount.getText().trim();
                if (amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền");
                    return;
                }
                BigDecimal amount = new BigDecimal(amountStr);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0");
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
                Calendar cal = Calendar.getInstance();
                cal.setTime(transDate);
                int month = cal.get(Calendar.MONTH) + 1;
                String monthStr = String.valueOf(month);
                int incomeId = -1;
                // Kiểm tra số dư nguồn tiền và trừ tiền nếu đủ
                if (source.equals("Lương") || source.equals("Phụ cấp")) {
                    String sql = "SELECT IncomeID, remain_income FROM INCOME WHERE UserID = ? AND ic_month = ? AND income_name = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    stmt.setString(2, monthStr);
                    stmt.setString(3, source);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy thu nhập " + source + " trong tháng " + month);
                        return;
                    }
                    incomeId = rs.getInt("IncomeID");
                    BigDecimal remain = rs.getBigDecimal("remain_income");
                    if (remain.compareTo(amount) < 0) {
                        JOptionPane.showMessageDialog(this, "Số dư " + source + " không đủ");
                        return;
                    }
                    // Trừ tiền
                    String sqlUpdate = "UPDATE INCOME SET remain_income = remain_income - ? WHERE IncomeID = ?";
                    PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate);
                    stmtUpdate.setBigDecimal(1, amount);
                    stmtUpdate.setInt(2, incomeId);
                    stmtUpdate.executeUpdate();
                } else {
                    JOptionPane.showMessageDialog(this, "Chỉ hỗ trợ nguồn tiền Lương hoặc Phụ cấp");
                    return;
                }
                // Thêm giao dịch mua
                transactionService.addBuyTransaction(userId, assetId, amount, transDate, incomeId);
                // Clear form
                cmbBuyAssetId.setSelectedItem(null);
                txtBuyAmount.setText("");
                cmbSource.setSelectedItem(null);
                buyDateChooser.setDate(null);
                // Sau khi thêm giao dịch, reload bảng danh mục đầu tư
                loadInvestStorageData(investModel);
                JOptionPane.showMessageDialog(this, "Thêm giao dịch mua thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm giao dịch mua: " + ex.getMessage());
            }
        });

        tabbedPane.addTab("Mua tài sản", buyPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Bổ sung logic cập nhật giá
        refreshButton.addActionListener(e -> {
            try {
                // Gọi service cập nhật giá
                PriceUpdateService priceUpdateService = new PriceUpdateService(new com.personal.finance.testproject.dao.impl.InvestStorageDetailDAOImpl(connection));
                priceUpdateService.updatePrices();
                // Sau khi cập nhật xong, load lại bảng
                loadInvestStorageDetailData(detailModel);
                JOptionPane.showMessageDialog(this, "Đã cập nhật giá thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật giá: " + ex.getMessage());
            }
        });
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
                // Lấy nguồn tiền
                String source = "";
                String sqlSource = "SELECT ic_month FROM INCOME WHERE IncomeID = ?";
                PreparedStatement stmtSource = connection.prepareStatement(sqlSource);
                stmtSource.setInt(1, trans.getIncomeId());
                ResultSet rsSource = stmtSource.executeQuery();
                if (rsSource.next()) source = rsSource.getString("ic_month");
                row.add(source);
                row.add(trans.getTransAmount()); // Số tiền mua
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