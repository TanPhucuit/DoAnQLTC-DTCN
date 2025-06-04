package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.personal.finance.testproject.util.DatabaseConnection;
import java.math.BigDecimal;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class TransactionTypePanel extends JPanel {
    private final int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0xF5F5F5);
    private static final Color PANEL_BG = new Color(0xFFFFFF);
    private static final Color TEXT_DARK = new Color(0x333333);
    private static final Color ACCENT = new Color(0x2196F3);
    private JComboBox<String> cbType;
    private ManageSearchFrame parentFrame;

    public TransactionTypePanel(int userId, ManageSearchFrame parentFrame) {
        this.userId = userId;
        this.parentFrame = parentFrame;
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(0xF5F5F5));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(0x2E2E5D));

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

        // Tab 1: Danh sách loại giao dịch
        JPanel listPanel = createTypeListPanel();
        tabbedPane.addTab("Danh sách loại giao dịch", listPanel);

 

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createTypeListPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PANEL_BG);
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        int row = 0;
        // Loại giao dịch
        JLabel lblType = new JLabel("Loại giao dịch:");
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbType = new JComboBox<>();
        cbType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbType.setPreferredSize(new Dimension(220, 28));
        cbType.setMaximumSize(new Dimension(220, 28));
        try {
            String sql = "SELECT DISTINCT type_description FROM TYPE WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cbType.addItem(rs.getString("type_description"));
            }
        } catch (SQLException e) {
            cbType.addItem("Lỗi tải loại giao dịch");
        }
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(lblType, gbc);
        gbc.gridx = 1; formPanel.add(cbType, gbc);
        row++;
        // Tháng
        JLabel lblMonth = new JLabel("Tháng:");
        lblMonth.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JComboBox<String> cbMonth = new JComboBox<>();
        cbMonth.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbMonth.setPreferredSize(new Dimension(220, 28));
        cbMonth.setMaximumSize(new Dimension(220, 28));
        for (int i = 1; i <= 12; i++) cbMonth.addItem(String.valueOf(i));
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(lblMonth, gbc);
        gbc.gridx = 1; formPanel.add(cbMonth, gbc);
        row++;
        // Nút xem tổng tiền
        JButton btnTotal = new JButton("Xem tổng tiền");
        btnTotal.setBackground(new Color(0x008BCF));
        btnTotal.setForeground(Color.WHITE);
        btnTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnTotal.setFocusPainted(false);
        btnTotal.setBorderPainted(false);
        btnTotal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTotal.setPreferredSize(new Dimension(220, 32));
        btnTotal.setMaximumSize(new Dimension(220, 32));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; formPanel.add(btnTotal, gbc);
        gbc.gridwidth = 1;
        row++;
        // Label kết quả
        JLabel lblResult = new JLabel("Tổng số tiền: ");
        lblResult.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblResult.setForeground(TEXT_DARK);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; formPanel.add(lblResult, gbc);
        gbc.gridwidth = 1;
        // Căn giữa formPanel trong mainPanel
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(formPanel);
        centerPanel.add(Box.createVerticalGlue());
        mainPanel.add(centerPanel, BorderLayout.NORTH);

        // Bảng loại giao dịch
        String[] columnNames = {"ID", "Tên loại giao dịch", "Số tiền tối đa", "Sửa", "Xóa"};
        DefaultTableModel typeModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 3 || col == 4; }
        };
        JTable typeTable = new JTable(typeModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setForeground(Color.BLACK);
                return c;
            }
        };
        typeTable.setRowHeight(32);
        typeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        typeTable.getTableHeader().setBackground(new Color(0x00AEEF));
        typeTable.getTableHeader().setForeground(TEXT_DARK);
        typeTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        typeTable.setBorder(BorderFactory.createLineBorder(new Color(0xD3D3D3)));
        typeTable.setShowGrid(true);
        typeTable.setGridColor(new Color(0xD3D3D3));
        typeTable.setSelectionBackground(new Color(0xD3D3D3));
        typeTable.setSelectionForeground(Color.BLACK);
        JScrollPane typeScroll = new JScrollPane(typeTable);
        typeScroll.setPreferredSize(new Dimension(900, 220));
        typeScroll.setBorder(BorderFactory.createLineBorder(new Color(0xD3D3D3), 1, true));
        // Load dữ liệu bảng TYPE
        loadTypeData(typeModel);
        // Renderer/Editor cho nút Sửa/Xóa
        typeTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer("Sửa"));
        typeTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), typeModel, this, true));
        typeTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("Xóa"));
        typeTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), typeModel, this, false));
        // Thêm bảng vào dưới mainPanel
        mainPanel.add(typeScroll, BorderLayout.CENTER);

        // Sự kiện nút
        btnTotal.addActionListener(e -> {
            String typeDesc = (String) cbType.getSelectedItem();
            String monthStr = (String) cbMonth.getSelectedItem();
            if (typeDesc == null || monthStr == null) {
                lblResult.setText("Tổng số tiền: ");
                return;
            }
            try {
                // Lấy TypeID từ type_description
                String sql = "SELECT TypeID FROM TYPE WHERE UserID = ? AND type_description = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, typeDesc);
                ResultSet rs = stmt.executeQuery();
                String typeId = null;
                if (rs.next()) typeId = rs.getString("TypeID");
                if (typeId == null) {
                    lblResult.setText("Tổng số tiền: 0");
                    return;
                }
                // Lấy tổng số tiền giao dịch theo loại và tháng
                sql = "SELECT COALESCE(SUM(trans_amount),0) as total FROM TRANSACTION WHERE UserID = ? AND TypeID = ? AND MONTH(trans_date) = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, typeId);
                stmt.setInt(3, Integer.parseInt(monthStr));
                rs = stmt.executeQuery();
                if (rs.next()) {
                    lblResult.setText("Tổng số tiền: " + rs.getBigDecimal("total"));
                } else {
                    lblResult.setText("Tổng số tiền: 0");
                }
            } catch (Exception ex) {
                lblResult.setText("Lỗi: " + ex.getMessage());
            }
        });

        return mainPanel;
    }

    private void loadTypeData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT TypeID, type_description, max_amount FROM TYPE WHERE UserID = ? ORDER BY TypeID DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("TypeID"));
                row.add(rs.getString("type_description"));
                row.add(rs.getBigDecimal("max_amount"));
                row.add("Sửa");
                row.add("Xóa");
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu loại giao dịch: " + e.getMessage());
        }
    }

    // Renderer cho nút
    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String label) {
            setOpaque(true);
            setText(label);
            setBackground(new Color(0x008BCF));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Editor cho nút Sửa/Xóa
    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int row;
        private DefaultTableModel model;
        private TransactionTypePanel parent;
        private boolean isEdit;
        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, TransactionTypePanel parent, boolean isEdit) {
            super(checkBox);
            this.model = model;
            this.parent = parent;
            this.isEdit = isEdit;
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
                String typeId = (String) model.getValueAt(row, 0);
                if (isEdit) {
                    // Sửa loại giao dịch
                    String oldDesc = (String) model.getValueAt(row, 1);
                    BigDecimal oldMax = (BigDecimal) model.getValueAt(row, 2);
                    JTextField descField = new JTextField(oldDesc);
                    JTextField maxField = new JTextField(oldMax != null ? oldMax.toString() : "");
                    JPanel panel = new JPanel(new GridLayout(2,2));
                    panel.add(new JLabel("Tên loại giao dịch:")); panel.add(descField);
                    panel.add(new JLabel("Số tiền tối đa:")); panel.add(maxField);
                    int result = JOptionPane.showConfirmDialog(parent, panel, "Sửa loại giao dịch", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            String sql = "UPDATE TYPE SET type_description=?, max_amount=? WHERE TypeID=? AND UserID=?";
                            PreparedStatement stmt = connection.prepareStatement(sql);
                            stmt.setString(1, descField.getText());
                            stmt.setBigDecimal(2, new BigDecimal(maxField.getText()));
                            stmt.setString(3, typeId);
                            stmt.setInt(4, userId);
                            stmt.executeUpdate();
                            loadTypeData(model);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(parent, "Lỗi sửa loại giao dịch: " + ex.getMessage());
                        }
                    }
                } else {
                    // Xóa loại giao dịch
                    int confirm = JOptionPane.showConfirmDialog(parent, "Bạn có chắc chắn muốn xóa loại giao dịch này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String sql = "DELETE FROM TYPE WHERE TypeID=? AND UserID=?";
                            PreparedStatement stmt = connection.prepareStatement(sql);
                            stmt.setString(1, typeId);
                            stmt.setInt(2, userId);
                            stmt.executeUpdate();
                            loadTypeData(model);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(parent, "Lỗi xóa loại giao dịch: " + ex.getMessage());
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

    private JPanel createTypeEditPanel() {
        JPanel editPanel = new JPanel(new GridBagLayout());
        editPanel.setBackground(PANEL_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        JLabel lblTitle = new JLabel("Thêm loại giao dịch mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        editPanel.add(lblTitle, gbc);
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        row++;
        JLabel lblDesc = new JLabel("Tên loại giao dịch:");
        lblDesc.setPreferredSize(new Dimension(120, 28));
        JTextField tfDesc = new JTextField();
        tfDesc.setPreferredSize(new Dimension(160, 28));
        gbc.gridx = 0; gbc.gridy = row; editPanel.add(lblDesc, gbc);
        gbc.gridx = 1; editPanel.add(tfDesc, gbc);
        row++;
        JLabel lblMax = new JLabel("Số tiền tối đa:");
        lblMax.setPreferredSize(new Dimension(120, 28));
        JTextField tfMax = new JTextField();
        tfMax.setPreferredSize(new Dimension(160, 28));
        gbc.gridx = 0; gbc.gridy = row; editPanel.add(lblMax, gbc);
        gbc.gridx = 1; editPanel.add(tfMax, gbc);
        row++;
        JButton btnAdd = new JButton("Thêm loại giao dịch");
        btnAdd.setBackground(new Color(0x008BCF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(180, 32));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        editPanel.add(btnAdd, gbc);
        gbc.gridwidth = 1;
        // Sự kiện thêm
        btnAdd.addActionListener(e -> {
            String desc = tfDesc.getText().trim();
            String maxStr = tfMax.getText().trim();
            if (desc.isEmpty() || maxStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }
            try {
                BigDecimal max = new BigDecimal(maxStr);
                String sql = "INSERT INTO TYPE (UserID, type_description, max_amount) VALUES (?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, desc);
                stmt.setBigDecimal(3, max);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Thêm loại giao dịch thành công!");
                tfDesc.setText("");
                tfMax.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm loại giao dịch: " + ex.getMessage());
            }
        });
        return editPanel;
    }
} 