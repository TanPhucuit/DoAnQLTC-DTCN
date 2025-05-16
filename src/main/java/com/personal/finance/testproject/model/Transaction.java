package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int transId;
    private String typeId;
    private int userId;
    private BigDecimal transAmount;
    private LocalDate transDate;
    private BigDecimal soldNumUnit;
    private BigDecimal soldProfit;
    private Integer loanId;
    private String inStId;
    private Integer incomeId;
    private Integer saveId;
    private Integer overPayFeeId;

    public Transaction() {}

    public Transaction(String typeId, int userId, BigDecimal transAmount, LocalDate transDate) {
        this.typeId = typeId;
        this.userId = userId;
        this.transAmount = transAmount;
        this.transDate = transDate;
    }

    // Getters and Setters
    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        if (typeId == null || typeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Type ID cannot be null or empty");
        }
        if (!typeId.matches("^(INCOME|EXPENSE|SAVE|INVEST|LOAN|PROPERTY)$")) {
            throw new IllegalArgumentException("Invalid transaction type");
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
        if (transAmount == null || transAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        this.transAmount = transAmount;
    }

    public LocalDate getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDate transDate) {
        if (transDate == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (transDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }
        this.transDate = transDate;
    }

    public BigDecimal getSoldNumUnit() {
        return soldNumUnit;
    }

    public void setSoldNumUnit(BigDecimal soldNumUnit) {
        if (soldNumUnit != null && soldNumUnit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Sold number of units cannot be negative");
        }
        this.soldNumUnit = soldNumUnit;
    }

    public BigDecimal getSoldProfit() {
        return soldProfit;
    }

    public void setSoldProfit(BigDecimal soldProfit) {
        this.soldProfit = soldProfit;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        if (loanId != null && loanId <= 0) {
            throw new IllegalArgumentException("Loan ID must be positive");
        }
        this.loanId = loanId;
    }

    public String getInStId() {
        return inStId;
    }

    public void setInStId(String inStId) {
        if (inStId != null && inStId.trim().isEmpty()) {
            throw new IllegalArgumentException("Investment storage ID cannot be empty");
        }
        this.inStId = inStId;
    }

    public Integer getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(Integer incomeId) {
        if (incomeId != null && incomeId <= 0) {
            throw new IllegalArgumentException("Income ID must be positive");
        }
        this.incomeId = incomeId;
    }

    public Integer getSaveId() {
        return saveId;
    }

    public void setSaveId(Integer saveId) {
        if (saveId != null && saveId <= 0) {
            throw new IllegalArgumentException("Save ID must be positive");
        }
        this.saveId = saveId;
    }

    public Integer getOverPayFeeId() {
        return overPayFeeId;
    }

    public void setOverPayFeeId(Integer overPayFeeId) {
        if (overPayFeeId != null && overPayFeeId <= 0) {
            throw new IllegalArgumentException("Over pay fee ID must be positive");
        }
        this.overPayFeeId = overPayFeeId;
    }

    // Alias methods for compatibility
    public BigDecimal getAmount() {
        return getTransAmount();
    }

    public void setAmount(BigDecimal amount) {
        setTransAmount(amount);
    }

    public LocalDate getDate() {
        return getTransDate();
    }

    public void setDate(LocalDate date) {
        setTransDate(date);
    }

    public String getType() {
        return getTypeId();
    }

    public void setType(String type) {
        setTypeId(type);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transId=" + transId +
                ", typeId='" + typeId + '\'' +
                ", userId=" + userId +
                ", transAmount=" + transAmount +
                ", transDate=" + transDate +
                ", soldNumUnit=" + soldNumUnit +
                ", soldProfit=" + soldProfit +
                ", loanId=" + loanId +
                ", inStId='" + inStId + '\'' +
                ", incomeId=" + incomeId +
                ", saveId=" + saveId +
                ", overPayFeeId=" + overPayFeeId +
                '}';
    }
} 