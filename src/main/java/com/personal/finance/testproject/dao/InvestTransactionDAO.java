package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.InvestTransaction;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

public interface InvestTransactionDAO {
    // Create
    void insert(InvestTransaction transaction);
    
    // Read
    InvestTransaction findById(int transactionId);
    List<InvestTransaction> findByUserId(int userId);
    List<InvestTransaction> findByUserIdAndType(int userId, String transactionType);
    List<InvestTransaction> findByUserIdAndDateRange(int userId, Date startDate, Date endDate);
    List<InvestTransaction> findByUserIdAndMonth(int userId, int month);
    
    // Update
    void update(InvestTransaction transaction);
    
    // Delete
    void delete(int transactionId);
    
    // Additional methods
    List<InvestTransaction> getBuyTransactionsBySourceType(int userId, String sourceType);
    List<InvestTransaction> getSellTransactionsByAssetId(int userId, String inStId);
    BigDecimal getTotalBuyAmountByMonth(int userId, int month);
    BigDecimal getTotalSellAmountByMonth(int userId, int month);
} 