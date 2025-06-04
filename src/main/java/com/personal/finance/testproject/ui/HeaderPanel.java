package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private final JButton menuButton;
    private final JLabel titleLabel;
    private final JLabel userLabel;
    private static final Color HEADER_BG = new Color(0x000000); // Đen tuyệt đối
    private static final Color TEXT_COLOR = Color.WHITE; // #FFFFFF
    private JLabel userIconLabel;

    // Hàm tiện ích để load icon an toàn
    private ImageIcon loadIcon(String path, String fallback) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            // Nếu không tìm thấy, thử icon fallback
            if (fallback != null) {
                java.net.URL fallbackURL = getClass().getClassLoader().getResource(fallback);
                if (fallbackURL != null) {
                    return new ImageIcon(fallbackURL);
                }
            }
            // Nếu vẫn không có, trả về null hoặc icon rỗng
            System.err.println("Không tìm thấy icon: " + path + ", dùng tạm icon rỗng.");
            return new ImageIcon();
        }
    }

    private String getFullNameFromDB(int userId, String username) {
        try {
            java.sql.Connection conn = com.personal.finance.testproject.util.DatabaseConnection.getConnection();
            String sql = "SELECT full_name FROM USER_INFORMATION WHERE UserID = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String fullName = rs.getString("full_name");
                if (fullName != null && !fullName.trim().isEmpty()) return fullName;
            }
        } catch (Exception e) { /* ignore, fallback */ }
        return username;
    }

    public HeaderPanel(int userId, String username) {
        setLayout(new GridBagLayout());
        setBackground(new Color(0x00AEEF));
        setPreferredSize(new Dimension(0, 60));
        setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0x008BCF)));

        // Menu button
        ImageIcon menuIcon = loadIcon("icons/menu.png", null);
        if (menuIcon != null) {
            Image img = menuIcon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            menuButton = new JButton(new ImageIcon(img));
        } else {
        menuButton = new JButton("☰");
        menuButton.setFont(new Font("Segoe UI", Font.BOLD, 24));
        }
        menuButton.setForeground(Color.WHITE);
        menuButton.setBackground(new Color(0x00AEEF));
        menuButton.setBorderPainted(false);
        menuButton.setFocusPainted(false);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuButton.setOpaque(false);

        // Title
        titleLabel = new JLabel("Quản lý tài chính & đầu tư cá nhân");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // User info
        String displayName = getFullNameFromDB(userId, username);
        userLabel = new JLabel(displayName);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        // Icon user
        userIconLabel = new JLabel();
        ImageIcon userIcon = loadIcon("icons/user.png", null);
        if (userIcon != null) {
            Image img = userIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            userIconLabel.setIcon(new ImageIcon(img));
        }
        userIconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        userIconLabel.setToolTipText("Thông tin người dùng");
        userIconLabel.setOpaque(false);
        userIconLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showUserInfoDialog();
            }
        });

        // Layout
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(new Color(0x00AEEF));
        leftPanel.add(menuButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(new Color(0x00AEEF));
        rightPanel.add(userLabel);
        rightPanel.add(userIconLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.anchor = GridBagConstraints.WEST;
        add(leftPanel, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        add(rightPanel, gbc);
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    // Hiển thị thông tin người dùng dạng layered pane
    private void showUserInfoDialog() {
        // Dialog chọn chức năng
        JDialog menuDialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Tùy chọn người dùng", true);
        menuDialog.setLayout(new BorderLayout());
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JButton btnInfo = new JButton("Xem thông tin người dùng");
        btnInfo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnInfo.setBackground(new Color(0x008BCF));
        btnInfo.setForeground(Color.WHITE);
        btnInfo.setFocusPainted(false);
        btnInfo.setBorderPainted(false);
        btnInfo.setOpaque(true);
        btnInfo.setContentAreaFilled(true);
        btnInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(btnInfo);
        menuPanel.add(Box.createVerticalStrut(20));
        menuDialog.add(menuPanel, BorderLayout.CENTER);
        menuDialog.setSize(300, 150);
        menuDialog.setLocationRelativeTo(this);
        btnInfo.addActionListener(e -> {
            menuDialog.dispose();
            showUserInfoLayeredPane();
        });
        menuDialog.setVisible(true);
    }

    // Hiển thị thông tin người dùng với các nút sửa/xóa/thêm
    private void showUserInfoLayeredPane() {
        try {
            int userId = getUserIdFromContext();
            java.sql.Connection conn = com.personal.finance.testproject.util.DatabaseConnection.getConnection();
            String sql = "SELECT * FROM user_information WHERE UserID = ?";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            java.sql.ResultSet rs = stmt.executeQuery();
            boolean hasInfo = false;
            String fullName = "", dob = "", country = "", city = "", phone = "";
            if (rs.next()) {
                fullName = rs.getString("full_name");
                dob = rs.getDate("date_of_birth") != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(rs.getDate("date_of_birth")) : "";
                country = rs.getString("Country");
                city = rs.getString("City");
                phone = rs.getString("PhoneNumber");
                hasInfo = !(isEmpty(fullName) && isEmpty(dob) && isEmpty(country) && isEmpty(city) && isEmpty(phone));
            }
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
            JLabel title = new JLabel("Thông tin người dùng");
            title.setFont(new Font("Segoe UI", Font.BOLD, 20));
            title.setForeground(new Color(0x2E2E5D));
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(title);
            panel.add(Box.createVerticalStrut(20));
            JLabel[] labels = new JLabel[5];
            String[] fields = {"Họ tên", "Ngày sinh", "Quốc gia", "Thành phố", "Số điện thoại"};
            String[] values = {fullName, dob, country, city, phone};
            for (int i = 0; i < fields.length; i++) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
                row.setBackground(Color.WHITE);
                JLabel lbl = new JLabel(fields[i] + ":");
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
                JLabel val = new JLabel(values[i]);
                val.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                row.add(lbl);
                row.add(val);
                panel.add(row);
                labels[i] = val;
            }
            panel.add(Box.createVerticalStrut(20));
            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
            if (hasInfo) {
                JButton btnEdit = new JButton("Sửa thông tin");
                btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btnEdit.setBackground(new Color(0x008BCF));
                btnEdit.setForeground(Color.WHITE);
                btnEdit.setFocusPainted(false);
                btnEdit.setBorderPainted(false);
                btnEdit.setOpaque(true);
                btnEdit.setContentAreaFilled(true);
                btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnEdit.addActionListener(e -> editUserInfo(userId, labels));
                JButton btnDelete = new JButton("Xóa thông tin");
                btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btnDelete.setBackground(new Color(0x008BCF));
                btnDelete.setForeground(Color.WHITE);
                btnDelete.setFocusPainted(false);
                btnDelete.setBorderPainted(false);
                btnDelete.setOpaque(true);
                btnDelete.setContentAreaFilled(true);
                btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnDelete.addActionListener(e -> deleteUserInfo(userId));
                btnPanel.add(btnEdit);
                btnPanel.add(btnDelete);
            } else {
                JButton btnAdd = new JButton("Thêm thông tin");
                btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 18));
                btnAdd.setBackground(new Color(0x008BCF));
                btnAdd.setForeground(Color.WHITE);
                btnAdd.setFocusPainted(false);
                btnAdd.setBorderPainted(false);
                btnAdd.setOpaque(true);
                btnAdd.setContentAreaFilled(true);
                btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btnAdd.addActionListener(e -> addUserInfo(userId));
                btnPanel.add(btnAdd);
            }
            panel.add(btnPanel);
            JDialog dialog = new JDialog((JFrame)SwingUtilities.getWindowAncestor(this), "Thông tin người dùng", true);
            dialog.setContentPane(panel);
            dialog.setSize(500, 420);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy thông tin người dùng: " + ex.getMessage());
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Hàm sửa thông tin người dùng
    private void editUserInfo(int userId, JLabel[] labels) {
        JTextField tfName = new JTextField(labels[0].getText(), 20);
        JTextField tfDob = new JTextField(labels[1].getText(), 20);
        JTextField tfCountry = new JTextField(labels[2].getText(), 20);
        JTextField tfCity = new JTextField(labels[3].getText(), 20);
        JTextField tfPhone = new JTextField(labels[4].getText(), 20);
        JPanel editPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        editPanel.add(new JLabel("Họ tên:")); editPanel.add(tfName);
        editPanel.add(new JLabel("Ngày sinh (dd/MM/yyyy):")); editPanel.add(tfDob);
        editPanel.add(new JLabel("Quốc gia:")); editPanel.add(tfCountry);
        editPanel.add(new JLabel("Thành phố:")); editPanel.add(tfCity);
        editPanel.add(new JLabel("Số điện thoại:")); editPanel.add(tfPhone);
        int result = JOptionPane.showConfirmDialog(this, editPanel, "Sửa thông tin người dùng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE user_information SET full_name=?, date_of_birth=?, Country=?, City=?, PhoneNumber=? WHERE UserID=?";
                java.sql.Connection conn = com.personal.finance.testproject.util.DatabaseConnection.getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tfName.getText().trim());
                java.util.Date dob = null;
                try { dob = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(tfDob.getText().trim()); } catch (Exception ignore) {}
                if (dob != null) stmt.setDate(2, new java.sql.Date(dob.getTime())); else stmt.setNull(2, java.sql.Types.DATE);
                stmt.setString(3, tfCountry.getText().trim());
                stmt.setString(4, tfCity.getText().trim());
                stmt.setString(5, tfPhone.getText().trim());
                stmt.setInt(6, userId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin thành công!");
                showUserInfoLayeredPane();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin: " + ex.getMessage());
            }
        }
    }

    // Hàm xóa thông tin người dùng
    private void deleteUserInfo(int userId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa toàn bộ thông tin người dùng?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "UPDATE user_information SET full_name='', date_of_birth=NULL, Country='', City='', PhoneNumber='' WHERE UserID=?";
                java.sql.Connection conn = com.personal.finance.testproject.util.DatabaseConnection.getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đã xóa thông tin người dùng!");
                showUserInfoLayeredPane();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa thông tin: " + ex.getMessage());
            }
        }
    }

    // Hàm thêm thông tin người dùng
    private void addUserInfo(int userId) {
        JTextField tfName = new JTextField(20);
        JTextField tfDob = new JTextField(20);
        JTextField tfCountry = new JTextField(20);
        JTextField tfCity = new JTextField(20);
        JTextField tfPhone = new JTextField(20);
        JPanel addPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        addPanel.add(new JLabel("Họ tên:")); addPanel.add(tfName);
        addPanel.add(new JLabel("Ngày sinh (dd/MM/yyyy):")); addPanel.add(tfDob);
        addPanel.add(new JLabel("Quốc gia:")); addPanel.add(tfCountry);
        addPanel.add(new JLabel("Thành phố:")); addPanel.add(tfCity);
        addPanel.add(new JLabel("Số điện thoại:")); addPanel.add(tfPhone);
        int result = JOptionPane.showConfirmDialog(this, addPanel, "Thêm thông tin người dùng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE user_information SET full_name=?, date_of_birth=?, Country=?, City=?, PhoneNumber=? WHERE UserID=?";
                java.sql.Connection conn = com.personal.finance.testproject.util.DatabaseConnection.getConnection();
                java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, tfName.getText().trim());
                java.util.Date dob = null;
                try { dob = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(tfDob.getText().trim()); } catch (Exception ignore) {}
                if (dob != null) stmt.setDate(2, new java.sql.Date(dob.getTime())); else stmt.setNull(2, java.sql.Types.DATE);
                stmt.setString(3, tfCountry.getText().trim());
                stmt.setString(4, tfCity.getText().trim());
                stmt.setString(5, tfPhone.getText().trim());
                stmt.setInt(6, userId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Đã thêm thông tin thành công!");
                showUserInfoLayeredPane();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm thông tin: " + ex.getMessage());
            }
        }
    }

    // Hàm giả định lấy userId, cần sửa lại cho đúng context thực tế
    private int getUserIdFromContext() {
        // TODO: Lấy userId từ MainFrame hoặc context thực tế
        return 1;
    }
} 