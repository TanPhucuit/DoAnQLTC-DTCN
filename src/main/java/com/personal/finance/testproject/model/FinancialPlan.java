package com.personal.finance.testproject.model;

import java.math.BigDecimal;

public class FinancialPlan {
    private int userId;
    private String investorType;
    private BigDecimal curInvestProperty;
    private BigDecimal curEsProfit;
    private BigDecimal curCumulativePnl;
    private BigDecimal warningLossRate;

    public FinancialPlan() {}

    public FinancialPlan(int userId, String investorType, BigDecimal curInvestProperty, 
                        BigDecimal curEsProfit, BigDecimal curCumulativePnl, BigDecimal warningLossRate) {
        this.userId = userId;
        this.investorType = investorType;
        this.curInvestProperty = curInvestProperty;
        this.curEsProfit = curEsProfit;
        this.curCumulativePnl = curCumulativePnl;
        this.warningLossRate = warningLossRate;
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

    public String getInvestorType() {
        return investorType;
    }

    public void setInvestorType(String investorType) {
        if (investorType == null || investorType.trim().isEmpty()) {
            throw new IllegalArgumentException("Investor type cannot be null or empty");
        }
        if (!investorType.matches("^(Conservative|Moderate|Aggressive)$")) {
            throw new IllegalArgumentException("Investor type must be Conservative, Moderate, or Aggressive");
        }
        this.investorType = investorType;
    }

    public BigDecimal getCurInvestProperty() {
        return curInvestProperty;
    }

    public void setCurInvestProperty(BigDecimal curInvestProperty) {
        if (curInvestProperty == null || curInvestProperty.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current investment property cannot be negative");
        }
        this.curInvestProperty = curInvestProperty;
    }

    public BigDecimal getCurEsProfit() {
        return curEsProfit;
    }

    public void setCurEsProfit(BigDecimal curEsProfit) {
        if (curEsProfit == null) {
            throw new IllegalArgumentException("Current estimated profit cannot be null");
        }
        this.curEsProfit = curEsProfit;
    }

    public BigDecimal getCurCumulativePnl() {
        return curCumulativePnl;
    }

    public void setCurCumulativePnl(BigDecimal curCumulativePnl) {
        if (curCumulativePnl == null) {
            throw new IllegalArgumentException("Current cumulative PnL cannot be null");
        }
        this.curCumulativePnl = curCumulativePnl;
    }

    public BigDecimal getWarningLossRate() {
        return warningLossRate;
    }

    public void setWarningLossRate(BigDecimal warningLossRate) {
        if (warningLossRate == null || warningLossRate.compareTo(BigDecimal.ZERO) < 0 || warningLossRate.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Warning loss rate must be between 0 and 100");
        }
        this.warningLossRate = warningLossRate;
    }

    @Override
    public String toString() {
        return "FinancialPlan{" +
                "userId=" + userId +
                ", investorType='" + investorType + '\'' +
                ", curInvestProperty=" + curInvestProperty +
                ", curEsProfit=" + curEsProfit +
                ", curCumulativePnl=" + curCumulativePnl +
                ", warningLossRate=" + warningLossRate +
                '}';
    }
} 