package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.InvestStorageDAO;
import com.personal.finance.testproject.model.InvestStorage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class InvestStorageDAOImpl implements InvestStorageDAO {
    private final Connection connection;

    public InvestStorageDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(InvestStorage investStorage) {
        String sql = "INSERT INTO INVEST_STORAGE (InStID, UserID, num_unit, buy_price, es_profit, up_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, investStorage.getInStId());
            stmt.setInt(2, investStorage.getUserId());
            stmt.setBigDecimal(3, investStorage.getNumUnit());
            stmt.setBigDecimal(4, investStorage.getBuyPrice());
            stmt.setBigDecimal(5, investStorage.getEsProfit());
            stmt.setDate(6, new java.sql.Date(investStorage.getUpDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting investment storage", e);
        }
    }

    @Override
    public InvestStorage findById(String inStId, int userId) {
        String sql = "SELECT * FROM INVEST_STORAGE WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, inStId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvestStorage(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding investment storage by ID", e);
        }
        return null;
    }

    @Override
    public List<InvestStorage> findByUserId(int userId) {
        List<InvestStorage> investments = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_STORAGE WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    investments.add(mapResultSetToInvestStorage(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding investments by user ID", e);
        }
        return investments;
    }

    @Override
    public List<InvestStorage> findByRiskLevel(int riskLevel) {
        List<InvestStorage> investments = new ArrayList<>();
        String sql = "SELECT ist.* FROM INVEST_STORAGE ist " +
                    "JOIN INVEST_STORAGE_DETAIL isd ON ist.InStID = isd.InStID " +
                    "WHERE isd.riskLevel = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, riskLevel);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    investments.add(mapResultSetToInvestStorage(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding investments by risk level", e);
        }
        return investments;
    }

    @Override
    public List<InvestStorage> findAll() {
        List<InvestStorage> investments = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_STORAGE";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                investments.add(mapResultSetToInvestStorage(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all investments", e);
        }
        return investments;
    }

    @Override
    public void update(InvestStorage investStorage) {
        String sql = "UPDATE INVEST_STORAGE SET num_unit = ?, buy_price = ?, " +
                    "es_profit = ?, up_date = ? WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, investStorage.getNumUnit());
            stmt.setBigDecimal(2, investStorage.getBuyPrice());
            stmt.setBigDecimal(3, investStorage.getEsProfit());
            stmt.setDate(4, new java.sql.Date(investStorage.getUpDate().getTime()));
            stmt.setString(5, investStorage.getInStId());
            stmt.setInt(6, investStorage.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating investment storage", e);
        }
    }

    @Override
    public void updateNumUnit(String inStId, int userId, BigDecimal numUnit) {
        String sql = "UPDATE INVEST_STORAGE SET num_unit = ?, up_date = CURDATE() " +
                    "WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, numUnit);
            stmt.setString(2, inStId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating number of units", e);
        }
    }

    @Override
    public void updateBuyPrice(String inStId, int userId, BigDecimal buyPrice) {
        String sql = "UPDATE INVEST_STORAGE SET buy_price = ?, up_date = CURDATE() " +
                    "WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, buyPrice);
            stmt.setString(2, inStId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating buy price", e);
        }
    }

    @Override
    public void updateEsProfit(String inStId, int userId, BigDecimal esProfit) {
        String sql = "UPDATE INVEST_STORAGE SET es_profit = ?, up_date = CURDATE() " +
                    "WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, esProfit);
            stmt.setString(2, inStId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating estimated profit", e);
        }
    }

    @Override
    public void delete(String inStId, int userId) {
        String sql = "DELETE FROM INVEST_STORAGE WHERE InStID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, inStId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting investment storage", e);
        }
    }

    @Override
    public BigDecimal getTotalInvestValue(int userId) {
        String sql = "SELECT ist.num_unit, isd.cur_price " +
                    "FROM INVEST_STORAGE ist " +
                    "JOIN INVEST_STORAGE_DETAIL isd ON ist.InStID = isd.InStID " +
                    "WHERE ist.UserID = ?";
        BigDecimal totalValue = BigDecimal.ZERO;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BigDecimal numUnit = rs.getBigDecimal("num_unit");
                    BigDecimal curPrice = rs.getBigDecimal("cur_price");
                    if (numUnit != null && curPrice != null) {
                        totalValue = totalValue.add(numUnit.multiply(curPrice));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total investment value", e);
        }
        return totalValue;
    }

    @Override
    public BigDecimal getTotalEsProfit(int userId) {
        String sql = "SELECT SUM(es_profit) as total_profit FROM INVEST_STORAGE WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_profit");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total estimated profit", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<InvestStorage> getInvestmentsByRiskLevel(int userId, int riskLevel) {
        List<InvestStorage> investments = new ArrayList<>();
        String sql = "SELECT ist.* FROM INVEST_STORAGE ist " +
                    "JOIN INVEST_STORAGE_DETAIL isd ON ist.InStID = isd.InStID " +
                    "WHERE ist.UserID = ? AND isd.riskLevel = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, riskLevel);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    investments.add(mapResultSetToInvestStorage(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding investments by risk level", e);
        }
        return investments;
    }

    @Override
    public BigDecimal getTotalInvestValueByRiskLevel(int userId, int riskLevel) {
        String sql = "SELECT SUM(ist.num_unit * isd.cur_price) as total_value " +
                    "FROM INVEST_STORAGE ist " +
                    "JOIN INVEST_STORAGE_DETAIL isd ON ist.InStID = isd.InStID " +
                    "WHERE ist.UserID = ? AND isd.riskLevel = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, riskLevel);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_value");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total investment value by risk level", e);
        }
        return BigDecimal.ZERO;
    }

    private InvestStorage mapResultSetToInvestStorage(ResultSet rs) throws SQLException {
        InvestStorage investStorage = new InvestStorage();
        investStorage.setInStId(rs.getString("InStID"));
        investStorage.setUserId(rs.getInt("UserID"));
        investStorage.setNumUnit(rs.getBigDecimal("num_unit"));
        investStorage.setBuyPrice(rs.getBigDecimal("buy_price"));
        investStorage.setEsProfit(rs.getBigDecimal("es_profit"));
        java.sql.Date upDate = rs.getDate("up_date");
        if (upDate == null || upDate.toString().equals("0000-00-00")) {
            upDate = java.sql.Date.valueOf("2000-01-01");
        }
        investStorage.setUpDate(upDate);
        return investStorage;
    }
} 