package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.service.AuthService;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final AuthService authService;
    private final RegisterPanel registerPanel;
    private Runnable registerListener;

    public RegisterFrame(AuthService authService) {
        this.authService = authService;
        
        setTitle("Quản lý tài chính & đầu tư cá nhân - Đăng ký");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        registerPanel = new RegisterPanel(authService);
        registerPanel.setRegisterListener(new RegisterPanel.RegisterListener() {
            @Override
            public void onRegisterSuccess() {
                if (registerListener != null) {
                    registerListener.run();
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                        "Đăng ký thành công! Vui lòng đăng nhập.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        LoginFrame loginFrame = new LoginFrame();
                        loginFrame.setVisible(true);
                    });
                }
            }

            @Override
            public void onBackToLogin() {
                dispose();
                if (registerListener != null) {
                    registerListener.run();
                } else {
                    SwingUtilities.invokeLater(() -> {
                        LoginFrame loginFrame = new LoginFrame();
                        loginFrame.setVisible(true);
                    });
                }
            }
        });

        add(registerPanel);
    }

    public void setRegisterListener(Runnable listener) {
        this.registerListener = listener;
    }
} 