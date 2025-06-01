package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.FinancialPlanDAO;
import com.personal.finance.testproject.model.FinancialPlan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class FinancialPlanDAOImpl implements FinancialPlanDAO {
    private Connection connection;

    public FinancialPlanDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(FinancialPlan financialPlan) {
        String sql = "INSERT INTO FINANCIAL_PLAN (UserID, Investor_type, cur_property, " +
                    "cur_invest_property, cur_es_profit, cur_cumulative_pnl, warning_loss_rate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, financialPlan.getUserId());
            stmt.setString(2, financialPlan.getInvestorType());
            stmt.setBigDecimal(4, financialPlan.getCurInvestProperty());
            stmt.setBigDecimal(5, financialPlan.getCurEsProfit());
            stmt.setBigDecimal(6, financialPlan.getCurCumulativePnl());
            stmt.setBigDecimal(7, financialPlan.getWarningLossRate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting financial plan", e);
        }
    }

    @Override
    public FinancialPlan findByUserId(int userId) {
        String sql = "SELECT * FROM FINANCIAL_PLAN WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFinancialPlan(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding financial plan by user ID", e);
        }
        return null;
    }

    @Override
    public List<FinancialPlan> findAll() {
        List<FinancialPlan> financialPlans = new ArrayList<>();
        String sql = "SELECT * FROM FINANCIAL_PLAN";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                financialPlans.add(mapResultSetToFinancialPlan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all financial plans", e);
        }
        return financialPlans;
    }

    @Override
    public List<FinancialPlan> findByInvestorType(String investorType) {
        List<FinancialPlan> financialPlans = new ArrayList<>();
        String sql = "SELECT * FROM FINANCIAL_PLAN WHERE Investor_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, investorType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    financialPlans.add(mapResultSetToFinancialPlan(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding financial plans by investor type", e);
        }
        return financialPlans;
    }

    @Override
    public void update(FinancialPlan financialPlan) {
        String sql = "UPDATE FINANCIAL_PLAN SET Investor_type = ?, cur_property = ?, " +
                    "cur_invest_property = ?, cur_es_profit = ?, cur_cumulative_pnl = ?, " +
                    "warning_loss_rate = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, financialPlan.getInvestorType());
            stmt.setBigDecimal(3, financialPlan.getCurInvestProperty());
            stmt.setBigDecimal(4, financialPlan.getCurEsProfit());
            stmt.setBigDecimal(5, financialPlan.getCurCumulativePnl());
            stmt.setBigDecimal(6, financialPlan.getWarningLossRate());
            stmt.setInt(7, financialPlan.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating financial plan", e);
        }
    }

  

    @Override
    public void updateCurProperty(int userId, double amount) {
        String sql = "UPDATE FINANCIAL_PLAN SET cur_property = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(amount));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating current property", e);
        }
    }

    @Override
    public void updateCurInvestProperty(int userId, double amount) {
        String sql = "UPDATE FINANCIAL_PLAN SET cur_invest_property = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(amount));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating current investment property", e);
        }
    }

    @Override
    public void updateCurEsProfit(int userId, double amount) {
        String sql = "UPDATE FINANCIAL_PLAN SET cur_es_profit = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(amount));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating current estimated profit", e);
        }
    }

    @Override
    public void updateCurCumulativePnl(int userId, double amount) {
        String sql = "UPDATE FINANCIAL_PLAN SET cur_cumulative_pnl = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(amount));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating current cumulative PnL", e);
        }
    }

    @Override
    public void updateWarningLossRate(int userId, double rate) {
        String sql = "UPDATE FINANCIAL_PLAN SET warning_loss_rate = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(rate));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating warning loss rate", e);
        }
    }

    private FinancialPlan mapResultSetToFinancialPlan(ResultSet rs) throws SQLException {
        FinancialPlan financialPlan = new FinancialPlan();
        financialPlan.setUserId(rs.getInt("UserID"));
        financialPlan.setInvestorType(rs.getString("Investor_type"));
        financialPlan.setCurInvestProperty(rs.getBigDecimal("cur_invest_property"));
        financialPlan.setCurEsProfit(rs.getBigDecimal("cur_es_profit"));
        financialPlan.setCurCumulativePnl(rs.getBigDecimal("cur_cumulative_pnl"));
        financialPlan.setWarningLossRate(rs.getBigDecimal("warning_loss_rate"));
        return financialPlan;
    }
} 