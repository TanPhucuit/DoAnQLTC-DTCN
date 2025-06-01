package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvestTransaction {
    private int transactionId;
    private String inStId;
    private int userId;
    private String transactionType; // "BUY" hoặc "SELL"
    private BigDecimal amount;
    private String sourceType; // "SALARY" hoặc "ALLOWANCE" cho giao dịch mua
    private Date transactionDate;
    private int icMonth; // Tháng thu nhập cho giao dịch mua

    public InvestTransaction() {}

    public InvestTransaction(String inStId, int userId, String transactionType, 
                           BigDecimal amount, String sourceType, Date transactionDate, int icMonth) {
        this.inStId = inStId;
        this.userId = userId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.sourceType = sourceType;
        this.transactionDate = transactionDate;
        this.icMonth = icMonth;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getInStId() {
        return inStId;
    }

    public void setInStId(String inStId) {
        if (inStId == null || inStId.trim().isEmpty()) {
            throw new IllegalArgumentException("Investment storage ID cannot be null or empty");
        }
        this.inStId = inStId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.userId = userId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        if (!transactionType.equals("BUY") && !transactionType.equals("SELL")) {
            throw new IllegalArgumentException("Transaction type must be either BUY or SELL");
        }
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.amount = amount;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        if (sourceType != null && !sourceType.equals("SALARY") && !sourceType.equals("ALLOWANCE")) {
            throw new IllegalArgumentException("Source type must be either SALARY or ALLOWANCE");
        }
        this.sourceType = sourceType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        if (transactionDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        this.transactionDate = transactionDate;
    }

    public int getIcMonth() {
        return icMonth;
    }

    public void setIcMonth(int icMonth) {
        if (icMonth < 1 || icMonth > 12) {
            throw new IllegalArgumentException("Income month must be between 1 and 12");
        }
        this.icMonth = icMonth;
    }
} 