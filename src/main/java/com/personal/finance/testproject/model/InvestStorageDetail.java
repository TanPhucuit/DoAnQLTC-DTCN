package com.personal.finance.testproject.model;

import java.math.BigDecimal;

public class InvestStorageDetail {
    private String inStId;
    private int riskLevel;
    private BigDecimal curPrice;
    private BigDecimal zScore;
    private BigDecimal standardDeviation;
    private String unit;

    // Constructors
    public InvestStorageDetail() {}

    public InvestStorageDetail(String inStId, int riskLevel, BigDecimal curPrice,
                             BigDecimal zScore, BigDecimal standardDeviation, String unit) {
        this.inStId = inStId;
        this.riskLevel = riskLevel;
        this.curPrice = curPrice;
        this.zScore = zScore;
        this.standardDeviation = standardDeviation;
        this.unit = unit;
    }

    // Getters and Setters
    public String getInStId() {
        return inStId;
    }

    public void setInStId(String inStId) {
        if (inStId == null || inStId.trim().isEmpty()) {
            throw new IllegalArgumentException("Investment storage ID cannot be null or empty");
        }
        this.inStId = inStId;
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
        if (curPrice == null || curPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current price cannot be null or negative");
        }
        this.curPrice = curPrice;
    }

    public BigDecimal getZScore() {
        return zScore;
    }

    public void setZScore(BigDecimal zScore) {
        if (zScore == null) {
            throw new IllegalArgumentException("Z-score cannot be null");
        }
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
        return "InvestStorageDetail{" +
                "inStId='" + inStId + '\'' +
                ", riskLevel=" + riskLevel +
                ", curPrice=" + curPrice +
                ", zScore=" + zScore +
                ", standardDeviation=" + standardDeviation +
                ", unit='" + unit + '\'' +
                '}';
    }
} 