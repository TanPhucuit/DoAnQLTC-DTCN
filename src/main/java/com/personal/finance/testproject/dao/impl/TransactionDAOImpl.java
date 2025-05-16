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
    public void insert(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTION (UserID, TypeID, trans_amount, trans_date, sold_num_unit, sold_profit, LoanID, InStID, IncomeID, SaveID, OverPayFeeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setString(2, transaction.getTypeId());
            stmt.setBigDecimal(3, transaction.getTransAmount());
            stmt.setDate(4, java.sql.Date.valueOf(transaction.getTransDate()));
            stmt.setBigDecimal(5, transaction.getSoldNumUnit());
            stmt.setBigDecimal(6, transaction.getSoldProfit());
            if (transaction.getLoanId() != null) stmt.setInt(7, transaction.getLoanId()); else stmt.setNull(7, java.sql.Types.INTEGER);
            stmt.setString(8, transaction.getInStId());
            if (transaction.getIncomeId() != null) stmt.setInt(9, transaction.getIncomeId()); else stmt.setNull(9, java.sql.Types.INTEGER);
            if (transaction.getSaveId() != null) stmt.setInt(10, transaction.getSaveId()); else stmt.setNull(10, java.sql.Types.INTEGER);
            if (transaction.getOverPayFeeId() != null) stmt.setInt(11, transaction.getOverPayFeeId()); else stmt.setNull(11, java.sql.Types.INTEGER);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting transaction", e);
        }
    }

    @Override
    public Transaction findById(int transId, int userId) {
        String sql = "SELECT * FROM TRANSACTION WHERE TransID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transId);
            stmt.setInt(2, userId);
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
    public List<Transaction> findByUserId(int userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ?";
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
    public List<Transaction> findByAmountRange(int userId, BigDecimal minAmount, BigDecimal maxAmount) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND trans_amount BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setBigDecimal(2, minAmount);
            stmt.setBigDecimal(3, maxAmount);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by amount range", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findBySourceType(int userId, String sourceType) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND TypeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, sourceType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by source type", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByDateRange(int userId, Date startDate, Date endDate) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM TRANSACTION WHERE UserID = ? AND trans_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by date range: " + e.getMessage(), e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all transactions", e);
        }
        return transactions;
    }

    @Override
    public void update(Transaction transaction) {
        String sql = "UPDATE TRANSACTION SET TypeID = ?, trans_amount = ?, trans_date = ?, sold_num_unit = ?, sold_profit = ?, LoanID = ?, InStID = ?, IncomeID = ?, SaveID = ?, OverPayFeeID = ? WHERE TransID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, transaction.getTypeId());
            stmt.setBigDecimal(2, transaction.getTransAmount());
            stmt.setDate(3, java.sql.Date.valueOf(transaction.getTransDate()));
            stmt.setBigDecimal(4, transaction.getSoldNumUnit());
            stmt.setBigDecimal(5, transaction.getSoldProfit());
            if (transaction.getLoanId() != null) stmt.setInt(6, transaction.getLoanId()); else stmt.setNull(6, java.sql.Types.INTEGER);
            stmt.setString(7, transaction.getInStId());
            if (transaction.getIncomeId() != null) stmt.setInt(8, transaction.getIncomeId()); else stmt.setNull(8, java.sql.Types.INTEGER);
            if (transaction.getSaveId() != null) stmt.setInt(9, transaction.getSaveId()); else stmt.setNull(9, java.sql.Types.INTEGER);
            if (transaction.getOverPayFeeId() != null) stmt.setInt(10, transaction.getOverPayFeeId()); else stmt.setNull(10, java.sql.Types.INTEGER);
            stmt.setInt(11, transaction.getTransId());
            stmt.setInt(12, transaction.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating transaction", e);
        }
    }

    @Override
    public List<Transaction> findByTypeId(int userId, String typeId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND TypeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, typeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by typeId", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByLoanId(int userId, int loanId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND LoanID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, loanId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by loanId", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByInStId(int userId, String inStId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND InStID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, inStId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by inStId", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByIncomeId(int userId, int incomeId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND IncomeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, incomeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by incomeId", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findBySaveId(int userId, int saveId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND SaveID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, saveId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by saveId", e);
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByOverPayFeeId(int userId, int overPayFeeId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND OverPayFeeID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, overPayFeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transactions by overPayFeeId", e);
        }
        return transactions;
    }

    @Override
    public void delete(int transId, int userId) {
        String sql = "DELETE FROM TRANSACTION WHERE TransID = ? AND UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting transaction", e);
        }
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransId(rs.getInt("TransID"));
        transaction.setUserId(rs.getInt("UserID"));
        transaction.setTypeId(rs.getString("TypeID"));
        transaction.setTransAmount(rs.getBigDecimal("trans_amount"));
        transaction.setTransDate(rs.getDate("trans_date").toLocalDate());
        transaction.setSoldNumUnit(rs.getBigDecimal("sold_num_unit"));
        transaction.setSoldProfit(rs.getBigDecimal("sold_profit"));
        
        int loanId = rs.getInt("LoanID");
        transaction.setLoanId(rs.wasNull() ? null : loanId);
        
        transaction.setInStId(rs.getString("InStID"));
        
        int incomeId = rs.getInt("IncomeID");
        transaction.setIncomeId(rs.wasNull() ? null : incomeId);
        
        int saveId = rs.getInt("SaveID");
        transaction.setSaveId(rs.wasNull() ? null : saveId);
        
        int overPayFeeId = rs.getInt("OverPayFeeID");
        transaction.setOverPayFeeId(rs.wasNull() ? null : overPayFeeId);
        
        return transaction;
    }
} 