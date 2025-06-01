package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class Invest {
    private int userId;
    private String investId;
    private String investName;
    private String inMonth;
    private BigDecimal amount;
    private BigDecimal investAmount;
    private BigDecimal investProperty;
    private Date upDate;
    private int riskLevel;
    private BigDecimal currentPrice;
    private BigDecimal zScore;
    private BigDecimal standardDeviation;

    public Invest() {
    }

    public Invest(int userId, String investId, String investName, String inMonth, BigDecimal amount, BigDecimal investAmount, BigDecimal investProperty, Date upDate) {
        this.userId = userId;
        this.investId = investId;
        this.investName = investName;
        this.inMonth = inMonth;
        this.amount = amount;
        this.investAmount = investAmount;
        this.investProperty = investProperty;
        this.upDate = upDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public String getInMonth() {
        return inMonth;
    }

    public void setInMonth(String inMonth) {
        this.inMonth = inMonth;
    }

    public String getInvMonth() {
        return inMonth;
    }

    public void setInvMonth(String invMonth) {
        this.inMonth = invMonth;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInvestAmount() {
        return amount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.amount = investAmount;
    }

    public BigDecimal getInvestProperty() {
        return investProperty;
    }

    public void setInvestProperty(BigDecimal investProperty) {
        this.investProperty = investProperty;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setUpDate(Date upDate) {
        this.upDate = upDate;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public BigDecimal getZScore() {
        return zScore;
    }

    public void setZScore(BigDecimal zScore) {
        this.zScore = zScore;
    }

    public BigDecimal getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(BigDecimal standardDeviation) {
        this.standardDeviation = standardDeviation;
    }
} 