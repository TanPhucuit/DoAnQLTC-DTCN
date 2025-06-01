package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.SavingDAO;
import com.personal.finance.testproject.model.Saving;
import com.personal.finance.testproject.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;

public class SavingDAOImpl implements SavingDAO {
    private final Connection connection;

    public SavingDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Saving saving) {
        String sql = "INSERT INTO SAVING (UserID, PurposeID, BankAccountNumber, save_name, save_amount, remain_save, up_date, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, saving.getUserId());
            stmt.setInt(2, saving.getPurposeId());
            stmt.setString(3, saving.getBankAccountNumber());
            stmt.setString(4, saving.getSaveName());
            stmt.setBigDecimal(5, saving.getSaveAmount());
            stmt.setBigDecimal(6, saving.getRemainSave());
            stmt.setDate(7, new java.sql.Date(saving.getUpDate().getTime()));
            stmt.setString(8, saving.getDescription());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    saving.setSaveId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Saving saving) {
        String sql = "UPDATE SAVING SET PurposeID = ?, BankAccountNumber = ?, save_name = ?, save_amount = ?, remain_save = ?, up_date = ?, description = ? WHERE SaveID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, saving.getPurposeId());
            stmt.setString(2, saving.getBankAccountNumber());
            stmt.setString(3, saving.getSaveName());
            stmt.setBigDecimal(4, saving.getSaveAmount());
            stmt.setBigDecimal(5, saving.getRemainSave());
            stmt.setDate(6, new java.sql.Date(saving.getUpDate().getTime()));
            stmt.setString(7, saving.getDescription());
            stmt.setInt(8, saving.getSaveId());
            stmt.setInt(9, saving.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  

   

    @Override
    public List<Saving> findByUserID(int userId) {
        List<Saving> savings = new ArrayList<>();
        String sql = "SELECT * FROM SAVING WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    savings.add(mapResultSetToSaving(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savings;
    }


    @Override
    public void updateRemainSave(int savingId, int userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
        String sql = "UPDATE SAVING SET remain_save = remain_save - ? " +
                    "WHERE SaveID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, amount.setScale(5, BigDecimal.ROUND_HALF_UP));
            stmt.setInt(2, savingId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating remain save", e);
        }
    }

 

    private Saving mapResultSetToSaving(ResultSet rs) throws SQLException {
        Saving saving = new Saving();
        saving.setSaveId(rs.getInt("SaveID"));
        saving.setUserId(rs.getInt("UserID"));
        saving.setPurposeId(rs.getInt("PurposeID"));
        saving.setBankAccountNumber(rs.getString("BankAccountNumber"));
        saving.setSaveName(rs.getString("save_name"));
        saving.setSaveAmount(rs.getBigDecimal("save_amount"));
        saving.setRemainSave(rs.getBigDecimal("remain_save"));
        java.sql.Date upDate = rs.getDate("up_date");
        if (upDate == null || upDate.toString().equals("0000-00-00")) {
            upDate = java.sql.Date.valueOf("2000-01-01");
        }
        saving.setUpDate(upDate);
        saving.setDescription(rs.getString("description"));
        return saving;
    }

    private void validateSaving(Saving saving) {
        if (saving == null) {
            throw new IllegalArgumentException("Saving cannot be null");
        }
        if (saving.getUserId() <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (saving.getSaveId() <= 0) {
            throw new IllegalArgumentException("SaveID must be positive");
        }
        if (saving.getPurposeId() <= 0) {
            throw new IllegalArgumentException("PurposeID must be positive");
        }
        if (saving.getBankAccountNumber() == null || saving.getBankAccountNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number cannot be null or empty");
        }
        if (saving.getSaveAmount() == null || saving.getSaveAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Save amount cannot be null or negative");
        }
        if (saving.getRemainSave() == null || saving.getRemainSave().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Remain save cannot be null or negative");
        }
        if (saving.getRemainSave().compareTo(saving.getSaveAmount()) > 0) {
            throw new IllegalArgumentException("Remain save cannot be greater than save amount");
        }
    }
} 