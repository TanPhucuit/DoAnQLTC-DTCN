package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.InvestDAO;
import com.personal.finance.testproject.model.Invest;
import com.personal.finance.testproject.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvestDAOImpl implements InvestDAO {
    private Connection conn;

    public InvestDAOImpl() {
     
    }

    public InvestDAOImpl(Connection connection) {
        this.conn = connection;
    }

    @Override
    public void insert(Invest invest) {
        String sql = "INSERT INTO INVEST_STORAGE (InStID, UserID, num_unit, buy_price, es_profit, up_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, invest.getInvestId());
            stmt.setInt(2, invest.getUserId());
            stmt.setBigDecimal(3, invest.getAmount());
            stmt.setBigDecimal(4, invest.getCurrentPrice());
            stmt.setBigDecimal(5, invest.getInvestProperty());
            stmt.setDate(6, new java.sql.Date(invest.getUpDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Invest findById(String investId, int userId) {
        String sql = "SELECT isr.*, isd.riskLevel, isd.cur_price, isd.z_score, isd.standard_deviation " +
                    "FROM INVEST_STORAGE isr " +
                    "LEFT JOIN INVEST_STORAGE_DETAIL isd ON isr.InStID = isd.InStID " +
                    "WHERE isr.InStID = ? AND isr.UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, investId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToInvest(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Invest> findByUserId(int userId) {
        List<Invest> invests = new ArrayList<>();
        String sql = "SELECT isr.*, isd.riskLevel, isd.cur_price, isd.z_score, isd.standard_deviation " +
                    "FROM INVEST_STORAGE isr " +
                    "LEFT JOIN INVEST_STORAGE_DETAIL isd ON isr.InStID = isd.InStID " +
                    "WHERE isr.UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                invests.add(mapResultSetToInvest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding invests by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return invests;
    }

    @Override
    public List<Invest> findByMonth(String month) {
        List<Invest> invests = new ArrayList<>();
        String sql = "SELECT isr.*, isd.riskLevel, isd.cur_price, isd.z_score, isd.standard_deviation " +
                    "FROM INVEST_STORAGE isr " +
                    "LEFT JOIN INVEST_STORAGE_DETAIL isd ON isr.InStID = isd.InStID " +
                    "WHERE MONTH(isr.up_date) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                invests.add(mapResultSetToInvest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding invests by month: " + e.getMessage());
            e.printStackTrace();
        }
        return invests;
    }

    @Override
    public List<Invest> findByUserIdAndMonth(int userId, String month) {
        List<Invest> invests = new ArrayList<>();
        String sql = "SELECT isr.*, isd.riskLevel, isd.cur_price, isd.z_score, isd.standard_deviation " +
                    "FROM INVEST_STORAGE isr " +
                    "LEFT JOIN INVEST_STORAGE_DETAIL isd ON isr.InStID = isd.InStID " +
                    "WHERE isr.UserID = ? AND MONTH(isr.up_date) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                invests.add(mapResultSetToInvest(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding invests by user ID and month: " + e.getMessage());
            e.printStackTrace();
        }
        return invests;
    }

    @Override
    public List<Invest> findAll() {
        List<Invest> invests = new ArrayList<>();
        String sql = "SELECT isr.*, isd.riskLevel, isd.cur_price, isd.z_score, isd.standard_deviation " +
                    "FROM INVEST_STORAGE isr " +
                    "LEFT JOIN INVEST_STORAGE_DETAIL isd ON isr.InStID = isd.InStID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invests.add(mapResultSetToInvest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invests;
    }

    @Override
    public void update(Invest invest) {
        String sql = "UPDATE INVEST_STORAGE SET num_unit = ?, buy_price = ?, es_profit = ?, up_date = ? WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, invest.getAmount());
            stmt.setBigDecimal(2, invest.getCurrentPrice());
            stmt.setBigDecimal(3, invest.getInvestProperty());
            stmt.setDate(4, new java.sql.Date(invest.getUpDate().getTime()));
            stmt.setString(5, invest.getInvestId());
            stmt.setInt(6, invest.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateInvestProperty(String investId, String month, double amount) {
        String sql = "UPDATE INVEST_STORAGE SET es_profit = ?, up_date = CURDATE() WHERE InStID = ? AND MONTH(up_date) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, investId);
            stmt.setString(3, month);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Invest mapResultSetToInvest(ResultSet rs) throws SQLException {
        Invest invest = new Invest();
        invest.setUserId(rs.getInt("UserID"));
        invest.setInvestId(rs.getString("InStID"));
        invest.setAmount(rs.getBigDecimal("num_unit"));
        invest.setCurrentPrice(rs.getBigDecimal("buy_price"));
        invest.setInvestProperty(rs.getBigDecimal("es_profit"));
        Date upDate = null;
        try {
            upDate = rs.getDate("up_date");
        } catch (Exception e) {
            upDate = null;
        }
        invest.setUpDate(upDate);
        invest.setRiskLevel(rs.getInt("riskLevel"));
        invest.setCurrentPrice(rs.getBigDecimal("cur_price"));
        invest.setZScore(rs.getBigDecimal("z_score"));
        invest.setStandardDeviation(rs.getBigDecimal("standard_deviation"));
        return invest;
    }
} 