package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class OverPayFee {
    private int overPayFeeId;
    private int loanId;
    private BigDecimal overPayAmount;
    private Date overPayDate;
    private String description;

    public OverPayFee() {}

    public OverPayFee(int overPayFeeId, int loanId, BigDecimal overPayAmount,
                     Date overPayDate, String description) {
        this.overPayFeeId = overPayFeeId;
        this.loanId = loanId;
        this.overPayAmount = overPayAmount;
        this.overPayDate = overPayDate;
        this.description = description;
    }

    public int getOverPayFeeId() {
        return overPayFeeId;
    }

    public void setOverPayFeeId(int overPayFeeId) {
        if (overPayFeeId <= 0) {
            throw new IllegalArgumentException("Over pay fee ID must be positive");
        }
        this.overPayFeeId = overPayFeeId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        if (loanId <= 0) {
            throw new IllegalArgumentException("Loan ID must be positive");
        }
        this.loanId = loanId;
    }

    public BigDecimal getOverPayAmount() {
        return overPayAmount;
    }

    public void setOverPayAmount(BigDecimal overPayAmount) {
        if (overPayAmount == null || overPayAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Over pay amount must be positive");
        }
        this.overPayAmount = overPayAmount;
    }

    public Date getOverPayDate() {
        return overPayDate;
    }

    public void setOverPayDate(Date overPayDate) {
        if (overPayDate == null) {
            throw new IllegalArgumentException("Over pay date cannot be null");
        }
        this.overPayDate = overPayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "OverPayFee{" +
                "overPayFeeId=" + overPayFeeId +
                ", loanId=" + loanId +
                ", overPayAmount=" + overPayAmount +
                ", overPayDate=" + overPayDate +
                ", description='" + description + '\'' +
                '}';
    }
} 