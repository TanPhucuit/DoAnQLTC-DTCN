package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.AccountBalanceDAO;
import com.personal.finance.testproject.model.AccountBalance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountBalanceDAOImpl implements AccountBalanceDAO {
    private Connection connection;

    public AccountBalanceDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(AccountBalance accountBalance) {
        validateAccountBalance(accountBalance);
        String sql = "INSERT INTO ACCOUNT_BALANCE (UserID, AccountBalanceID, ab_month, " +
                    "total_remain_income, total_remain_save, total_loan_remain, total_spend, " +
                    "total_invest, total_invest_property, balance, up_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, accountBalance.getUserId());
            stmt.setString(2, accountBalance.getAccountBalanceId());
            stmt.setString(3, accountBalance.getAbMonth());
            stmt.setBigDecimal(4, accountBalance.getTotalRemainIncome());
            stmt.setBigDecimal(5, accountBalance.getTotalRemainSave());
            stmt.setBigDecimal(6, accountBalance.getTotalLoanRemain());
            stmt.setBigDecimal(7, accountBalance.getTotalSpend());
            stmt.setBigDecimal(8, accountBalance.getTotalInvest());
            stmt.setBigDecimal(9, accountBalance.getTotalInvestProperty());
            stmt.setBigDecimal(10, accountBalance.getBalance());
            stmt.setDate(11, java.sql.Date.valueOf(accountBalance.getUpDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting account balance", e);
        }
    }

    @Override
    public AccountBalance findById(String accountBalanceId, String month) {
        String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE AccountBalanceID = ? AND ab_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountBalanceId);
            stmt.setString(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccountBalance(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account balance by ID", e);
        }
        return null;
    }

    @Override
    public List<AccountBalance> findByUserId(int userId) {
        List<AccountBalance> balances = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    balances.add(mapResultSetToAccountBalance(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account balances by user ID", e);
        }
        return balances;
    }

    @Override
    public List<AccountBalance> findByMonth(String month) {
        List<AccountBalance> balances = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE ab_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    balances.add(mapResultSetToAccountBalance(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account balances by month", e);
        }
        return balances;
    }

    @Override
    public List<AccountBalance> findAll() {
        List<AccountBalance> balances = new ArrayList<>();
        String sql = "SELECT * FROM ACCOUNT_BALANCE";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                balances.add(mapResultSetToAccountBalance(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all account balances", e);
        }
        return balances;
    }

    @Override
    public void update(AccountBalance accountBalance) {
        validateAccountBalance(accountBalance);
        String sql = "UPDATE ACCOUNT_BALANCE SET total_remain_income = ?, total_remain_save = ?, " +
                    "total_loan_remain = ?, total_spend = ?, total_invest = ?, " +
                    "total_invest_property = ?, balance = ?, up_date = ? " +
                    "WHERE AccountBalanceID = ? AND ab_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, accountBalance.getTotalRemainIncome());
            stmt.setBigDecimal(2, accountBalance.getTotalRemainSave());
            stmt.setBigDecimal(3, accountBalance.getTotalLoanRemain());
            stmt.setBigDecimal(4, accountBalance.getTotalSpend());
            stmt.setBigDecimal(5, accountBalance.getTotalInvest());
            stmt.setBigDecimal(6, accountBalance.getTotalInvestProperty());
            stmt.setBigDecimal(7, accountBalance.getBalance());
            stmt.setDate(8, java.sql.Date.valueOf(accountBalance.getUpDate()));
            stmt.setString(9, accountBalance.getAccountBalanceId());
            stmt.setString(10, accountBalance.getAbMonth());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account balance", e);
        }
    }

    @Override
    public void updateBalance(String accountBalanceId, String month, double amount) {
        String sql = "UPDATE ACCOUNT_BALANCE SET balance = ? WHERE AccountBalanceID = ? AND ab_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(amount).setScale(5, BigDecimal.ROUND_HALF_UP));
            stmt.setString(2, accountBalanceId);
            stmt.setString(3, month);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account balance amount", e);
        }
    }

    @Override
    public AccountBalance findByUserIdAndMonth(int userId, String abMonth) {
        String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE UserID = ? AND ab_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, abMonth);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccountBalance(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account balance by userId and ab_month", e);
        }
        return null;
    }

    private AccountBalance mapResultSetToAccountBalance(ResultSet rs) throws SQLException {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setUserId(rs.getInt("UserID"));
        accountBalance.setAccountBalanceId(rs.getString("AccountBalanceID"));
        accountBalance.setAbMonth(rs.getString("ab_month"));
        accountBalance.setTotalRemainIncome(rs.getBigDecimal("total_remain_income"));
        accountBalance.setTotalRemainSave(rs.getBigDecimal("total_remain_save"));
        accountBalance.setTotalLoanRemain(rs.getBigDecimal("total_loan_remain"));
        accountBalance.setTotalSpend(rs.getBigDecimal("total_spend"));
        accountBalance.setTotalInvest(rs.getBigDecimal("total_invest"));
        accountBalance.setTotalInvestProperty(rs.getBigDecimal("total_invest_property"));
        accountBalance.setBalance(rs.getBigDecimal("balance"));
        Date upDate = rs.getDate("up_date");
        accountBalance.setUpDate(upDate != null ? upDate.toLocalDate() : LocalDate.now());
        return accountBalance;
    }

    private void validateAccountBalance(AccountBalance accountBalance) {
        if (accountBalance == null) {
            throw new IllegalArgumentException("AccountBalance cannot be null");
        }
        if (accountBalance.getUserId() <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (accountBalance.getAccountBalanceId() == null || accountBalance.getAccountBalanceId().trim().isEmpty()) {
            throw new IllegalArgumentException("AccountBalanceID cannot be null or empty");
        }
        if (accountBalance.getAbMonth() == null || !accountBalance.getAbMonth().matches("^([1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Invalid month value. Must be between 1 and 12");
        }
        if (accountBalance.getUpDate() == null) {
            throw new IllegalArgumentException("UpDate cannot be null");
        }
    }
} 