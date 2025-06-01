package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvestStorage {
    private String inStId;
    private int userId;
    private BigDecimal numUnit;
    private BigDecimal buyPrice;
    private BigDecimal esProfit;
    private Date upDate;
    
    // Thông tin từ INVEST_STORAGE_DETAIL
    private int riskLevel;
    private BigDecimal curPrice;
    private BigDecimal zScore;
    private BigDecimal standardDeviation;
    private String unit;

    public InvestStorage() {}

    public InvestStorage(String inStId, int userId, BigDecimal numUnit, BigDecimal buyPrice, 
                        BigDecimal esProfit, Date upDate, int riskLevel, BigDecimal curPrice,
                        BigDecimal zScore, BigDecimal standardDeviation, String unit) {
        this.inStId = inStId;
        this.userId = userId;
        this.numUnit = numUnit;
        this.buyPrice = buyPrice;
        this.esProfit = esProfit;
        this.upDate = upDate;
        this.riskLevel = riskLevel;
        this.curPrice = curPrice;
        this.zScore = zScore;
        this.standardDeviation = standardDeviation;
        this.unit = unit;
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

    public BigDecimal getNumUnit() {
        return numUnit;
    }

    public void setNumUnit(BigDecimal numUnit) {
        if (numUnit == null || numUnit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Number of units cannot be null or negative");
        }
        this.numUnit = numUnit;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        if (buyPrice == null || buyPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Buy price cannot be null or negative");
        }
        this.buyPrice = buyPrice;
    }

    public BigDecimal getEsProfit() {
        return esProfit;
    }

    public void setEsProfit(BigDecimal esProfit) {
        if (esProfit == null) {
            throw new IllegalArgumentException("Estimated profit cannot be null");
        }
        this.esProfit = esProfit;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setUpDate(Date upDate) {
        if (upDate == null) {
            throw new IllegalArgumentException("Update date cannot be null");
        }
        this.upDate = upDate;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(int riskLevel) {
        if (riskLevel < 1 || riskLevel > 4) {
            throw new IllegalArgumentException("Risk level must be between 1 and 4");
        }
        this.riskLevel = riskLevel;
    }

    public BigDecimal getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(BigDecimal curPrice) {
        if (curPrice == null || curPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Current price must be positive");
        }
        this.curPrice = curPrice;
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
        if (standardDeviation == null || standardDeviation.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Standard deviation cannot be null or negative");
        }
        this.standardDeviation = standardDeviation;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty");
        }
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "InvestStorage{" +
                "inStId='" + inStId + '\'' +
                ", userId=" + userId +
                ", numUnit=" + numUnit +
                ", buyPrice=" + buyPrice +
                ", esProfit=" + esProfit +
                ", upDate=" + upDate +
                ", riskLevel=" + riskLevel +
                ", curPrice=" + curPrice +
                ", zScore=" + zScore +
                ", standardDeviation=" + standardDeviation +
                '}';
    }
} 