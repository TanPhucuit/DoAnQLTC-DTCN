package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Transaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransactionDAO {
    // Create
    void insertBuyTransaction(int userId, String instId, BigDecimal amount, Date transDate, int incomeId);
    void insertSellTransaction(int userId, String instId, BigDecimal amount, Date transDate);
    
    // Read
    Transaction findById(int transId);
    List<Transaction> findByUserId(int userId);
    List<Transaction> findByUserIdAndType(int userId, String type);
    List<Transaction> findByUserIdAndDateRange(int userId, Date startDate, Date endDate);
    
    // Update
    void update(Transaction transaction);
    
    // Delete
    void delete(int transId);
    
    // Additional methods
    BigDecimal getTotalBuyAmountByMonth(int userId, int month);
    BigDecimal getTotalSellAmountByMonth(int userId, int month);
    BigDecimal getTotalProfitByMonth(int userId, int month);
} 