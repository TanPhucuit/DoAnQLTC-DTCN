package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.TransactionDAO;
import com.personal.finance.testproject.model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionDAOImpl implements TransactionDAO {
    private final Connection connection;

    public TransactionDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertBuyTransaction(int userId, String instId, BigDecimal amount, Date transDate, int incomeId) {
        String sql = "INSERT INTO TRANSACTION (UserID, TypeID, trans_amount, trans_date, InStID, IncomeID) VALUES (?, 'InSt_Buy', ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setBigDecimal(2, amount);
            stmt.setDate(3, new java.sql.Date(transDate.getTime()));
            stmt.setString(4, instId);
            stmt.setInt(5, incomeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting buy transaction", e);
        }
    }

    @Override
    public void insertSellTransaction(int userId, String instId, BigDecimal amount, Date transDate) {
        String sql = "INSERT INTO TRANSACTION (UserID, TypeID, trans_amount, trans_date, InStID) VALUES (?, 'InSt_Sell', ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setBigDecimal(2, amount);
            stmt.setDate(3, new java.sql.Date(transDate.getTime()));
            stmt.setString(4, instId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting sell transaction", e);
        }
    }

    @Override
    public Transaction findById(int transId) {
        String sql = "SELECT * FROM TRANSACTION WHERE TransID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transaction by ID", e);
        }
        return null;
    }

    @Override
    public List<Transaction> findByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by user ID", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByUserIdAndType(int userId, String type) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND TypeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by user ID and type", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByUserIdAndDateRange(int userId, Date startDate, Date endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND trans_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by date range", e);
        }
        return transactions;
    }

    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE TRANSACTION SET TypeID = ?, trans_amount = ?, trans_date = ?, InStID = ?, IncomeID = ? WHERE TransID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getTypeId());
            stmt.setBigDecimal(2, transaction.getTransAmount());
            stmt.setDate(3, new java.sql.Date(transaction.getTransDate().getTime()));
            stmt.setString(4, transaction.getInStId());
            stmt.setInt(5, transaction.getIncomeId());
            stmt.setInt(6, transaction.getTransId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }

    @Override
    public void delete(int transId) {
        String sql = "DELETE FROM TRANSACTION WHERE TransID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        }
    }

    @Override
    public BigDecimal getTotalBuyAmountByMonth(int userId, int month) {
        String sql = "SELECT COALESCE(SUM(trans_amount), 0) as total FROM TRANSACTION WHERE UserID = ? AND TypeID = 'InSt_Buy' AND MONTH(trans_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total buy amount by month", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalSellAmountByMonth(int userId, int month) {
        String sql = "SELECT COALESCE(SUM(trans_amount), 0) as total FROM TRANSACTION WHERE UserID = ? AND TypeID = 'InSt_Sell' AND MONTH(trans_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total sell amount by month", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getTotalProfitByMonth(int userId, int month) {
        String sql = "SELECT COALESCE(SUM(CASE WHEN TypeID = 'InSt_Sell' THEN trans_amount ELSE -trans_amount END), 0) as profit " +
                     "FROM TRANSACTION WHERE UserID = ? AND TypeID IN ('InSt_Buy', 'InSt_Sell') AND MONTH(trans_date) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("profit");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total profit by month", e);
        }
        return BigDecimal.ZERO;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransId(rs.getInt("TransID"));
        transaction.setTypeId(rs.getString("TypeID"));
        transaction.setUserId(rs.getInt("UserID"));
        transaction.setTransAmount(rs.getBigDecimal("trans_amount"));
        transaction.setTransDate(rs.getDate("trans_date"));
        transaction.setInStId(rs.getString("InStID"));
        transaction.setIncomeId(rs.getInt("IncomeID"));
        return transaction;
    }
} 