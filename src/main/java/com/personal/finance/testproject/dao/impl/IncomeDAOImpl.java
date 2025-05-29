package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.IncomeDAO;
import com.personal.finance.testproject.model.Income;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class IncomeDAOImpl implements IncomeDAO {
    private final Connection connection;

    public IncomeDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Income income) {
        String sql = "INSERT INTO INCOME (UserID, income_date, salary, allowance, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, income.getUserId());
            stmt.setDate(2, new java.sql.Date(income.getIncomeDate().getTime()));
            stmt.setBigDecimal(3, income.getSalary());
            stmt.setBigDecimal(4, income.getAllowance());
            stmt.setString(5, income.getDescription());
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
    public Income findById(int incomeId) {
        String sql = "SELECT * FROM INCOME WHERE income_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, incomeId);
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
        String sql = "SELECT * FROM INCOME WHERE UserID = ? ORDER BY income_date DESC";
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
        String sql = "SELECT * FROM INCOME WHERE UserID = ? AND ic_month = ? ORDER BY ic_month DESC";
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
        String sql = "SELECT * FROM INCOME ORDER BY income_date DESC";
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
        String sql = "UPDATE INCOME SET income_date = ?, salary = ?, allowance = ?, description = ? WHERE income_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(income.getIncomeDate().getTime()));
            stmt.setBigDecimal(2, income.getSalary());
            stmt.setBigDecimal(3, income.getAllowance());
            stmt.setString(4, income.getDescription());
            stmt.setInt(5, income.getIncomeId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating income", e);
        }
    }

    @Override
    public void updateSalary(int incomeId, BigDecimal salary) {
        String sql = "UPDATE INCOME SET salary = ? WHERE income_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, salary);
            stmt.setInt(2, incomeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating salary", e);
        }
    }

    @Override
    public void updateAllowance(int incomeId, BigDecimal allowance) {
        String sql = "UPDATE INCOME SET allowance = ? WHERE income_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, allowance);
            stmt.setInt(2, incomeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating allowance", e);
        }
    }

    @Override
    public void delete(int incomeId) {
        String sql = "DELETE FROM INCOME WHERE income_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, incomeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting income", e);
        }
    }

    @Override
    public BigDecimal getTotalSalaryByMonth(int userId, int month) {
        String sql = "SELECT SUM(salary) as total FROM INCOME WHERE UserID = ? AND MONTH(income_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total salary by month", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalAllowanceByMonth(int userId, int month) {
        String sql = "SELECT SUM(allowance) as total FROM INCOME WHERE UserID = ? AND MONTH(income_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total allowance by month", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalIncomeByMonth(int userId, int month) {
        String sql = "SELECT SUM(salary + allowance) as total FROM INCOME WHERE UserID = ? AND MONTH(income_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total income by month", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void updateRemainIncome(int incomeId, int userId, BigDecimal amount) {
        String sql = "UPDATE INCOME SET remain_income = ? WHERE IncomeID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, incomeId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating remain income", e);
        }
    }

    private Income mapResultSetToIncome(ResultSet rs) throws SQLException {
        Income income = new Income();
        income.setIncomeId(rs.getInt("income_id"));
        income.setUserId(rs.getInt("UserID"));
        income.setIncomeDate(rs.getDate("income_date"));
        income.setSalary(rs.getBigDecimal("salary"));
        income.setAllowance(rs.getBigDecimal("allowance"));
        income.setDescription(rs.getString("description"));
        return income;
    }
} 