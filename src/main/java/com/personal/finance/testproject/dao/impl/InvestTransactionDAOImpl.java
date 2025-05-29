package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.InvestTransactionDAO;
import com.personal.finance.testproject.model.InvestTransaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

public class InvestTransactionDAOImpl implements InvestTransactionDAO {
    private final Connection connection;

    public InvestTransactionDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(InvestTransaction transaction) {
        String sql = "INSERT INTO INVEST_TRANSACTION (InStID, UserID, transaction_type, amount, " +
                    "source_type, transaction_date, ic_month) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, transaction.getInStId());
            stmt.setInt(2, transaction.getUserId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setString(5, transaction.getSourceType());
            stmt.setDate(6, new java.sql.Date(transaction.getTransactionDate().getTime()));
            stmt.setInt(7, transaction.getIcMonth());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setTransactionId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting investment transaction", e);
        }
    }

    @Override
    public InvestTransaction findById(int transactionId) {
        String sql = "SELECT * FROM INVEST_TRANSACTION WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transaction by ID", e);
        }
        return null;
    }

    @Override
    public List<InvestTransaction> findByUserId(int userId) {
        List<InvestTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_TRANSACTION WHERE UserID = ? ORDER BY transaction_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by user ID", e);
        }
        return transactions;
    }

    @Override
    public List<InvestTransaction> findByUserIdAndType(int userId, String transactionType) {
        List<InvestTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_TRANSACTION WHERE UserID = ? AND transaction_type = ? " +
                    "ORDER BY transaction_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, transactionType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by user ID and type", e);
        }
        return transactions;
    }

    @Override
    public List<InvestTransaction> findByUserIdAndDateRange(int userId, Date startDate, Date endDate) {
        List<InvestTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_TRANSACTION WHERE UserID = ? AND transaction_date BETWEEN ? AND ? " +
                    "ORDER BY transaction_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by date range", e);
        }
        return transactions;
    }

    @Override
    public List<InvestTransaction> findByUserIdAndMonth(int userId, int month) {
        List<InvestTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_TRANSACTION WHERE UserID = ? AND MONTH(transaction_date) = ? " +
                    "ORDER BY transaction_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by month", e);
        }
        return transactions;
    }

    @Override
    public void update(InvestTransaction transaction) {
        String sql = "UPDATE INVEST_TRANSACTION SET amount = ?, source_type = ?, " +
                    "transaction_date = ?, ic_month = ? WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, transaction.getAmount());
            stmt.setString(2, transaction.getSourceType());
            stmt.setDate(3, new java.sql.Date(transaction.getTransactionDate().getTime()));
            stmt.setInt(4, transaction.getIcMonth());
            stmt.setInt(5, transaction.getTransactionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }

    @Override
    public void delete(int transactionId) {
        String sql = "DELETE FROM INVEST_TRANSACTION WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        }
    }

    @Override
    public List<InvestTransaction> getBuyTransactionsBySourceType(int userId, String sourceType) {
        List<InvestTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_TRANSACTION WHERE UserID = ? AND transaction_type = 'BUY' " +
                    "AND source_type = ? ORDER BY transaction_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, sourceType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding buy transactions by source type", e);
        }
        return transactions;
    }

    @Override
    public List<InvestTransaction> getSellTransactionsByAssetId(int userId, String inStId) {
        List<InvestTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_TRANSACTION WHERE UserID = ? AND transaction_type = 'SELL' " +
                    "AND InStID = ? ORDER BY transaction_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, inStId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding sell transactions by asset ID", e);
        }
        return transactions;
    }

    @Override
    public BigDecimal getTotalBuyAmountByMonth(int userId, int month) {
        String sql = "SELECT SUM(amount) as total FROM INVEST_TRANSACTION " +
                    "WHERE UserID = ? AND transaction_type = 'BUY' AND MONTH(transaction_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total buy amount by month", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalSellAmountByMonth(int userId, int month) {
        String sql = "SELECT SUM(amount) as total FROM INVEST_TRANSACTION " +
                    "WHERE UserID = ? AND transaction_type = 'SELL' AND MONTH(transaction_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total sell amount by month", e);
        }
        return BigDecimal.ZERO;
    }

    private InvestTransaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        InvestTransaction transaction = new InvestTransaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setInStId(rs.getString("InStID"));
        transaction.setUserId(rs.getInt("UserID"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getBigDecimal("amount"));
        transaction.setSourceType(rs.getString("source_type"));
        transaction.setTransactionDate(rs.getDate("transaction_date"));
        transaction.setIcMonth(rs.getInt("ic_month"));
        return transaction;
    }
} 