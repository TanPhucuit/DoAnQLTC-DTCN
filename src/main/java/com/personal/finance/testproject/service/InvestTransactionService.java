package com.personal.finance.testproject.service;

import com.personal.finance.testproject.model.InvestTransaction;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

public interface InvestTransactionService {
    // Giao dịch mua
    void buyAsset(String inStId, int userId, BigDecimal amount, String sourceType, Date transactionDate) 
        throws IllegalArgumentException;
    
    // Giao dịch bán
    void sellAsset(String inStId, int userId, BigDecimal amount, Date transactionDate) 
        throws IllegalArgumentException;
    
    // Lấy danh sách giao dịch
    List<InvestTransaction> getTransactionsByUserId(int userId);
    List<InvestTransaction> getBuyTransactionsByUserId(int userId);
    List<InvestTransaction> getSellTransactionsByUserId(int userId);
    List<InvestTransaction> getTransactionsByDateRange(int userId, Date startDate, Date endDate);
    List<InvestTransaction> getTransactionsByMonth(int userId, int month);
    
    // Lấy thống kê
    BigDecimal getTotalBuyAmountByMonth(int userId, int month);
    BigDecimal getTotalSellAmountByMonth(int userId, int month);
    BigDecimal getTotalBuyAmountBySourceType(int userId, String sourceType);
    BigDecimal getTotalSellAmountByAssetId(int userId, String inStId);
    
    // Kiểm tra số dư
    boolean checkIncomeBalance(int userId, String sourceType, int month, BigDecimal amount);
    boolean checkAssetBalance(int userId, String inStId);
    
    // Cập nhật số dư
    void updateIncomeBalance(int userId, String sourceType, int month, BigDecimal amount);
    void updateAssetBalance(int userId, String inStId, BigDecimal amount, boolean isBuy);
} 