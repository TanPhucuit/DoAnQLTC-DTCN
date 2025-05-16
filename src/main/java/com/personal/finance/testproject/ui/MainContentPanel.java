package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainContentPanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final Map<String, JPanel> panelMap = new HashMap<>();

    public MainContentPanel() {
        setLayout(cardLayout);
    }

    public void addContentPanel(String key, JPanel panel) {
        panelMap.put(key, panel);
        add(panel, key);
    }

    public void showPanel(String key) {
        cardLayout.show(this, key);
    }
} 