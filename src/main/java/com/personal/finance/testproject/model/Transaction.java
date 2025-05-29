package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private int transId;
    private String typeId;
    private int userId;
    private BigDecimal transAmount;
    private Date transDate;
    private String inStId;
    private Integer incomeId;

    public Transaction() {}

    public Transaction(int userId, String typeId, BigDecimal transAmount, Date transDate, String inStId, Integer incomeId) {
        this.userId = userId;
        this.typeId = typeId;
        this.transAmount = transAmount;
        this.transDate = transDate;
        this.inStId = inStId;
        this.incomeId = incomeId;
    }

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        if (transId <= 0) {
            throw new IllegalArgumentException("Transaction ID must be positive");
        }
        this.transId = transId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        if (typeId == null || (!typeId.equals("InSt_Buy") && !typeId.equals("InSt_Sell"))) {
            throw new IllegalArgumentException("Type ID must be either 'InSt_Buy' or 'InSt_Sell'");
        }
        this.typeId = typeId;
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

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        if (transAmount == null || transAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount cannot be null or negative");
        }
        this.transAmount = transAmount;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        if (transDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        this.transDate = transDate;
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

    public Integer getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(Integer incomeId) {
        if (incomeId != null && incomeId <= 0) {
            throw new IllegalArgumentException("Income ID must be positive if not null");
        }
        this.incomeId = incomeId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transId=" + transId +
                ", typeId='" + typeId + '\'' +
                ", userId=" + userId +
                ", transAmount=" + transAmount +
                ", transDate=" + transDate +
                ", inStId='" + inStId + '\'' +
                ", incomeId=" + incomeId +
                '}';
    }
} 