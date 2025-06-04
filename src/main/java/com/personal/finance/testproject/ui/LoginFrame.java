package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.dao.impl.UserDAOImpl;
import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.service.AuthService;
import com.personal.finance.testproject.service.impl.AuthServiceImpl;
import com.personal.finance.testproject.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private AuthService authService;
    private LoginPanel loginPanel;
    private MainFrame mainFrame;
    private RegisterFrame registerFrame;
    private ChangePasswordFrame changePasswordFrame;
    private Image bgImg = null;
    private boolean bgLoaded = false;
    private JPanel backgroundPanel;

    public LoginFrame() {
        try {
            // Initialize database connection and services
            Connection connection = DatabaseConnection.getConnection();
            authService = new AuthServiceImpl(new UserDAOImpl(connection));

            // Setup frame
            setTitle("Đăng nhập - Quản Lý tài chính và đầu tư");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int frameW = 900, frameH = 540;
            int loginW = (int)(frameW * 0.32);
            int loginH = 480;
            setLayout(new BorderLayout());

            // Tạo backgroundPanel trước, chưa có ảnh
            backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Luôn fill nền trước (màu chủ đạo, ví dụ trắng hoặc xanh nhạt)
                    g.setColor(new Color(0xF8FBFF)); // màu nền chủ đạo, giống màu trắng của background.png
                    g.fillRect(0, 0, frameW, frameH);
                    if (bgLoaded && bgImg != null) {
                        g.drawImage(bgImg, 0, 0, frameW, frameH, null);
                    }
                }
            };
            backgroundPanel.setOpaque(true);
            backgroundPanel.setBounds(0, 0, frameW, frameH);

            // Sử dụng JLayeredPane để lồng LoginPanel trên background
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(frameW, frameH));
            layeredPane.add(backgroundPanel, Integer.valueOf(0));

            // LoginPanel wrapper (panel trắng bo góc, nhỏ gọn, căn sát phải, giữa dọc)
            loginPanel = new LoginPanel(authService);
            loginPanel.setOpaque(false);
            JPanel loginWrapper = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.97f));
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    // Shadow
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.10f));
                    g2.setColor(Color.BLACK);
                    g2.fillRoundRect(6, 6, getWidth()-12, getHeight()-12, 20, 20);
                    g2.dispose();
                }
            };
            loginWrapper.setOpaque(false);
            loginWrapper.setPreferredSize(new Dimension(loginW, loginH));
            loginWrapper.setMaximumSize(new Dimension(loginW+10, loginH+20));
            loginWrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            loginWrapper.add(loginPanel, BorderLayout.CENTER);
            int marginRight = 30;
            int loginX = frameW - loginW - marginRight;
            int loginY = (frameH - loginH) / 2;
            loginWrapper.setBounds(loginX, loginY, loginW, loginH);
            layeredPane.add(loginWrapper, Integer.valueOf(1));
            setContentPane(layeredPane);
            setSize(frameW, frameH);
            setMinimumSize(new Dimension(frameW, frameH));
            setLocationRelativeTo(null);

            // Đăng ký listener như cũ
            loginPanel.setLoginListener(new LoginPanel.LoginListener() {
                @Override
                public void onLoginSuccess(User user) {
                    SwingUtilities.invokeLater(() -> {
                        // Hide login frame
                        setVisible(false);

                        // Create and show main frame
                        if (mainFrame == null) {
                            mainFrame = new MainFrame(user, authService);
                            mainFrame.setLogoutListener(() -> {
                                // Handle logout
                                mainFrame.dispose();
                                mainFrame = null;
                                loginPanel.clearFields();
                                setVisible(true);
                            });
                        }
                        mainFrame.setVisible(true);
                    });
                }

                @Override
                public void onRegisterClick() {
                    SwingUtilities.invokeLater(() -> {
                        // Hide login frame
                        setVisible(false);

                        // Create and show register frame
                        if (registerFrame == null) {
                            registerFrame = new RegisterFrame(authService);
                            registerFrame.setRegisterListener(() -> {
                                // Handle register success
                                registerFrame.dispose();
                                registerFrame = null;
                                loginPanel.clearFields();
                                setVisible(true);
                            });
                        }
                        registerFrame.setVisible(true);
                    });
                }

                @Override
                public void onChangePasswordClick() {
                    SwingUtilities.invokeLater(() -> {
                        // Get username from login panel
                        String username = loginPanel.getUsername();
                        if (username == null || username.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(LoginFrame.this,
                                "Vui lòng nhập tên đăng nhập trước khi đổi mật khẩu",
                                "Thông báo",
                                JOptionPane.WARNING_MESSAGE);
                            loginPanel.requestUsernameFocus();
                            return;
                        }

                        // Create and show change password frame
                        if (changePasswordFrame == null) {
                            changePasswordFrame = new ChangePasswordFrame(authService, username.trim());
                            changePasswordFrame.setChangePasswordListener(() -> {
                                // Handle change password success
                                changePasswordFrame.dispose();
                                changePasswordFrame = null;
                                loginPanel.clearFields();
                                setVisible(true);
                            });
                        }
                        changePasswordFrame.setVisible(true);
                    });
                }
            });

            // Load background bất đồng bộ
            new SwingWorker<Image, Void>() {
                @Override
                protected Image doInBackground() {
                    try {
                        // Thử lần lượt các cách như cũ
                        java.net.URL bgUrl = getClass().getResource("/loginbackground/background.png");
                        if (bgUrl == null) {
                            bgUrl = Thread.currentThread().getContextClassLoader().getResource("loginbackground/background.png");
                        }
                        if (bgUrl != null) {
                            ImageIcon icon = new ImageIcon(bgUrl);
                            int bgImgW = icon.getIconWidth();
                            int bgImgH = icon.getIconHeight();
                            float scaleW = (float)frameW / bgImgW;
                            float scaleH = (float)frameH / bgImgH;
                            float scale = Math.max(scaleW, scaleH);
                            int scaledW = (int)(bgImgW * scale);
                            int scaledH = (int)(bgImgH * scale);
                            return icon.getImage().getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
                        }
                        // getResourceAsStream
                        java.io.InputStream is = getClass().getResourceAsStream("/loginbackground/background.png");
                        if (is != null) {
                            java.awt.image.BufferedImage bimg = javax.imageio.ImageIO.read(is);
                            if (bimg != null) {
                                int bgImgW = bimg.getWidth();
                                int bgImgH = bimg.getHeight();
                                float scaleW = (float)frameW / bgImgW;
                                float scaleH = (float)frameH / bgImgH;
                                float scale = Math.max(scaleW, scaleH);
                                int scaledW = (int)(bgImgW * scale);
                                int scaledH = (int)(bgImgH * scale);
                                return bimg.getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
                            }
                        }
                        // File hệ thống
                        String absPath = System.getProperty("user.dir") + "/src/main/resources/loginbackground/background.png";
                        java.io.File file = new java.io.File(absPath);
                        if (file.exists()) {
                            ImageIcon icon = new ImageIcon(absPath);
                            int bgImgW = icon.getIconWidth();
                            int bgImgH = icon.getIconHeight();
                            float scaleW = (float)frameW / bgImgW;
                            float scaleH = (float)frameH / bgImgH;
                            float scale = Math.max(scaleW, scaleH);
                            int scaledW = (int)(bgImgW * scale);
                            int scaledH = (int)(bgImgH * scale);
                            return icon.getImage().getScaledInstance(scaledW, scaledH, Image.SCALE_SMOOTH);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void done() {
                    try {
                        bgImg = get();
                        bgLoaded = (bgImg != null);
                        backgroundPanel.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.execute();

            // Show frame
            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi kết nối database: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
} 