package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.IncomeDAO;
import com.personal.finance.testproject.model.Income;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class IncomeDAOImpl implements IncomeDAO {
    private Connection connection;

    public IncomeDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Income income) {
        validateIncome(income);
        String sql = "INSERT INTO INCOME (UserID, ic_month, income_name, income_amount, remain_income) " +
                    "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, income.getUserId());
            stmt.setString(2, income.getIcMonth());
            stmt.setString(3, income.getIncomeName());
            stmt.setBigDecimal(4, income.getIncomeAmount());
            stmt.setBigDecimal(5, income.getRemainIncome());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    income.setIncomeId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting income", e);
        }
    }

    @Override
    public Income findById(int incomeId, int userId) {
        String sql = "SELECT * FROM INCOME WHERE IncomeID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, incomeId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToIncome(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding income by ID", e);
        }
        return null;
    }

    @Override
    public List<Income> findByUserId(int userId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM INCOME WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    incomes.add(mapResultSetToIncome(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding incomes by user ID", e);
        }
        return incomes;
    }

    @Override
    public List<Income> findByMonth(int userId, String month) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM INCOME WHERE UserID = ? AND ic_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    incomes.add(mapResultSetToIncome(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding incomes by month", e);
        }
        return incomes;
    }

    @Override
    public List<Income> findAll() {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM INCOME";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                incomes.add(mapResultSetToIncome(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all incomes", e);
        }
        return incomes;
    }

    @Override
    public void update(Income income) {
        validateIncome(income);
        String sql = "UPDATE INCOME SET income_name = ?, income_amount = ?, remain_income = ? " +
                    "WHERE IncomeID = ? AND UserID = ? AND ic_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, income.getIncomeName());
            stmt.setBigDecimal(2, income.getIncomeAmount());
            stmt.setBigDecimal(3, income.getRemainIncome());
            stmt.setInt(4, income.getIncomeId());
            stmt.setInt(5, income.getUserId());
            stmt.setString(6, income.getIcMonth());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating income", e);
        }
    }

    @Override
    public void updateRemainIncome(int incomeId, int userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
        String sql = "UPDATE INCOME SET remain_income = remain_income - ? " +
                    "WHERE IncomeID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, amount.setScale(5, BigDecimal.ROUND_HALF_UP));
            stmt.setInt(2, incomeId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating remain income", e);
        }
    }

    @Override
    public List<Income> findByYear(int userId, String year) {
        List<Income> incomes = new ArrayList<>();
        String query = "SELECT * FROM INCOME WHERE UserID = ? AND SUBSTRING(ic_month, 1, 4) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Income income = new Income();
                income.setIncomeId(rs.getInt("income_id"));
                income.setUserId(rs.getInt("user_id"));
                income.setIncomeName(rs.getString("income_name"));
                income.setIncomeAmount(rs.getBigDecimal("income_amount"));
                income.setIcMonth(rs.getString("ic_month"));
                income.setRemainIncome(rs.getBigDecimal("remain_income"));
                java.sql.Date upDate = rs.getDate("up_date");
                if (upDate == null || upDate.toString().equals("0000-00-00")) {
                    upDate = java.sql.Date.valueOf("2000-01-01");
                }
                income.setUpDate(upDate);
                incomes.add(income);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding incomes by year: " + e.getMessage(), e);
        }
        return incomes;
    }

    private Income mapResultSetToIncome(ResultSet rs) throws SQLException {
        Income income = new Income();
        income.setIncomeId(rs.getInt("IncomeID"));
        income.setUserId(rs.getInt("UserID"));
        income.setIcMonth(rs.getString("ic_month"));
        income.setIncomeName(rs.getString("income_name"));
        income.setIncomeAmount(rs.getBigDecimal("income_amount"));
        income.setRemainIncome(rs.getBigDecimal("remain_income"));
        return income;
    }

    private void validateIncome(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null");
        }
        if (income.getUserId() <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (income.getIncomeId() <= 0) {
            throw new IllegalArgumentException("IncomeID must be positive");
        }
        if (income.getIcMonth() == null || !income.getIcMonth().matches("^([1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Invalid month value. Must be between 1 and 12");
        }
        if (income.getIncomeName() == null || income.getIncomeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Income name cannot be null or empty");
        }
        if (income.getIncomeAmount() == null || income.getIncomeAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income amount cannot be null or negative");
        }
        if (income.getRemainIncome() == null || income.getRemainIncome().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Remain income cannot be null or negative");
        }
        if (income.getRemainIncome().compareTo(income.getIncomeAmount()) > 0) {
            throw new IllegalArgumentException("Remain income cannot be greater than income amount");
        }
    }
} 