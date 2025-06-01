package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.InvestStorageDetailDAO;
import com.personal.finance.testproject.model.InvestStorageDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class InvestStorageDetailDAOImpl implements InvestStorageDetailDAO {
    private final Connection connection;

    public InvestStorageDetailDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(InvestStorageDetail detail) {
        String sql = "INSERT INTO INVEST_STORAGE_DETAIL (InStID, riskLevel, cur_price, z_score, standard_deviation, unit) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, detail.getInStId());
            stmt.setInt(2, detail.getRiskLevel());
            stmt.setBigDecimal(3, detail.getCurPrice());
            stmt.setBigDecimal(4, detail.getZScore());
            stmt.setBigDecimal(5, detail.getStandardDeviation());
            stmt.setString(6, detail.getUnit());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting investment storage detail", e);
        }
    }

    @Override
    public InvestStorageDetail findById(String inStId) {
        String sql = "SELECT * FROM INVEST_STORAGE_DETAIL WHERE InStID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, inStId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDetail(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding investment storage detail by ID", e);
        }
        return null;
    }

    @Override
    public List<InvestStorageDetail> findByRiskLevel(int riskLevel) {
        List<InvestStorageDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_STORAGE_DETAIL WHERE riskLevel = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, riskLevel);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                details.add(mapResultSetToDetail(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding investment storage details by risk level", e);
        }
        return details;
    }

    @Override
    public List<InvestStorageDetail> findAll() {
        List<InvestStorageDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_STORAGE_DETAIL";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                details.add(mapResultSetToDetail(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all investment storage details", e);
        }
        return details;
    }

    @Override
    public void update(InvestStorageDetail detail) {
        String sql = "UPDATE INVEST_STORAGE_DETAIL SET riskLevel = ?, cur_price = ?, " +
                    "z_score = ?, standard_deviation = ?, unit = ? WHERE InStID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detail.getRiskLevel());
            stmt.setBigDecimal(2, detail.getCurPrice());
            stmt.setBigDecimal(3, detail.getZScore());
            stmt.setBigDecimal(4, detail.getStandardDeviation());
            stmt.setString(5, detail.getUnit());
            stmt.setString(6, detail.getInStId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating investment storage detail", e);
        }
    }

    @Override
    public void updateCurPrice(String inStId, BigDecimal curPrice) {
        String sql = "UPDATE INVEST_STORAGE_DETAIL SET cur_price = ? WHERE InStID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, curPrice);
            stmt.setString(2, inStId);
            int affected = stmt.executeUpdate();
            System.out.println("[LOG][DAO] Update cur_price for " + inStId + ": " + curPrice + " (affected rows: " + affected + ")");
            
            // Nếu là BTC, cập nhật cả mã còn lại
            if ("IN_BTC".equals(inStId)) {
                stmt.setString(2, "Short_BTC");
                affected = stmt.executeUpdate();
                System.out.println("[LOG][DAO] Update cur_price for Short_BTC: " + curPrice + " (affected rows: " + affected + ")");
            } else if ("Short_BTC".equals(inStId)) {
                stmt.setString(2, "IN_BTC");
                affected = stmt.executeUpdate();
                System.out.println("[LOG][DAO] Update cur_price for IN_BTC: " + curPrice + " (affected rows: " + affected + ")");
            } else if ("IN_ETH".equals(inStId)) {
                stmt.setString(2, "Short_ETH");
                affected = stmt.executeUpdate();
                System.out.println("[LOG][DAO] Update cur_price for Short_ETH: " + curPrice + " (affected rows: " + affected + ")");
            } else if ("Short_ETH".equals(inStId)) {
                stmt.setString(2, "IN_ETH");
                affected = stmt.executeUpdate();
                System.out.println("[LOG][DAO] Update cur_price for IN_ETH: " + curPrice + " (affected rows: " + affected + ")");
            }
            
            if (affected == 0) {
                System.err.println("[ERROR][DAO] Không update được cur_price cho " + inStId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating current price", e);
        }
    }

    @Override
    public void updateZScore(String inStId, BigDecimal zScore) {
        String sql = "UPDATE INVEST_STORAGE_DETAIL SET z_score = ? WHERE InStID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, zScore);
            stmt.setString(2, inStId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Z-score", e);
        }
    }

    @Override
    public void updateStandardDeviation(String inStId, BigDecimal standardDeviation) {
        String sql = "UPDATE INVEST_STORAGE_DETAIL SET standard_deviation = ? WHERE InStID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, standardDeviation);
            stmt.setString(2, inStId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating standard deviation", e);
        }
    }

    @Override
    public void delete(String inStId) {
        String sql = "DELETE FROM INVEST_STORAGE_DETAIL WHERE InStID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, inStId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting investment storage detail", e);
        }
    }

    @Override
    public List<InvestStorageDetail> getDetailsByRiskRange(int minRisk, int maxRisk) {
        List<InvestStorageDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_STORAGE_DETAIL WHERE riskLevel BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, minRisk);
            stmt.setInt(2, maxRisk);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                details.add(mapResultSetToDetail(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding investment storage details by risk range", e);
        }
        return details;
    }

    @Override
    public BigDecimal getAverageCurPriceByRiskLevel(int riskLevel) {
        String sql = "SELECT AVG(cur_price) as avg_price FROM INVEST_STORAGE_DETAIL WHERE riskLevel = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, riskLevel);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("avg_price");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating average current price by risk level", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getMaxStandardDeviation() {
        String sql = "SELECT MAX(standard_deviation) as max_dev FROM INVEST_STORAGE_DETAIL";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal("max_dev");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting maximum standard deviation", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getMinStandardDeviation() {
        String sql = "SELECT MIN(standard_deviation) as min_dev FROM INVEST_STORAGE_DETAIL";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal("min_dev");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting minimum standard deviation", e);
        }
        return BigDecimal.ZERO;
    }

    private InvestStorageDetail mapResultSetToDetail(ResultSet rs) throws SQLException {
        InvestStorageDetail detail = new InvestStorageDetail();
        detail.setInStId(rs.getString("InStID"));
        detail.setRiskLevel(rs.getInt("riskLevel"));
        detail.setCurPrice(rs.getBigDecimal("cur_price"));
        detail.setZScore(rs.getBigDecimal("z_score"));
        detail.setStandardDeviation(rs.getBigDecimal("standard_deviation"));
        detail.setUnit(rs.getString("unit"));
        return detail;
    }
} 