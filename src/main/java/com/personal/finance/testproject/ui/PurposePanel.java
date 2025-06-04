package com.personal.finance.testproject.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import com.personal.finance.testproject.util.DatabaseConnection;
import java.math.BigDecimal;

public class PurposePanel extends JPanel {
    private final int userId;
    private Connection connection;
    private ManageSearchFrame parentFrame;
    private static final Color MAIN_BG = new Color(0x000000);
    private static final Color PANEL_BG = new Color(0xE0E0E0);
    private static final Color TABLE_BG = Color.WHITE;
    private static final Color TEXT_DARK = Color.BLACK;
    private static final Color ACCENT = new Color(0x2E2E5D);

    public PurposePanel(int userId, ManageSearchFrame parentFrame) {
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
        GridBagConstraints gbcBack = new GridBagConstraints();
        gbcBack.gridx = 0; gbcBack.gridy = 0; gbcBack.anchor = GridBagConstraints.WEST; gbcBack.insets = new Insets(10, 10, 0, 0);
        add(btnBack, gbcBack);
        // Bảng mục đích
        String[] columnNames = {"ID", "Tên mục đích", "Số tiền dự kiến", "Mô tả", "Trạng thái", "Sửa", "Xóa"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5 || col == 6; }
        };
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setForeground(Color.BLACK);
                return c;
            }
        };
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(0x00AEEF));
        table.getTableHeader().setForeground(TEXT_DARK);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setBorder(BorderFactory.createLineBorder(new Color(0xD3D3D3)));
        table.setShowGrid(true);
        table.setGridColor(new Color(0xD3D3D3));
        table.setSelectionBackground(new Color(0xD3D3D3));
        table.setSelectionForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 220));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xD3D3D3), 1, true));
        // Form thêm mục đích
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.insets = new Insets(5, 5, 5, 5);
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        JTextField nameField = new JTextField(15);
        JTextField amountField = new JTextField(10);
        JTextField descField = new JTextField(20);
        JButton btnAdd = new JButton("Thêm mục đích");
        btnAdd.setBackground(new Color(0x008BCF));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbcForm.gridx = 0; gbcForm.gridy = 0;
        formPanel.add(new JLabel("Tên mục đích:"), gbcForm);
        gbcForm.gridx = 1;
        formPanel.add(nameField, gbcForm);
        gbcForm.gridx = 0; gbcForm.gridy = 1;
        formPanel.add(new JLabel("Số tiền dự kiến:"), gbcForm);
        gbcForm.gridx = 1;
        formPanel.add(amountField, gbcForm);
        gbcForm.gridx = 0; gbcForm.gridy = 2;
        formPanel.add(new JLabel("Mô tả:"), gbcForm);
        gbcForm.gridx = 1;
        formPanel.add(descField, gbcForm);
        gbcForm.gridx = 0; gbcForm.gridy = 3;
        gbcForm.gridwidth = 2;
        formPanel.add(btnAdd, gbcForm);
        // Đặt form và bảng vào panel chính
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);
        add(formPanel, gbc);
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        // Load dữ liệu mục đích
        loadPurposeData(model);
        // Thêm mục đích
        btnAdd.addActionListener(e -> {
            try {
                String sql = "INSERT INTO PURPOSE (UserID, purpose_name, estimate_amount, description, Purpose_state) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setString(2, nameField.getText());
                stmt.setBigDecimal(3, new java.math.BigDecimal(amountField.getText()));
                stmt.setString(4, descField.getText());
                stmt.setBoolean(5, false);
                stmt.executeUpdate();
                loadPurposeData(model);
                nameField.setText("");
                amountField.setText("");
                descField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm mục đích: " + ex.getMessage());
            }
        });
        // Renderer và Editor cho nút Sửa/Xóa
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Sửa"));
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), model, this, true));
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Xóa"));
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), model, this, false));
    }

    private void loadPurposeData(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT PurposeID, purpose_name, estimate_amount, description, Purpose_state FROM PURPOSE WHERE UserID = ? ORDER BY PurposeID DESC";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("PurposeID"));
                row.add(rs.getString("purpose_name"));
                row.add(rs.getBigDecimal("estimate_amount"));
                row.add(rs.getString("description"));
                row.add(rs.getBoolean("Purpose_state") ? "Hoàn thành" : "Chưa hoàn thành");
                row.add("Sửa");
                row.add("Xóa");
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu mục đích: " + e.getMessage());
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
        private PurposePanel parent;
        private boolean isEdit;
        public ButtonEditor(JCheckBox checkBox, DefaultTableModel model, PurposePanel parent, boolean isEdit) {
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
                int purposeId = (int) model.getValueAt(row, 0);
                if (isEdit) {
                    // Sửa mục đích: popup nhập lại tên, số tiền, mô tả
                    String oldName = (String) model.getValueAt(row, 1);
                    BigDecimal oldAmount = (BigDecimal) model.getValueAt(row, 2);
                    String oldDesc = (String) model.getValueAt(row, 3);
                    JTextField nameField = new JTextField(oldName);
                    JTextField amountField = new JTextField(oldAmount.toString());
                    JTextField descField = new JTextField(oldDesc);
                    JPanel panel = new JPanel(new GridLayout(3,2));
                    panel.add(new JLabel("Tên mục đích:")); panel.add(nameField);
                    panel.add(new JLabel("Số tiền dự kiến:")); panel.add(amountField);
                    panel.add(new JLabel("Mô tả:")); panel.add(descField);
                    int result = JOptionPane.showConfirmDialog(parent, panel, "Sửa mục đích", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            String sql = "UPDATE PURPOSE SET purpose_name=?, estimate_amount=?, description=? WHERE PurposeID=? AND UserID=?";
                            PreparedStatement stmt = connection.prepareStatement(sql);
                            stmt.setString(1, nameField.getText());
                            stmt.setBigDecimal(2, new BigDecimal(amountField.getText()));
                            stmt.setString(3, descField.getText());
                            stmt.setInt(4, purposeId);
                            stmt.setInt(5, userId);
                            stmt.executeUpdate();
                            loadPurposeData(model);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(parent, "Lỗi sửa mục đích: " + ex.getMessage());
                        }
                    }
                } else {
                    // Xóa mục đích
                    int confirm = JOptionPane.showConfirmDialog(parent, "Bạn có chắc chắn muốn xóa mục đích này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String sql = "DELETE FROM PURPOSE WHERE PurposeID=? AND UserID=?";
                            PreparedStatement stmt = connection.prepareStatement(sql);
                            stmt.setInt(1, purposeId);
                            stmt.setInt(2, userId);
                            stmt.executeUpdate();
                            loadPurposeData(model);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(parent, "Lỗi xóa mục đích: " + ex.getMessage());
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
} 