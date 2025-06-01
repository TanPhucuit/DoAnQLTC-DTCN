package com.personal.finance.testproject.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardService {
    private final Connection connection;

    public DashboardService(Connection connection) {
        this.connection = connection;
    }

    public Map<String, Double> getFinancialSummary(int userId) throws Exception {
        Map<String, Double> summary = new HashMap<>();
        
        // Get total income
        String incomeQuery = "SELECT COALESCE(SUM(amount), 0) as total FROM income WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(incomeQuery)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                summary.put("totalIncome", rs.getDouble("total"));
            }
        }

        // Get total expenses
        String expenseQuery = "SELECT COALESCE(SUM(trans_amount), 0) as total FROM TRANSACTION WHERE user_id = ? AND TypeID LIKE 'SP_%'";
        try (PreparedStatement stmt = connection.prepareStatement(expenseQuery)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                summary.put("totalExpenses", rs.getDouble("total"));
            }
        }

        // Get total savings
        String savingsQuery = "SELECT COALESCE(SUM(amount), 0) as total FROM SAVING WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(savingsQuery)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                summary.put("totalSavings", rs.getDouble("total"));
            }
        }

        // Get total investments
        String investmentQuery = "SELECT COALESCE(SUM(amount), 0) as total FROM investments WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(investmentQuery)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                summary.put("totalInvestments", rs.getDouble("total"));
            }
        }

        return summary;
    }

    public List<Map<String, Object>> getRecentTransactions(int userId) throws Exception {
        List<Map<String, Object>> transactions = new ArrayList<>();
        
        String query = "SELECT t.*, c.name as category_name " +
                      "FROM TRANSACTION t " +
                      "LEFT JOIN categories c ON t.category_id = c.id " +
                      "WHERE t.user_id = ? " +
                      "ORDER BY t.transaction_date DESC " +
                      "LIMIT 10";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> transaction = new HashMap<>();
                transaction.put("id", rs.getInt("id"));
                transaction.put("amount", rs.getDouble("amount"));
                transaction.put("description", rs.getString("description"));
                transaction.put("transaction_date", rs.getDate("transaction_date"));
                transaction.put("category_name", rs.getString("category_name"));
                transaction.put("type", rs.getString("type"));
                transactions.add(transaction);
            }
        }

        return transactions;
    }

    public List<Map<String, Object>> getFinancialGoals(int userId) throws Exception {
        List<Map<String, Object>> goals = new ArrayList<>();
        
        String query = "SELECT * FROM financial_goals WHERE user_id = ? ORDER BY target_date ASC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> goal = new HashMap<>();
                goal.put("id", rs.getInt("id"));
                goal.put("name", rs.getString("name"));
                goal.put("target_amount", rs.getDouble("target_amount"));
                goal.put("current_amount", rs.getDouble("current_amount"));
                goal.put("target_date", rs.getDate("target_date"));
                goal.put("status", rs.getString("status"));
                goals.add(goal);
            }
        }

        return goals;
    }
} 