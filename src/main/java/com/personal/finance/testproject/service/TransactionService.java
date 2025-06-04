package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.TransactionDAO;
import com.personal.finance.testproject.dao.impl.TransactionDAOImpl;
import com.personal.finance.testproject.model.Transaction;
import com.personal.finance.testproject.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final Connection connection;

    public TransactionService() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        this.transactionDAO = new TransactionDAOImpl(connection);
    }

    public void addBuyTransaction(int userId, String instId, BigDecimal amount, Date transDate, int incomeId) {
        try {
            // Kiểm tra số dư thu nhập
            String sql = "SELECT remain_income FROM INCOME WHERE IncomeID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, incomeId);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Không tìm thấy thu nhập");
            }
            BigDecimal remainAmount = rs.getBigDecimal("remain_income");
            if (remainAmount.compareTo(amount) < 0) {
                throw new RuntimeException("Số dư thu nhập không đủ để thực hiện giao dịch");
            }

            // Thêm giao dịch mua
            transactionDAO.insertBuyTransaction(userId, instId, amount, transDate, incomeId);

            // Cập nhật số dư thu nhập
            sql = "UPDATE INCOME SET remain_income = remain_income - ? WHERE IncomeID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, incomeId);
            stmt.executeUpdate();

            // Cập nhật số lượng tài sản
            sql = "UPDATE INVEST_STORAGE SET num_unit = num_unit + 1 WHERE UserID = ? AND InStID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, instId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm giao dịch mua: " + e.getMessage(), e);
        }
    }

    public void addSellTransaction(int userId, String instId, BigDecimal amount, Date transDate) {
        try {
            // Lấy số lượng tài sản hiện có
            String sql = "SELECT num_unit FROM INVEST_STORAGE WHERE UserID = ? AND InStID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, instId);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Không tìm thấy tài sản đầu tư");
            }
            BigDecimal numUnit = rs.getBigDecimal("num_unit");
            if (numUnit.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Không đủ số lượng tài sản để bán");
            }
            // Lấy giá hiện tại
            sql = "SELECT cur_price FROM INVEST_STORAGE_DETAIL WHERE InStID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, instId);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Không tìm thấy giá hiện tại của tài sản");
            }
            BigDecimal curPrice = rs.getBigDecimal("cur_price");
            if (curPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Giá hiện tại không hợp lệ");
            }
            // Tính số đơn vị bán
            BigDecimal unitsSold = amount.divide(curPrice, 5, BigDecimal.ROUND_HALF_UP);
            if (unitsSold.compareTo(numUnit) > 0) {
                throw new RuntimeException("Không đủ số lượng để bán");
            }
            // Thêm giao dịch bán
            transactionDAO.insertSellTransaction(userId, instId, amount, transDate);
            // Cập nhật lại số lượng tài sản
            sql = "UPDATE INVEST_STORAGE SET num_unit = num_unit - ? WHERE UserID = ? AND InStID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, unitsSold);
            stmt.setInt(2, userId);
            stmt.setString(3, instId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thêm giao dịch bán: " + e.getMessage(), e);
        }
    }

    public List<Transaction> getTransactionsByUserId(int userId) {
        return transactionDAO.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByUserIdAndType(int userId, String type) {
        return transactionDAO.findByUserIdAndType(userId, type);
    }

    public List<Transaction> getTransactionsByDateRange(int userId, Date startDate, Date endDate) {
        return transactionDAO.findByUserIdAndDateRange(userId, startDate, endDate);
    }

    public BigDecimal getTotalBuyAmountByMonth(int userId, int month) {
        return transactionDAO.getTotalBuyAmountByMonth(userId, month);
    }

    public BigDecimal getTotalSellAmountByMonth(int userId, int month) {
        return transactionDAO.getTotalSellAmountByMonth(userId, month);
    }

    public BigDecimal getTotalProfitByMonth(int userId, int month) {
        return transactionDAO.getTotalProfitByMonth(userId, month);
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đóng kết nối: " + e.getMessage(), e);
        }
    }
} 