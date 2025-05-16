package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.personal.finance.testproject.util.DatabaseConnection;

public class TransferLayeredPanel extends JPanel {
    private final int userId;
    private final JLayeredPane layeredPane;
    private final JPanel step1Panel, step2Panel, step3Panel;
    private JComboBox<String> sourceAccountCombo;
    private JTextField destAccountField, amountField, contentField;
    private JLabel destBankLabel, destNameLabel, errorLabel;
    private String destBank, destName;
    private String sourceAccount, destAccount, content;
    private double amount;
    private JButton nextBtn1, nextBtn2, backBtn2, newTransBtn;
    private JLabel confirmInfoLabel, successInfoLabel;
    private Connection connection;

    public TransferLayeredPanel(int userId) {
        this.userId = userId;
        setLayout(new BorderLayout());
        setBackground(Color.DARK_GRAY);
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database: " + e.getMessage());
        }
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(420, 650));
        step1Panel = createStep1Panel();
        step2Panel = createStep2Panel();
        step3Panel = createStep3Panel();
        layeredPane.add(step1Panel, Integer.valueOf(0));
        layeredPane.add(step2Panel, Integer.valueOf(1));
        layeredPane.add(step3Panel, Integer.valueOf(2));
        showStep(1);
        add(layeredPane, BorderLayout.CENTER);
    }

    private JPanel createStep1Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(24, 26, 32));
        panel.setBounds(0, 0, 420, 650);
        JLabel title = new JLabel("Chuyển tiền qua tài khoản ngân hàng", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(255, 215, 0));
        title.setBounds(0, 30, 420, 40);
        panel.add(title);
        JLabel lblSource = new JLabel("Tài khoản nguồn:");
        lblSource.setForeground(Color.WHITE);
        lblSource.setBounds(40, 90, 120, 30);
        panel.add(lblSource);
        sourceAccountCombo = new JComboBox<>();
        sourceAccountCombo.setBounds(180, 90, 200, 30);
        loadSourceAccounts();
        panel.add(sourceAccountCombo);
        JLabel lblDest = new JLabel("Tài khoản thụ hưởng:");
        lblDest.setForeground(Color.WHITE);
        lblDest.setBounds(40, 140, 140, 30);
        panel.add(lblDest);
        destAccountField = new JTextField();
        destAccountField.setBounds(180, 140, 200, 30);
        panel.add(destAccountField);
        JLabel lblBank = new JLabel("Ngân hàng thụ hưởng:");
        lblBank.setForeground(Color.WHITE);
        lblBank.setBounds(40, 190, 140, 30);
        panel.add(lblBank);
        destBankLabel = new JLabel("");
        destBankLabel.setForeground(new Color(255, 215, 0));
        destBankLabel.setBounds(180, 190, 200, 30);
        panel.add(destBankLabel);
        JLabel lblName = new JLabel("Tên người nhận:");
        lblName.setForeground(Color.WHITE);
        lblName.setBounds(40, 240, 140, 30);
        panel.add(lblName);
        destNameLabel = new JLabel("");
        destNameLabel.setForeground(new Color(255, 215, 0));
        destNameLabel.setBounds(180, 240, 200, 30);
        panel.add(destNameLabel);
        JLabel lblAmount = new JLabel("Số tiền:");
        lblAmount.setForeground(Color.WHITE);
        lblAmount.setBounds(40, 290, 140, 30);
        panel.add(lblAmount);
        amountField = new JTextField();
        amountField.setBounds(180, 290, 200, 30);
        panel.add(amountField);
        JLabel lblContent = new JLabel("Nội dung:");
        lblContent.setForeground(Color.WHITE);
        lblContent.setBounds(40, 340, 140, 30);
        panel.add(lblContent);
        contentField = new JTextField();
        contentField.setBounds(180, 340, 200, 30);
        panel.add(contentField);
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(40, 380, 340, 30);
        panel.add(errorLabel);
        nextBtn1 = new JButton("Xác nhận");
        nextBtn1.setBackground(new Color(255, 215, 0));
        nextBtn1.setBounds(120, 430, 180, 40);
        nextBtn1.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextBtn1.addActionListener(e -> handleStep1Next());
        panel.add(nextBtn1);
        destAccountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                checkDestAccount();
            }
        });
        return panel;
    }

    private JPanel createStep2Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(24, 26, 32));
        panel.setBounds(0, 0, 420, 650);
        JLabel title = new JLabel("Xác nhận giao dịch", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(255, 215, 0));
        title.setBounds(0, 30, 420, 40);
        panel.add(title);
        confirmInfoLabel = new JLabel("", SwingConstants.CENTER);
        confirmInfoLabel.setForeground(Color.WHITE);
        confirmInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        confirmInfoLabel.setBounds(40, 100, 340, 200);
        panel.add(confirmInfoLabel);
        nextBtn2 = new JButton("Thực hiện giao dịch");
        nextBtn2.setBackground(new Color(255, 215, 0));
        nextBtn2.setBounds(120, 350, 180, 40);
        nextBtn2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextBtn2.addActionListener(e -> handleStep2Next());
        panel.add(nextBtn2);
        backBtn2 = new JButton("Quay lại");
        backBtn2.setBounds(120, 410, 180, 32);
        backBtn2.addActionListener(e -> showStep(1));
        panel.add(backBtn2);
        return panel;
    }

    private JPanel createStep3Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(24, 26, 32));
        panel.setBounds(0, 0, 420, 650);

        // Icon thành công/thất bại
        JLabel iconLabel = new JLabel();
        iconLabel.setBounds(160, 20, 100, 100);
        panel.add(iconLabel);

        // Dòng trạng thái chuyển khoản
        JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        statusLabel.setBounds(0, 120, 420, 32);
        panel.add(statusLabel);

        // Số tiền nổi bật
        JLabel amountLabel = new JLabel("", SwingConstants.CENTER);
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        amountLabel.setForeground(new Color(0, 200, 83));
        amountLabel.setBounds(0, 165, 420, 40);
        panel.add(amountLabel);

        // Thời gian
        JLabel timeLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        timeLabel.setForeground(Color.LIGHT_GRAY);
        timeLabel.setBounds(0, 210, 420, 25);
        panel.add(timeLabel);

        // Thông tin chi tiết (dùng HTML để căn lề, xuống dòng đẹp)
        JLabel infoLabel = new JLabel("", SwingConstants.LEFT);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBounds(40, 250, 340, 120);
        panel.add(infoLabel);

        JButton newTransBtn = new JButton("Thực hiện giao dịch mới");
        newTransBtn.setBackground(new Color(255, 215, 0));
        newTransBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        newTransBtn.setBounds(90, 400, 240, 40);
        newTransBtn.setFocusPainted(false);
        newTransBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2, true));
        newTransBtn.addActionListener(e -> resetForm());
        panel.add(newTransBtn);

        // Lưu các label để cập nhật khi chuyển khoản thành công/thất bại
        this.successInfoLabel = new JLabel(); // dummy, không dùng nữa
        this.successInfoLabel.putClientProperty("iconLabel", iconLabel);
        this.successInfoLabel.putClientProperty("statusLabel", statusLabel);
        this.successInfoLabel.putClientProperty("amountLabel", amountLabel);
        this.successInfoLabel.putClientProperty("timeLabel", timeLabel);
        this.successInfoLabel.putClientProperty("infoLabel", infoLabel);
        return panel;
    }

    private void showStep(int step) {
        step1Panel.setVisible(step == 1);
        step2Panel.setVisible(step == 2);
        step3Panel.setVisible(step == 3);
    }

    private void loadSourceAccounts() {
        try {
            String sql = "SELECT BankAccountNumber, BankName FROM BANK_ACCOUNT WHERE UserID = ? AND BankAccountNumber NOT LIKE 'INCOME%' AND BankAccountNumber NOT LIKE 'INVEST%'";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String acc = rs.getString("BankAccountNumber");
                String bank = rs.getString("BankName");
                sourceAccountCombo.addItem(acc + " - " + bank);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải tài khoản nguồn: " + e.getMessage());
        }
    }

    private void checkDestAccount() {
        String acc = destAccountField.getText().trim();
        if (acc.isEmpty()) {
            destBankLabel.setText("");
            destNameLabel.setText("");
            return;
        }
        try {
            String sql = "SELECT BankName, UserID FROM BANK_ACCOUNT WHERE BankAccountNumber = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, acc);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                destBank = rs.getString("BankName");
                int destUserId = rs.getInt("UserID");
                destBankLabel.setText(destBank);
                // Lấy tên người nhận nếu có
                String name = "";
                PreparedStatement stmt2 = connection.prepareStatement("SELECT full_name FROM USER_INFORMATION WHERE UserID = ?");
                stmt2.setInt(1, destUserId);
                ResultSet rs2 = stmt2.executeQuery();
                if (rs2.next()) name = rs2.getString("full_name");
                destName = name;
                destNameLabel.setText(name);
                errorLabel.setText("");
            } else {
                destBankLabel.setText("Không hợp lệ");
                destNameLabel.setText("");
                errorLabel.setText("Số tài khoản thụ hưởng không tồn tại!");
            }
        } catch (SQLException e) {
            errorLabel.setText("Lỗi kiểm tra tài khoản: " + e.getMessage());
        }
    }

    private void handleStep1Next() {
        errorLabel.setText("");
        sourceAccount = (String) sourceAccountCombo.getSelectedItem();
        if (sourceAccount == null) {
            errorLabel.setText("Chưa chọn tài khoản nguồn!");
            return;
        }
        destAccount = destAccountField.getText().trim();
        if (destAccount.isEmpty() || destBankLabel.getText().equals("Không hợp lệ")) {
            errorLabel.setText("Số tài khoản thụ hưởng không hợp lệ!");
            return;
        }
        try {
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                errorLabel.setText("Số tiền phải lớn hơn 0!");
                return;
            }
        } catch (Exception e) {
            errorLabel.setText("Số tiền không hợp lệ!");
            return;
        }
        content = contentField.getText().trim();
        // Kiểm tra số dư tài khoản nguồn
        try {
            String accNum = sourceAccount.split(" - ")[0];
            String sql = "SELECT Balance FROM BANK_ACCOUNT WHERE BankAccountNumber = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, accNum);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("Balance");
                if (balance < amount) {
                    errorLabel.setText("Số dư không đủ!");
                    return;
                }
            }
        } catch (SQLException e) {
            errorLabel.setText("Lỗi kiểm tra số dư: " + e.getMessage());
            return;
        }
        // Hiển thị thông tin xác nhận
        confirmInfoLabel.setText("Từ: " + sourceAccount +
                "<br>Đến: " + destAccount + " - " + destBank +
                (destName != null && !destName.isEmpty() ? "<br>Tên: " + destName : "") +
                "<br>Số tiền: " + String.format("%,.0f", amount) +
                "<br>Nội dung: " + content);
        showStep(2);
    }

    private void handleStep2Next() {
        // Thực hiện chuyển khoản và lưu lịch sử
        boolean success = false;
        String errorMsg = "";
        String transId = "";
        try {
            String srcAcc = sourceAccount.split(" - ")[0];
            // Trừ tiền tài khoản nguồn
            PreparedStatement stmt1 = connection.prepareStatement("UPDATE BANK_ACCOUNT SET Balance = Balance - ? WHERE BankAccountNumber = ?");
            stmt1.setDouble(1, amount);
            stmt1.setString(2, srcAcc);
            int updated1 = stmt1.executeUpdate();
            // Cộng tiền tài khoản đích
            PreparedStatement stmt2 = connection.prepareStatement("UPDATE BANK_ACCOUNT SET Balance = Balance + ? WHERE BankAccountNumber = ?");
            stmt2.setDouble(1, amount);
            stmt2.setString(2, destAccount);
            int updated2 = stmt2.executeUpdate();
            if (updated1 == 1 && updated2 == 1) {
                // Lưu lịch sử giao dịch
                PreparedStatement stmt3 = connection.prepareStatement("INSERT INTO BANK_TRANSFER (SourceBankAccountNumber, TargetBankAccountNumber, Transfer_amount, Transfer_date) VALUES (?, ?, ?, NOW())", Statement.RETURN_GENERATED_KEYS);
                stmt3.setString(1, srcAcc);
                stmt3.setString(2, destAccount);
                stmt3.setDouble(3, amount);
                stmt3.executeUpdate();
                ResultSet rs = stmt3.getGeneratedKeys();
                if (rs.next()) transId = rs.getString(1);
                success = true;
            } else {
                errorMsg = "Không thể cập nhật số dư tài khoản.";
            }
        } catch (SQLException e) {
            errorMsg = e.getMessage();
        }
        // Lấy các label để cập nhật
        JLabel iconLabel = (JLabel) this.successInfoLabel.getClientProperty("iconLabel");
        JLabel statusLabel = (JLabel) this.successInfoLabel.getClientProperty("statusLabel");
        JLabel amountLabel = (JLabel) this.successInfoLabel.getClientProperty("amountLabel");
        JLabel timeLabel = (JLabel) this.successInfoLabel.getClientProperty("timeLabel");
        JLabel infoLabel = (JLabel) this.successInfoLabel.getClientProperty("infoLabel");
        // Cập nhật thông tin
        if (success) {
            // Icon thành công
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/safe_status.png"));
            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            }
            statusLabel.setText("CHUYỂN KHOẢN THÀNH CÔNG");
            statusLabel.setForeground(new Color(0, 200, 83));
            amountLabel.setText(String.format("%s VND", String.format("%,.0f", amount)));
            amountLabel.setForeground(new Color(0, 200, 83));
            timeLabel.setText(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy").format(java.time.LocalDateTime.now()));
            StringBuilder info = new StringBuilder();
            info.append("<html>");
            info.append("<b>Tên người thụ hưởng:</b> " + (destName != null && !destName.isEmpty() ? destName : "---") + "<br>");
            info.append("<b>Tài khoản thụ hưởng:</b> " + destAccount + "<br>");
            info.append("<b>Ngân hàng thụ hưởng:</b> " + (destBank != null ? destBank : "---") + "<br>");
            info.append("<b>Mã giao dịch:</b> " + transId + "<br>");
            info.append("<b>Nội dung:</b> " + (content != null && !content.isEmpty() ? content : "---") + "");
            info.append("</html>");
            infoLabel.setText(info.toString());
        } else {
            // Icon thất bại
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icons/dangerous_status.png"));
            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            }
            statusLabel.setText("CHUYỂN KHOẢN THẤT BẠI");
            statusLabel.setForeground(Color.RED);
            amountLabel.setText(String.format("%s VND", String.format("%,.0f", amount)));
            amountLabel.setForeground(Color.RED);
            timeLabel.setText(java.time.format.DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy").format(java.time.LocalDateTime.now()));
            StringBuilder info = new StringBuilder();
            info.append("<html>");
            info.append("<b>Lý do:</b> " + errorMsg + "<br>");
            info.append("<b>Tài khoản nguồn:</b> " + (sourceAccount != null ? sourceAccount : "---") + "<br>");
            info.append("<b>Tài khoản thụ hưởng:</b> " + destAccount + "<br>");
            info.append("<b>Ngân hàng thụ hưởng:</b> " + (destBank != null ? destBank : "---") + "<br>");
            info.append("<b>Nội dung:</b> " + (content != null && !content.isEmpty() ? content : "---") + "");
            info.append("</html>");
            infoLabel.setText(info.toString());
        }
        showStep(3);
    }

    private void resetForm() {
        destAccountField.setText("");
        destBankLabel.setText("");
        destNameLabel.setText("");
        amountField.setText("");
        contentField.setText("");
        errorLabel.setText("");
        showStep(1);
    }
} 