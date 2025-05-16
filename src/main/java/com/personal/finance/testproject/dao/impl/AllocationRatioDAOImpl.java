package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.AllocationRatioDAO;
import com.personal.finance.testproject.model.AllocationRatio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class AllocationRatioDAOImpl implements AllocationRatioDAO {
    private Connection connection;

    public AllocationRatioDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(AllocationRatio allocationRatio) {
        String sql = "INSERT INTO ALLOCATION_RATIO (Investor_type, lv1_rate, lv2_rate, lv3_rate) " +
                    "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, allocationRatio.getInvestorType());
            stmt.setBigDecimal(2, allocationRatio.getLv1Rate());
            stmt.setBigDecimal(3, allocationRatio.getLv2Rate());
            stmt.setBigDecimal(4, allocationRatio.getLv3Rate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting allocation ratio", e);
        }
    }

    @Override
    public AllocationRatio findByInvestorType(String investorType) {
        String sql = "SELECT * FROM ALLOCATION_RATIO WHERE Investor_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, investorType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAllocationRatio(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding allocation ratio by investor type", e);
        }
        return null;
    }

    @Override
    public List<AllocationRatio> findAll() {
        List<AllocationRatio> allocationRatios = new ArrayList<>();
        String sql = "SELECT * FROM ALLOCATION_RATIO";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                allocationRatios.add(mapResultSetToAllocationRatio(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all allocation ratios", e);
        }
        return allocationRatios;
    }

    @Override
    public void update(AllocationRatio allocationRatio) {
        String sql = "UPDATE ALLOCATION_RATIO SET lv1_rate = ?, lv2_rate = ?, lv3_rate = ? " +
                    "WHERE Investor_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, allocationRatio.getLv1Rate());
            stmt.setBigDecimal(2, allocationRatio.getLv2Rate());
            stmt.setBigDecimal(3, allocationRatio.getLv3Rate());
            stmt.setString(4, allocationRatio.getInvestorType());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating allocation ratio", e);
        }
    }

 

    @Override
    public void updateLv1Rate(String investorType, double rate) {
        String sql = "UPDATE ALLOCATION_RATIO SET lv1_rate = ? WHERE Investor_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(rate));
            stmt.setString(2, investorType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating level 1 rate", e);
        }
    }

    @Override
    public void updateLv2Rate(String investorType, double rate) {
        String sql = "UPDATE ALLOCATION_RATIO SET lv2_rate = ? WHERE Investor_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(rate));
            stmt.setString(2, investorType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating level 2 rate", e);
        }
    }

    @Override
    public void updateLv3Rate(String investorType, double rate) {
        String sql = "UPDATE ALLOCATION_RATIO SET lv3_rate = ? WHERE Investor_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, BigDecimal.valueOf(rate));
            stmt.setString(2, investorType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating level 3 rate", e);
        }
    }

    private AllocationRatio mapResultSetToAllocationRatio(ResultSet rs) throws SQLException {
        AllocationRatio allocationRatio = new AllocationRatio();
        allocationRatio.setInvestorType(rs.getString("Investor_type"));
        allocationRatio.setLv1Rate(rs.getBigDecimal("lv1_rate"));
        allocationRatio.setLv2Rate(rs.getBigDecimal("lv2_rate"));
        allocationRatio.setLv3Rate(rs.getBigDecimal("lv3_rate"));
        return allocationRatio;
    }
} 