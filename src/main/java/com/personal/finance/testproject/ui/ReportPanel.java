package com.personal.finance.testproject.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportPanel extends JPanel {
    private final int userId;
    private JTable reportTable;
    private JComboBox<String> reportTypeComboBox;
    private JComboBox<String> periodComboBox;
    private JPanel chartPanel;

    public ReportPanel(int userId) {
        this.userId = userId;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Báo cáo tài chính");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Report Type
        JLabel reportTypeLabel = new JLabel("Loại báo cáo:");
        filterPanel.add(reportTypeLabel);

        String[] reportTypes = {"Thu chi", "Khoản vay", "Tiết kiệm"};
        reportTypeComboBox = new JComboBox<>(reportTypes);
        filterPanel.add(reportTypeComboBox);

        filterPanel.add(Box.createHorizontalStrut(20));

        // Period
        JLabel periodLabel = new JLabel("Kỳ báo cáo:");
        filterPanel.add(periodLabel);

        String[] periods = {"Tháng này", "Quý này", "Năm nay", "Tùy chọn"};
        periodComboBox = new JComboBox<>(periods);
        filterPanel.add(periodComboBox);

        // Generate Button
        JButton generateButton = new JButton("Tạo báo cáo");
        generateButton.setBackground(new Color(0x2E2E5D));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        generateButton.setFocusPainted(false);
        generateButton.setBorderPainted(false);
        generateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        filterPanel.add(generateButton);

        add(filterPanel, BorderLayout.CENTER);

        // Chart Panel
        chartPanel = new JPanel();
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(chartPanel, BorderLayout.CENTER);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnNames = {"Thời gian", "Loại", "Số tiền", "Mô tả"};
        Object[][] data = {}; // TODO: Load data from database
        reportTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Split Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chartPanel, tablePanel);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
    }

    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        String period = (String) periodComboBox.getSelectedItem();

        // Calculate date range based on period
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        Date startDate;

        switch (period) {
            case "Tháng này":
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();
                break;
            case "Quý này":
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) / 3) * 3);
                startDate = calendar.getTime();
                break;
            case "Năm nay":
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                startDate = calendar.getTime();
                break;
            case "Tùy chọn":
                // TODO: Show date picker dialog
                return;
            default:
                startDate = calendar.getTime();
        }

        // TODO: Generate report based on type and date range
        // TODO: Update chart and table
    }
} 