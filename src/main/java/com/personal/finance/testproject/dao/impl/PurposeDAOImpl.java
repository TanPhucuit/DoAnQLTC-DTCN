package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.PurposeDAO;
import com.personal.finance.testproject.model.Purpose;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurposeDAOImpl implements PurposeDAO {
    private final Connection connection;

    public PurposeDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Purpose purpose) {
        String sql = "INSERT INTO PURPOSE (UserID, purpose_name, estimate_amount, description, Purpose_state) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, purpose.getUserId());
            stmt.setString(2, purpose.getPurposeName());
            stmt.setBigDecimal(3, purpose.getEstimateAmount());
            stmt.setString(4, purpose.getDescription());
            stmt.setBoolean(5, purpose.isPurposeState());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    purpose.setPurposeId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting purpose", e);
        }
    }

    @Override
    public Purpose findById(int purposeId, int userId) {
        String sql = "SELECT * FROM PURPOSE WHERE PurposeID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purposeId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Purpose purpose = new Purpose();
                    purpose.setPurposeId(rs.getInt("PurposeID"));
                    purpose.setUserId(rs.getInt("UserID"));
                    purpose.setPurposeName(rs.getString("purpose_name"));
                    purpose.setEstimateAmount(rs.getBigDecimal("estimate_amount"));
                    purpose.setDescription(rs.getString("description"));
                    purpose.setPurposeState(rs.getBoolean("Purpose_state"));
                    return purpose;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding purpose by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Purpose> findByUserId(int userId) {
        String sql = "SELECT * FROM PURPOSE WHERE UserID = ?";
        List<Purpose> purposes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Purpose purpose = new Purpose();
                    purpose.setPurposeId(rs.getInt("PurposeID"));
                    purpose.setUserId(rs.getInt("UserID"));
                    purpose.setPurposeName(rs.getString("purpose_name"));
                    purpose.setEstimateAmount(rs.getBigDecimal("estimate_amount"));
                    purpose.setDescription(rs.getString("description"));
                    purpose.setPurposeState(rs.getBoolean("Purpose_state"));
                    purposes.add(purpose);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding purposes by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return purposes;
    }

    @Override
    public List<Purpose> findAll() {
        String sql = "SELECT * FROM PURPOSE";
        List<Purpose> purposes = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Purpose purpose = new Purpose();
                purpose.setPurposeId(rs.getInt("PurposeID"));
                purpose.setUserId(rs.getInt("UserID"));
                purpose.setPurposeName(rs.getString("purpose_name"));
                purpose.setEstimateAmount(rs.getBigDecimal("estimate_amount"));
                purpose.setDescription(rs.getString("description"));
                purpose.setPurposeState(rs.getBoolean("Purpose_state"));
                purposes.add(purpose);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all purposes: " + e.getMessage());
            e.printStackTrace();
        }
        return purposes;
    }

    @Override
    public void update(Purpose purpose) {
        String sql = "UPDATE PURPOSE SET purpose_name = ?, estimate_amount = ?, description = ?, Purpose_state = ? WHERE PurposeID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, purpose.getPurposeName());
            stmt.setBigDecimal(2, purpose.getEstimateAmount());
            stmt.setString(3, purpose.getDescription());
            stmt.setBoolean(4, purpose.isPurposeState());
            stmt.setInt(5, purpose.getPurposeId());
            stmt.setInt(6, purpose.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating purpose", e);
        }
    }

    @Override
    public void delete(int purposeId, int userId) {
        String sql = "DELETE FROM PURPOSE WHERE PurposeID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purposeId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting purpose", e);
        }
    }
} 