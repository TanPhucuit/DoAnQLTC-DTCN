package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class InvestProperty {
    private String inStId;
    private int userId;
    private BigDecimal numUnit;
    private BigDecimal buyPrice;
    private BigDecimal esProfit;
    private Date upDate;
    private String unit; // lấy từ INVEST_STORAGE_DETAIL
    private BigDecimal curProperty;
    private BigDecimal curPrice;
    private String description;

    public InvestProperty() {
    }

    public InvestProperty(int userId, String investPropertyId, String investPropertyName, String ipMonth, BigDecimal investPropertyAmount, Date upDate) {
        this.userId = userId;
        this.inStId = investPropertyId;
        this.numUnit = investPropertyAmount;
        this.buyPrice = investPropertyAmount;
        this.esProfit = investPropertyAmount;
        this.upDate = upDate;
    }

    public String getInStId() {
        return inStId;
    }

    public void setInStId(String inStId) {
        if (inStId == null || inStId.trim().isEmpty()) {
            throw new IllegalArgumentException("Investment property ID cannot be null or empty");
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
        if (buyPrice == null || buyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Buy price must be positive");
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty");
        }
        this.unit = unit;
    }

    public String getInvestPropertyName() {
        return inStId;
    }

    public void setInvestPropertyName(String investPropertyName) {
        this.inStId = investPropertyName;
    }

    public String getIpMonth() {
        return inStId;
    }

    public void setIpMonth(String ipMonth) {
        this.inStId = ipMonth;
    }

    public String getPrMonth() {
        return inStId;
    }

    public void setPrMonth(String prMonth) {
        this.inStId = prMonth;
    }

    public BigDecimal getInvestPropertyAmount() {
        return numUnit;
    }

    public void setInvestPropertyAmount(BigDecimal investPropertyAmount) {
        this.numUnit = investPropertyAmount;
    }

    public BigDecimal getAmount() {
        return numUnit;
    }

    public void setAmount(BigDecimal amount) {
        this.numUnit = amount;
    }

    public void setPropertyId(String propertyId) {
        this.inStId = propertyId;
    }

    public void setPropertyName(String propertyName) {
        this.inStId = propertyName;
    }

    public void setCurProperty(BigDecimal curProperty) {
        if (curProperty == null || curProperty.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current property value cannot be null or negative");
        }
        this.curProperty = curProperty;
    }

    public BigDecimal getCurProperty() {
        return curProperty;
    }

    public void setCurPrice(BigDecimal curPrice) {
        if (curPrice == null || curPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Current price must be positive");
        }
        this.curPrice = curPrice;
    }

    public BigDecimal getCurPrice() {
        return curPrice;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Description cannot exceed 500 characters");
        }
        this.description = description;
    }

    public String getInvestPropertyId() {
        return getInStId();
    }

    public void setInvestPropertyId(String investPropertyId) {
        setInStId(investPropertyId);
    }

    public BigDecimal getCurrentProperty() {
        return getCurProperty();
    }

    public void setCurrentProperty(BigDecimal currentProperty) {
        setCurProperty(currentProperty);
    }

    public BigDecimal getCurrentPrice() {
        return getCurPrice();
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        setCurPrice(currentPrice);
    }

    public BigDecimal getEstimatedProfit() {
        return getEsProfit();
    }

    public void setEstimatedProfit(BigDecimal estimatedProfit) {
        setEsProfit(estimatedProfit);
    }

    @Override
    public String toString() {
        return "InvestProperty{" +
                "inStId='" + inStId + '\'' +
                ", userId=" + userId +
                ", numUnit=" + numUnit +
                ", buyPrice=" + buyPrice +
                ", curPrice=" + curPrice +
                ", curProperty=" + curProperty +
                ", esProfit=" + esProfit +
                ", description='" + description + '\'' +
                '}';
    }
} 