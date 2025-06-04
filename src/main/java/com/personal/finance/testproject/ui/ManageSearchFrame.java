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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManageSearchFrame extends JFrame {
    private int userId;
    private Connection connection;
    private static final Color MAIN_BG = new Color(0xFFFFFF ); // Đen tuyệt đối
    private static final Color PANEL_BG = new Color(0xE0E0E0); // Xám nhạt
    private static final Color TABLE_BG = Color.WHITE;
    private static final Color TEXT_DARK = Color.BLACK;
    private static final Color ACCENT = new Color(0x2E2E5D);
    private JButton btnBack;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

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
        setTitle("Quản lý & Tra cứu");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel);

        String[] titles = {"Thu nhập", "Khoản vay", "Tiết kiệm", "Đầu tư & Tích trữ", "Giao dịch", "Loại giao dịch", "Mục đích", "Thông tin người dùng"};
        String[] iconFiles = {"income.png", "loan.png", "save.png", "invest.png", "transaction.png", "transaction type.png", "purpose.png", "user_information.png"};
        String[] panelKeys = new String[]{"income", "loan", "saving", "invest", "transaction", "transactionType", "purpose", "userInfo"};
        JPanel[] functionPanels = new JPanel[]{
            createIncomePanel(),
            createLoanPanel(),
            createSavingPanel(),
            createInvestPanel(),
            createTransactionPanel(),
            createTransactionTypePanel(),
            new PurposePanel(userId, this),
            createUserInfoPanel()
        };

        btnBack = new JButton("← Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setBackground(new Color(0x008BCF));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setVisible(false);
        btnBack.addActionListener(e -> {
            cardLayout.show(cardPanel, "dashboard");
            btnBack.setVisible(false);
            cardPanel.revalidate();
            cardPanel.repaint();
        });
        GridBagConstraints gbcBack = new GridBagConstraints();
        gbcBack.gridx = 0; gbcBack.gridy = 0; gbcBack.anchor = GridBagConstraints.WEST; gbcBack.insets = new Insets(10, 10, 0, 0);
        mainPanel.add(btnBack, gbcBack);

        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(MAIN_BG);
        GridBagConstraints gridGbc = new GridBagConstraints();
        gridGbc.insets = new Insets(32, 32, 32, 32);
        int cols = 4;
        for (int i = 0; i < titles.length; i++) {
            JPanel box = new JPanel();
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
            box.setBackground(Color.WHITE);
            box.setPreferredSize(new Dimension(120, 120));
            box.setMaximumSize(new Dimension(120, 120));
            box.setMinimumSize(new Dimension(100, 100));
            box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE0E0E0), 2, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
            box.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Icon
            JLabel iconLabel = new JLabel();
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ImageIcon icon = new ImageIcon(getClass().getResource("/icon2/" + iconFiles[i]));
            Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(img));
            // Text
            JLabel textLabel = new JLabel(titles[i]);
            textLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            textLabel.setForeground(TEXT_DARK);
            textLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
            box.add(Box.createVerticalGlue());
            box.add(iconLabel);
            box.add(textLabel);
            box.add(Box.createVerticalGlue());
            int idx = i;
            box.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(cardPanel, panelKeys[idx]);
                    btnBack.setVisible(true);
                    cardPanel.revalidate();
                    cardPanel.repaint();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    box.setBackground(new Color(0xE0F7FA));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    box.setBackground(Color.WHITE);
                }
            });
            gridGbc.gridx = i % cols;
            gridGbc.gridy = i / cols;
            gridPanel.add(box, gridGbc);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(gridPanel, gbc);

        cardPanel.add(mainPanel, "dashboard");
        for (int i = 0; i < functionPanels.length; i++) {
            functionPanels[i].setBackground(Color.WHITE);
            cardPanel.add(functionPanels[i], panelKeys[i]);
        }
        setContentPane(cardPanel);
        cardLayout.show(cardPanel, "dashboard");
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private JPanel createIncomePanel() {
        return new IncomeManagementPanel(userId, this);
    }

    private JPanel createLoanPanel() {
        return new LoanManagementPanel(userId, this);
    }

    private JPanel createSavingPanel() {
        return new SavingManagementPanel(userId, this);
    }

    private JPanel createInvestPanel() {
        return new InvestStoragePanel(userId, this);
    }

    private JPanel createTransactionPanel() {
        return new TransactionPanel(userId, this);
    }

    private JPanel createTransactionTypePanel() {
        return new TransactionTypePanel(userId, this);
    }

    private JPanel createUserInfoPanel() {
        // Panel cha căn giữa
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbcOuter = new GridBagConstraints();
        gbcOuter.gridx = 0; gbcOuter.gridy = 0; gbcOuter.weightx = 1.0; gbcOuter.weighty = 1.0;
        gbcOuter.anchor = GridBagConstraints.CENTER;
        gbcOuter.fill = GridBagConstraints.BOTH;

        // Panel nội dung chính
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));

        // Nút quay lại
        JButton btnBack = new JButton("← Quay lại");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnBack.setBackground(new Color(0x008BCF));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBack.setMaximumSize(new Dimension(180, 44));
        btnBack.addActionListener(e -> {
            cardLayout.show(cardPanel, "dashboard");
            btnBack.setVisible(false);
            cardPanel.revalidate();
            cardPanel.repaint();
        });
        panel.add(btnBack);
        panel.add(Box.createVerticalStrut(18));

        // Lấy dữ liệu user_information
        final String[] fullName = {""};
        final String[] country = {""};
        final String[] city = {""};
        final String[] phone = {""};
        final java.sql.Date[] dob = {null};
        try {
            String sql = "SELECT * FROM USER_INFORMATION WHERE UserID = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fullName[0] = rs.getString("full_name");
                dob[0] = rs.getDate("date_of_birth");
                country[0] = rs.getString("Country");
                city[0] = rs.getString("City");
                phone[0] = rs.getString("PhoneNumber");
            }
        } catch (SQLException e) {
            JLabel err = new JLabel("Lỗi khi tải thông tin người dùng: " + e.getMessage());
            err.setFont(new Font("Segoe UI", Font.BOLD, 18));
            err.setForeground(Color.RED);
            panel.add(err);
        }
        // Tiêu đề
        JLabel lblTitle = new JLabel("Thông tin người dùng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(new Color(0x2E2E5D));
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(32));
        // Thông tin
        Font labelFont = new Font("Segoe UI", Font.BOLD, 22);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 22);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Họ tên
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        row1.setBackground(Color.WHITE);
        JLabel lblFullName = new JLabel("Họ tên:");
        lblFullName.setFont(labelFont);
        JLabel valFullName = new JLabel(fullName[0] != null ? fullName[0] : "");
        valFullName.setFont(valueFont);
        row1.add(lblFullName); row1.add(valFullName);
        infoPanel.add(row1);
        // Ngày sinh
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        row2.setBackground(Color.WHITE);
        JLabel lblDob = new JLabel("Ngày sinh:");
        lblDob.setFont(labelFont);
        JLabel valDob = new JLabel(dob[0] != null ? dob[0].toString() : "");
        valDob.setFont(valueFont);
        row2.add(lblDob); row2.add(valDob);
        infoPanel.add(row2);
        // Quốc gia
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        row3.setBackground(Color.WHITE);
        JLabel lblCountry = new JLabel("Quốc gia:");
        lblCountry.setFont(labelFont);
        JLabel valCountry = new JLabel(country[0] != null ? country[0] : "");
        valCountry.setFont(valueFont);
        row3.add(lblCountry); row3.add(valCountry);
        infoPanel.add(row3);
        // Thành phố
        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        row4.setBackground(Color.WHITE);
        JLabel lblCity = new JLabel("Thành phố:");
        lblCity.setFont(labelFont);
        JLabel valCity = new JLabel(city[0] != null ? city[0] : "");
        valCity.setFont(valueFont);
        row4.add(lblCity); row4.add(valCity);
        infoPanel.add(row4);
        // Số điện thoại
        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
        row5.setBackground(Color.WHITE);
        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setFont(labelFont);
        JLabel valPhone = new JLabel(phone[0] != null ? phone[0] : "");
        valPhone.setFont(valueFont);
        row5.add(lblPhone); row5.add(valPhone);
        infoPanel.add(row5);
        panel.add(infoPanel);
        panel.add(Box.createVerticalStrut(32));
        // Nút sửa thông tin
        JButton btnEdit = new JButton("Sửa thông tin");
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btnEdit.setBackground(new Color(0x008BCF));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setBorderPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setPreferredSize(new Dimension(220, 48));
        btnEdit.setMaximumSize(new Dimension(240, 48));
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnEdit);
        btnEdit.addActionListener(e -> {
            // Panel nhập liệu dạng label + textfield như hình
            JPanel editPanel = new JPanel(new GridBagLayout());
            editPanel.setBackground(new Color(0xF5F5F5));
            GridBagConstraints eg = new GridBagConstraints();
            eg.insets = new Insets(8, 8, 8, 8);
            eg.anchor = GridBagConstraints.WEST;
            eg.fill = GridBagConstraints.HORIZONTAL;
            eg.gridx = 0; eg.gridy = 0;
            JLabel l1 = new JLabel("Họ tên:");
            l1.setFont(labelFont);
            editPanel.add(l1, eg);
            eg.gridx = 1;
            JTextField tfFullName = new JTextField(fullName[0], 20);
            tfFullName.setFont(valueFont);
            editPanel.add(tfFullName, eg);
            eg.gridx = 0; eg.gridy++;
            JLabel l2 = new JLabel("Ngày sinh (yyyy-mm-dd):");
            l2.setFont(labelFont);
            editPanel.add(l2, eg);
            eg.gridx = 1;
            JTextField tfDob = new JTextField(dob[0] != null ? dob[0].toString() : "", 20);
            tfDob.setFont(valueFont);
            editPanel.add(tfDob, eg);
            eg.gridx = 0; eg.gridy++;
            JLabel l3 = new JLabel("Quốc gia:");
            l3.setFont(labelFont);
            editPanel.add(l3, eg);
            eg.gridx = 1;
            JTextField tfCountry = new JTextField(country[0], 20);
            tfCountry.setFont(valueFont);
            editPanel.add(tfCountry, eg);
            eg.gridx = 0; eg.gridy++;
            JLabel l4 = new JLabel("Thành phố:");
            l4.setFont(labelFont);
            editPanel.add(l4, eg);
            eg.gridx = 1;
            JTextField tfCity = new JTextField(city[0], 20);
            tfCity.setFont(valueFont);
            editPanel.add(tfCity, eg);
            eg.gridx = 0; eg.gridy++;
            JLabel l5 = new JLabel("Số điện thoại:");
            l5.setFont(labelFont);
            editPanel.add(l5, eg);
            eg.gridx = 1;
            JTextField tfPhone = new JTextField(phone[0], 20);
            tfPhone.setFont(valueFont);
            editPanel.add(tfPhone, eg);
            int result = JOptionPane.showConfirmDialog(panel, editPanel, "Sửa thông tin người dùng", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String sql = "UPDATE USER_INFORMATION SET full_name=?, date_of_birth=?, Country=?, City=?, PhoneNumber=? WHERE UserID=?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, tfFullName.getText());
                    stmt.setDate(2, tfDob.getText().isEmpty() ? null : java.sql.Date.valueOf(tfDob.getText()));
                    stmt.setString(3, tfCountry.getText());
                    stmt.setString(4, tfCity.getText());
                    stmt.setString(5, tfPhone.getText());
                    stmt.setInt(6, userId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Cập nhật thành công!");
                    // Reload lại panel
                    cardPanel.remove(outerPanel);
                    cardPanel.add(createUserInfoPanel(), "userInfo");
                    cardLayout.show(cardPanel, "userInfo");
                    cardPanel.revalidate();
                    cardPanel.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Lỗi cập nhật: " + ex.getMessage());
                }
            }
        });
        // Thêm panel nội dung vào panel cha căn giữa
        outerPanel.add(panel, gbcOuter);
        return outerPanel;
    }

    public void showDashboard() {
        cardLayout.show(cardPanel, "dashboard");
        btnBack.setVisible(false);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
} 