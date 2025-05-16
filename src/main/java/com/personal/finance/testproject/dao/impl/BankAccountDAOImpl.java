package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.BankAccountDAO;
import com.personal.finance.testproject.model.BankAccount;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class BankAccountDAOImpl implements BankAccountDAO {
    private final Connection connection;

    public BankAccountDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(BankAccount bankAccount) {
        String sql = "INSERT INTO BANK_ACCOUNT (BANK_ACCOUNT_NUMBER, BANK_NAME, USER_ID, BALANCE, NOTE, UP_DATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankAccount.getBankAccountNumber());
            stmt.setString(2, bankAccount.getBankName());
            stmt.setInt(3, bankAccount.getUserId());
            stmt.setBigDecimal(4, bankAccount.getBalance());
            stmt.setString(5, bankAccount.getNote());
            stmt.setDate(6, java.sql.Date.valueOf(bankAccount.getUpDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting bank account", e);
        }
    }

    @Override
    public BankAccount findById(String bankAccountNumber) {
        String sql = "SELECT * FROM BANK_ACCOUNT WHERE BANK_ACCOUNT_NUMBER = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankAccountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBankAccount(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bank account by ID", e);
        }
        return null;
    }

    @Override
    public List<BankAccount> findByUserId(int userId) {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM BANK_ACCOUNT WHERE USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToBankAccount(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bank accounts by user ID", e);
        }
        return accounts;
    }

    @Override
    public List<BankAccount> findAll() {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM BANK_ACCOUNT";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                accounts.add(mapResultSetToBankAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all bank accounts", e);
        }
        return accounts;
    }

    @Override
    public void update(BankAccount bankAccount) {
        String sql = "UPDATE BANK_ACCOUNT SET BANK_NAME = ?, BALANCE = ?, NOTE = ?, UP_DATE = ? " +
                    "WHERE BANK_ACCOUNT_NUMBER = ? AND USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankAccount.getBankName());
            stmt.setBigDecimal(2, bankAccount.getBalance());
            stmt.setString(3, bankAccount.getNote());
            stmt.setDate(4, java.sql.Date.valueOf(bankAccount.getUpDate()));
            stmt.setString(5, bankAccount.getBankAccountNumber());
            stmt.setInt(6, bankAccount.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating bank account", e);
        }
    }



    private BankAccount mapResultSetToBankAccount(ResultSet rs) throws SQLException {
        BankAccount account = new BankAccount();
        account.setBankAccountNumber(rs.getString("BANK_ACCOUNT_NUMBER"));
        account.setBankName(rs.getString("BANK_NAME"));
        account.setUserId(rs.getInt("USER_ID"));
        account.setBalance(rs.getBigDecimal("BALANCE"));
        account.setNote(rs.getString("NOTE"));
        java.sql.Date upDate = rs.getDate("UP_DATE");
        if (upDate == null || upDate.toString().equals("0000-00-00")) {
            upDate = java.sql.Date.valueOf("2000-01-01");
        }
        account.setUpDate(upDate.toLocalDate());
        return account;
    }

    @Override
    public List<BankAccount> findByBankName(String bankName) {
        String sql = "SELECT ba.*, u.user_name " +
                    "FROM BANK_ACCOUNT ba " +
                    "LEFT JOIN SYS_USER u ON ba.USER_ID = u.UserID " +
                    "WHERE ba.BANK_NAME = ?";
        List<BankAccount> bankAccounts = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setUserId(rs.getInt("USER_ID"));
                    bankAccount.setBankAccountId(rs.getInt("BANK_ACCOUNT_ID"));
                    bankAccount.setBankName(rs.getString("BANK_NAME"));
                    bankAccount.setBankAccountNumber(rs.getString("BANK_ACCOUNT_NUMBER"));
                    bankAccount.setBalance(rs.getBigDecimal("BALANCE"));
                    bankAccount.setNote(rs.getString("NOTE"));
                    Date upDate = rs.getDate("UP_DATE");
                    bankAccount.setUpDate(upDate != null ? upDate.toLocalDate() : null);
                    bankAccounts.add(bankAccount);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bank accounts by bank name", e);
        }
        return bankAccounts;
    }

    @Override
    public void updateBalance(String bankAccountId, BigDecimal amount) {
        String sql = "UPDATE BANK_ACCOUNT SET BALANCE = BALANCE + ? WHERE BANK_ACCOUNT_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, amount);
            stmt.setString(2, bankAccountId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating bank account balance", e);
        }
    }
} 