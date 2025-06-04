package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DrawerPanel extends JLayeredPane {
    private static final int DRAWER_WIDTH = 250;
    private static final int ANIMATION_STEPS = 10;
    private static final int ANIMATION_DELAY = 10;
    private static final Color DRAWER_BG = new Color(0xE0E0E0); // #E0E0E0
    private static final Color MENU_TEXT = Color.WHITE; // #FFFFFF
    private static final Color ACTIVE_MENU_BG = new Color(0x008BCF); // #008BCF
    private static final Color HOVER_MENU_BG = new Color(0x008BCF); // #008BCF
    private static final Color HOVER_TEXT = Color.WHITE; // #FFFFFF

    private int currentX;
    private boolean isOpen;
    private String activeMenu;
    private Consumer<String> drawerListener;
    private Timer animationTimer;
    private JPanel contentPanel;
    private JPanel overlayPanel;
    private final Map<String, JButton> menuButtons = new HashMap<>();
    private JButton menuButton;

    public DrawerPanel() {
        setLayout(null);
        setOpaque(false);

        // Overlay panel for click-outside-to-close
        overlayPanel = new JPanel();
        overlayPanel.setBackground(new Color(0, 0, 0, 120));
        overlayPanel.setOpaque(true);
        overlayPanel.setVisible(false);
        overlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeDrawer();
            }
        });
        add(overlayPanel, JLayeredPane.DEFAULT_LAYER);

        // Content panel for menu items
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Shadow
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0,0,0,60));
                g2.fillRoundRect(getWidth()-12, 0, 12, getHeight(), 32, 32);
                g2.dispose();
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(DRAWER_BG);
        contentPanel.setBorder(new EmptyBorder(36, 0, 36, 0));
        contentPanel.setOpaque(true);
        contentPanel.setVisible(true);
        contentPanel.setBounds(currentX, 0, DRAWER_WIDTH, getHeight());
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,220), 1, true),
            new EmptyBorder(36, 0, 36, 0)
        ));
        // Add menu items
        addMenuItem("Quản lý & Tra cứu", "manage_search");
        addMenuItem("Thống kê", "statistics");
        addMenuItem("Báo cáo tài chính", "financial_report");
        addMenuItem("Trạng thái tài chính", "financial_status");
        addMenuItem("Kế hoạch đầu tư", "investment_plan");
        addMenuItem("Tài khoản ngân hàng", "bank_account");
        addMenuItem("Đăng xuất", "logout");
        add(contentPanel, JLayeredPane.PALETTE_LAYER);

        // Animation timer
        animationTimer = new Timer(ANIMATION_DELAY, e -> {
            int targetX = isOpen ? 0 : -DRAWER_WIDTH;
            int step = (targetX - currentX) / ANIMATION_STEPS;
            if (Math.abs(targetX - currentX) <= Math.abs(step)) {
                currentX = targetX;
                animationTimer.stop();
            } else {
                currentX += step;
            }
            updateDrawerPosition();
        });
        // Đảm bảo setBounds đúng ngay khi khởi tạo
        updateDrawerPosition();
        contentPanel.setVisible(false);
    }

    // Hàm tiện ích để load icon (tương tự SidebarPanel)
    private ImageIcon loadIcon(String path) {
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource(path);
            if (imgURL == null) {
                System.err.println("Couldn't find icon: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Error loading icon " + path + ": " + e.getMessage());
            return null;
        }
    }

    private void addMenuItem(String text, String menuKey) {
        JButton menuItem = new JButton(text);
        menuItem.setFont(new Font("Arial", Font.BOLD, 15));
        menuItem.setForeground(MENU_TEXT);
        menuItem.setBackground(new Color(0x00AEEF));
        menuItem.setBorderPainted(false);
        menuItem.setFocusPainted(false);
        menuItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuItem.setMaximumSize(new Dimension(DRAWER_WIDTH - 20, 44));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuItem.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 12));
        menuItem.setOpaque(true);
        menuItem.setFocusable(false);
        menuItem.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        menuItem.setContentAreaFilled(true);
        menuItem.setFocusPainted(false);
        menuItem.setRolloverEnabled(true);
        menuItem.setToolTipText(text);
        // Gán icon tương ứng
        ImageIcon icon = null;
        switch (menuKey) {
            case "manage_search": icon = loadIcon("icons/search.png"); break;
            case "statistics": icon = loadIcon("icons/statistic.png"); break;
            case "financial_report": icon = loadIcon("icons/report.png"); break;
            case "financial_status": icon = loadIcon("icons/safe_status.png"); break;
            case "investment_plan": icon = loadIcon("icons/invest&storage.png"); break;
            case "bank_account": icon = loadIcon("icons/bank_account.png"); break;
            case "logout": icon = loadIcon("icons/logout.png"); break;
            default: break;
        }
        if (icon != null) menuItem.setIcon(icon);
        // Add hover effect
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(new Color(0x008BCF));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!menuItem.getBackground().equals(new Color(0xFFC107))) {
                    menuItem.setBackground(new Color(0x00AEEF));
                }
            }
        });
        menuItem.addActionListener(e -> {
            if (drawerListener != null) {
                drawerListener.accept(menuKey);
            }
            // Tự động đóng menu khi chọn chức năng
            closeDrawer();
        });
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(menuItem);
        menuButtons.put(menuKey, menuItem);
        menuButton = menuItem;
    }

    public void setDrawerListener(Consumer<String> listener) {
        this.drawerListener = listener;
    }

    public void toggleDrawer() {
        isOpen = !isOpen;
        if (isOpen) {
            overlayPanel.setVisible(true);
            contentPanel.setVisible(true);
            contentPanel.revalidate();
            contentPanel.repaint();
            this.revalidate();
            this.repaint();
        }
        animationTimer.start();
    }

    public void closeDrawer() {
        if (isOpen) {
            isOpen = false;
            animationTimer.start();
        }
    }

    public void setActiveMenu(String menuKey) {
        activeMenu = menuKey;
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            if (entry.getKey().equals(menuKey)) {
                entry.getValue().setBackground(ACTIVE_MENU_BG);
                entry.getValue().setForeground(MENU_TEXT);
            } else {
                entry.getValue().setBackground(new Color(0x00AEEF));
                entry.getValue().setForeground(MENU_TEXT);
            }
        }
    }

    private void updateDrawerPosition() {
        contentPanel.setBounds(currentX, 0, DRAWER_WIDTH, getHeight());
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());
        repaint();
    }

    @Override
    public void doLayout() {
        super.doLayout();
        updateDrawerPosition();
        if (getParent() != null) {
            getParent().setComponentZOrder(this, 0);
        }
        if (menuButton != null && menuButton.getParent() != null) {
            menuButton.getParent().setComponentZOrder(menuButton, 0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Bo góc trái
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0,0,0,0));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(currentX, 0, DRAWER_WIDTH, getHeight(), 32, 32);
        g2.dispose();
    }
} 