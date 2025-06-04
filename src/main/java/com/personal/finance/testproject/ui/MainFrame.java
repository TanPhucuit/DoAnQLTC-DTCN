package com.personal.finance.testproject.ui;

import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.service.AuthService;
import com.personal.finance.testproject.util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class MainFrame extends JFrame {
    private final DrawerPanel drawerPanel;
    private final HeaderPanel headerPanel;
    private final MainContentPanel mainContentPanel;
    private final User user;
    private final AuthService authService;
    private Runnable logoutListener;
    private Connection connection;

    public MainFrame(User user, AuthService authService) {
        this.user = user;
        this.authService = authService;

        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi kết nối database: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Quản lý tài chính & đầu tư cá nhân");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Changed to handle closing manually
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Add window listener for closing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int confirm = JOptionPane.showConfirmDialog(MainFrame.this,
                    "Bạn có chắc chắn muốn đóng ứng dụng?",
                    "Xác nhận đóng",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });

        // Initialize components
        drawerPanel = new DrawerPanel();
        headerPanel = new HeaderPanel(user.getUserId(), user.getUsername());
        mainContentPanel = new MainContentPanel();

        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        setGlassPane(drawerPanel);
        drawerPanel.setVisible(false); // GlassPane mặc định ẩn

        // Set up menu button action
        headerPanel.getMenuButton().addActionListener(e -> {
            drawerPanel.setVisible(true);
            drawerPanel.toggleDrawer();
        });

        // Add content panels
        mainContentPanel.addContentPanel("manage_search", (JPanel) new ManageSearchFrame(user.getUserId()).getContentPane());
        mainContentPanel.addContentPanel("statistics", new StatisticsPanel(user.getUserId()));
        mainContentPanel.addContentPanel("financial_report", new FinancialReportPanel(user.getUserId()));
        mainContentPanel.addContentPanel("financial_status", new FinancialStatusPanel(user.getUserId()));
        mainContentPanel.addContentPanel("investment_plan", new InvestmentPlanPanel(user.getUserId()));
        mainContentPanel.addContentPanel("bank_account", new BankAccountPanel(user.getUserId()));

        // Set up drawer listener
        drawerPanel.setDrawerListener(menuKey -> {
            if (menuKey.equals("logout")) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn đăng xuất?", 
                    "Đăng xuất", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION && logoutListener != null) {
                    logoutListener.run();
                }
                drawerPanel.setVisible(false);
                return;
            }
            mainContentPanel.showPanel(menuKey);
            drawerPanel.setActiveMenu(menuKey);
            drawerPanel.setVisible(false);
        });

        // Show default panel
        mainContentPanel.showPanel("manage_search");
        drawerPanel.setActiveMenu("manage_search");
    }

    public void setLogoutListener(Runnable listener) {
        this.logoutListener = listener;
    }

    // For backward compatibility
    public MainFrame(int userId, String userName) {
        this(new User(userId, userName, null, null, "USER"), null);
    }

    @Override
    public void dispose() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.dispose();
    }
} 