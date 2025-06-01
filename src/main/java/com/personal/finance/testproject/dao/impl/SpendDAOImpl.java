package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.SpendDAO;
import com.personal.finance.testproject.model.Spend;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class SpendDAOImpl implements SpendDAO {
    private Connection connection;

    public SpendDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Spend spend) {
        String sql = "INSERT INTO SPEND (UserID, SpendID, sp_month, spend_name, spend_amount, up_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, spend.getUserId());
            stmt.setString(2, spend.getSpendId());
            stmt.setString(3, spend.getSpMonth());
            stmt.setString(4, spend.getSpendName());
            stmt.setBigDecimal(5, spend.getSpendAmount());
            stmt.setDate(6, new java.sql.Date(spend.getUpDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting spend", e);
        }
    }

    @Override
    public Spend findById(String spendId, String month) {
        String sql = "SELECT s.*, st.type_name " +
                    "FROM SPEND s " +
                    "LEFT JOIN SPEND_TYPE st ON s.SpendID = st.SpendID " +
                    "WHERE s.SpendID = ? AND s.sp_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, spendId);
            stmt.setString(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Spend spend = new Spend();
                    spend.setUserId(rs.getInt("UserID"));
                    spend.setSpendId(rs.getString("SpendID"));
                    spend.setSpMonth(rs.getString("sp_month"));
                    spend.setSpendName(rs.getString("spend_name"));
                    spend.setSpendAmount(rs.getBigDecimal("spend_amount"));
                    java.sql.Date upDate = rs.getDate("up_date");
                    if (upDate == null || upDate.toString().equals("0000-00-00")) {
                        upDate = java.sql.Date.valueOf("2000-01-01");
                    }
                    spend.setUpDate(upDate);
                    return spend;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding spend by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Spend> findByUserId(int userId) {
        List<Spend> spends = new ArrayList<>();
        String sql = "SELECT s.*, st.type_name " +
                    "FROM SPEND s " +
                    "LEFT JOIN SPEND_TYPE st ON s.SpendID = st.SpendID " +
                    "WHERE s.UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Spend spend = new Spend();
                    spend.setUserId(rs.getInt("UserID"));
                    spend.setSpendId(rs.getString("SpendID"));
                    spend.setSpMonth(rs.getString("sp_month"));
                    spend.setSpendName(rs.getString("spend_name"));
                    spend.setSpendAmount(rs.getBigDecimal("spend_amount"));
                    java.sql.Date upDate = rs.getDate("up_date");
                    if (upDate == null || upDate.toString().equals("0000-00-00")) {
                        upDate = java.sql.Date.valueOf("2000-01-01");
                    }
                    spend.setUpDate(upDate);
                    spends.add(spend);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding spends by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return spends;
    }

    @Override
    public List<Spend> findByMonth(String month) {
        List<Spend> spends = new ArrayList<>();
        String sql = "SELECT s.*, st.type_name " +
                    "FROM SPEND s " +
                    "LEFT JOIN SPEND_TYPE st ON s.SpendID = st.SpendID " +
                    "WHERE s.sp_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Spend spend = new Spend();
                    spend.setUserId(rs.getInt("UserID"));
                    spend.setSpendId(rs.getString("SpendID"));
                    spend.setSpMonth(rs.getString("sp_month"));
                    spend.setSpendName(rs.getString("spend_name"));
                    spend.setSpendAmount(rs.getBigDecimal("spend_amount"));
                    java.sql.Date upDate = rs.getDate("up_date");
                    if (upDate == null || upDate.toString().equals("0000-00-00")) {
                        upDate = java.sql.Date.valueOf("2000-01-01");
                    }
                    spend.setUpDate(upDate);
                    spends.add(spend);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding spends by month: " + e.getMessage());
            e.printStackTrace();
        }
        return spends;
    }

    @Override
    public List<Spend> findByUserIdAndMonth(int userId, String month) {
        List<Spend> spends = new ArrayList<>();
        String sql = "SELECT s.*, st.type_name " +
                    "FROM SPEND s " +
                    "LEFT JOIN SPEND_TYPE st ON s.SpendID = st.SpendID " +
                    "WHERE s.UserID = ? AND s.sp_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Spend spend = new Spend();
                    spend.setUserId(rs.getInt("UserID"));
                    spend.setSpendId(rs.getString("SpendID"));
                    spend.setSpMonth(rs.getString("sp_month"));
                    spend.setSpendName(rs.getString("spend_name"));
                    spend.setSpendAmount(rs.getBigDecimal("spend_amount"));
                    java.sql.Date upDate = rs.getDate("up_date");
                    if (upDate == null || upDate.toString().equals("0000-00-00")) {
                        upDate = java.sql.Date.valueOf("2000-01-01");
                    }
                    spend.setUpDate(upDate);
                    spends.add(spend);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding spends by user ID and month: " + e.getMessage());
            e.printStackTrace();
        }
        return spends;
    }

    @Override
    public List<Spend> findAll() {
        List<Spend> spends = new ArrayList<>();
        String sql = "SELECT s.*, st.type_name " +
                    "FROM SPEND s " +
                    "LEFT JOIN SPEND_TYPE st ON s.SpendID = st.SpendID";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Spend spend = new Spend();
                spend.setUserId(rs.getInt("UserID"));
                spend.setSpendId(rs.getString("SpendID"));
                spend.setSpMonth(rs.getString("sp_month"));
                spend.setSpendName(rs.getString("spend_name"));
                spend.setSpendAmount(rs.getBigDecimal("spend_amount"));
                java.sql.Date upDate = rs.getDate("up_date");
                if (upDate == null || upDate.toString().equals("0000-00-00")) {
                    upDate = java.sql.Date.valueOf("2000-01-01");
                }
                spend.setUpDate(upDate);
                spends.add(spend);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all spends: " + e.getMessage());
            e.printStackTrace();
        }
        return spends;
    }

    @Override
    public void update(Spend spend) {
        String sql = "UPDATE SPEND SET spend_name = ?, spend_amount = ?, up_date = ? " +
                    "WHERE SpendID = ? AND sp_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, spend.getSpendName());
            stmt.setBigDecimal(2, spend.getSpendAmount());
            stmt.setDate(3, new java.sql.Date(spend.getUpDate().getTime()));
            stmt.setString(4, spend.getSpendId());
            stmt.setString(5, spend.getSpMonth());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating spend", e);
        }
    }

   
} 