package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.BankTransferDAO;
import com.personal.finance.testproject.model.BankTransfer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;

public class BankTransferDAOImpl implements BankTransferDAO {
    private final Connection connection;

    public BankTransferDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(BankTransfer bankTransfer) {
        String sql = "INSERT INTO BANK_TRANSFER (SourceBankAccountNumber, TargetBankAccountNumber, " +
                    "Transfer_amount, Transfer_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, bankTransfer.getSourceBankAccountNumber());
            stmt.setString(2, bankTransfer.getTargetBankAccountNumber());
            stmt.setBigDecimal(3, bankTransfer.getTransferAmount());
            stmt.setDate(4, new java.sql.Date(bankTransfer.getTransferDate().getTime()));
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    bankTransfer.setBankTransferId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting bank transfer", e);
        }
    }

    @Override
    public BankTransfer findById(int transferId) {
        String sql = "SELECT * FROM BANK_TRANSFER WHERE BankTransferID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transferId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToBankTransfer(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bank transfer by ID", e);
        }
        return null;
    }

    @Override
    public List<BankTransfer> findBySourceAccount(String accountNumber) {
        List<BankTransfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM BANK_TRANSFER WHERE SourceBankAccountNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transfers.add(mapResultSetToBankTransfer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bank transfers by source account", e);
        }
        return transfers;
    }

    @Override
    public List<BankTransfer> findByTargetAccount(String accountNumber) {
        List<BankTransfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM BANK_TRANSFER WHERE TargetBankAccountNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transfers.add(mapResultSetToBankTransfer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bank transfers by target account", e);
        }
        return transfers;
    }

    @Override
    public List<BankTransfer> findByDateRange(Date startDate, Date endDate) {
        List<BankTransfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM BANK_TRANSFER WHERE Transfer_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transfers.add(mapResultSetToBankTransfer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding bank transfers by date range", e);
        }
        return transfers;
    }

    @Override
    public List<BankTransfer> findAll() {
        List<BankTransfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM BANK_TRANSFER";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                transfers.add(mapResultSetToBankTransfer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all bank transfers", e);
        }
        return transfers;
    }

    @Override
    public void update(BankTransfer bankTransfer) {
        String sql = "UPDATE BANK_TRANSFER SET SourceBankAccountNumber = ?, TargetBankAccountNumber = ?, " +
                    "Transfer_amount = ?, Transfer_date = ? WHERE BankTransferID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankTransfer.getSourceBankAccountNumber());
            stmt.setString(2, bankTransfer.getTargetBankAccountNumber());
            stmt.setBigDecimal(3, bankTransfer.getTransferAmount());
            stmt.setDate(4, new java.sql.Date(bankTransfer.getTransferDate().getTime()));
            stmt.setInt(5, bankTransfer.getBankTransferId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating bank transfer", e);
        }
    }

    @Override
    public void transfer(int userId, String fromAccount, String toAccount, BigDecimal amount) {
        String sql = "CALL transfer_money(?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, fromAccount);
            stmt.setString(3, toAccount);
            stmt.setBigDecimal(4, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thực hiện chuyển khoản: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BankTransfer> findByUserId(int userId) {
        List<BankTransfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM BANK_TRANSFER WHERE SourceBankAccountNumber IN (SELECT BankAccountNumber FROM BANK_ACCOUNT WHERE UserID = ?) OR TargetBankAccountNumber IN (SELECT BankAccountNumber FROM BANK_ACCOUNT WHERE UserID = ?) ORDER BY Transfer_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transfers.add(mapResultSetToBankTransfer(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lấy danh sách chuyển khoản: " + e.getMessage(), e);
        }
        return transfers;
    }

    private BankTransfer mapResultSetToBankTransfer(ResultSet rs) throws SQLException {
        BankTransfer bankTransfer = new BankTransfer();
        bankTransfer.setBankTransferId(rs.getInt("BankTransferID"));
        bankTransfer.setSourceBankAccountNumber(rs.getString("SourceBankAccountNumber"));
        bankTransfer.setTargetBankAccountNumber(rs.getString("TargetBankAccountNumber"));
        bankTransfer.setTransferAmount(rs.getBigDecimal("Transfer_amount"));
        java.sql.Date transferDate = rs.getDate("Transfer_date");
        if (transferDate == null || transferDate.toString().equals("0000-00-00")) {
            transferDate = java.sql.Date.valueOf("2000-01-01");
        }
        bankTransfer.setTransferDate(transferDate);
        return bankTransfer;
    }
} 