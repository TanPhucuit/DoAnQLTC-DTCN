package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.InvestPropertyDAO;
import com.personal.finance.testproject.model.InvestProperty;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class InvestPropertyDAOImpl implements InvestPropertyDAO {
    private Connection connection;

    public InvestPropertyDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(InvestProperty investProperty) {
        String sql = "INSERT INTO INVEST_STORAGE (InStID, UserID, num_unit, buy_price, es_profit, up_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, investProperty.getInStId());
            stmt.setInt(2, investProperty.getUserId());
            stmt.setBigDecimal(3, investProperty.getNumUnit());
            stmt.setBigDecimal(4, investProperty.getBuyPrice());
            stmt.setBigDecimal(5, investProperty.getEsProfit());
            stmt.setDate(6, new java.sql.Date(investProperty.getUpDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting invest property", e);
        }
    }

    @Override
    public InvestProperty findById(String investPropertyId, String month) {
        String sql = "SELECT * FROM INVEST_PROPERTY WHERE InvestPropertyID = ? AND ip_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, investPropertyId);
            stmt.setString(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvestProperty(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding invest property by ID", e);
        }
        return null;
    }

    @Override
    public List<InvestProperty> findByUserId(int userId) {
        List<InvestProperty> investProperties = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_PROPERTY WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    investProperties.add(mapResultSetToInvestProperty(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding invest properties by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return investProperties;
    }

    @Override
    public List<InvestProperty> findByMonth(String month) {
        List<InvestProperty> investProperties = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_PROPERTY WHERE ip_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    investProperties.add(mapResultSetToInvestProperty(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding invest properties by month: " + e.getMessage());
            e.printStackTrace();
        }
        return investProperties;
    }

    @Override
    public List<InvestProperty> findByUserIdAndMonth(int userId, String month) {
        List<InvestProperty> investProperties = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_PROPERTY WHERE UserID = ? AND ip_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, month.length() == 1 ? "0" + month : month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    investProperties.add(mapResultSetToInvestProperty(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding invest properties by user ID and month: " + e.getMessage());
            e.printStackTrace();
        }
        return investProperties;
    }

    @Override
    public List<InvestProperty> findAll() {
        List<InvestProperty> investProperties = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_PROPERTY";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                investProperties.add(mapResultSetToInvestProperty(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all invest properties", e);
        }
        return investProperties;
    }

    @Override
    public void update(InvestProperty investProperty) {
        String sql = "UPDATE INVEST_STORAGE SET num_unit = ?, buy_price = ?, es_profit = ?, up_date = ? WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, investProperty.getNumUnit());
            stmt.setBigDecimal(2, investProperty.getBuyPrice());
            stmt.setBigDecimal(3, investProperty.getEsProfit());
            stmt.setDate(4, new java.sql.Date(investProperty.getUpDate().getTime()));
            stmt.setString(5, investProperty.getInStId());
            stmt.setInt(6, investProperty.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating invest property", e);
        }
    }

    private InvestProperty mapResultSetToInvestProperty(ResultSet rs) throws SQLException {
        InvestProperty investProperty = new InvestProperty();
        investProperty.setInStId(rs.getString("InStID"));
        investProperty.setUserId(rs.getInt("UserID"));
        investProperty.setNumUnit(rs.getBigDecimal("num_unit"));
        investProperty.setBuyPrice(rs.getBigDecimal("buy_price"));
        investProperty.setEsProfit(rs.getBigDecimal("es_profit"));
        investProperty.setUpDate(rs.getDate("up_date"));
        try { investProperty.setUnit(rs.getString("unit")); } catch (Exception e) { investProperty.setUnit(""); }
        return investProperty;
    }
} 