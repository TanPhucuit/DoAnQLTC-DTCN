package com.personal.finance.testproject.model;

import java.math.BigDecimal;

public class AllocationRatio {
    private String investorType;
    private BigDecimal lv1Rate;
    private BigDecimal lv2Rate;
    private BigDecimal lv3Rate;

    // Constructors
    public AllocationRatio() {}

    public AllocationRatio(String investorType, BigDecimal lv1Rate, BigDecimal lv2Rate, BigDecimal lv3Rate) {
        this.investorType = investorType;
        this.lv1Rate = lv1Rate;
        this.lv2Rate = lv2Rate;
        this.lv3Rate = lv3Rate;
    }

    // Getters and Setters
    public String getInvestorType() {
        return investorType;
    }

    public void setInvestorType(String investorType) {
        if (investorType == null || investorType.trim().isEmpty()) {
            throw new IllegalArgumentException("Investor type cannot be null or empty");
        }
        this.investorType = investorType;
    }

    public BigDecimal getLv1Rate() {
        return lv1Rate;
    }

    public void setLv1Rate(BigDecimal lv1Rate) {
        if (lv1Rate == null || lv1Rate.compareTo(BigDecimal.ZERO) < 0 || lv1Rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Level 1 rate must be between 0 and 1");
        }
        this.lv1Rate = lv1Rate;
    }

    public BigDecimal getLv2Rate() {
        return lv2Rate;
    }

    public void setLv2Rate(BigDecimal lv2Rate) {
        if (lv2Rate == null || lv2Rate.compareTo(BigDecimal.ZERO) < 0 || lv2Rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Level 2 rate must be between 0 and 1");
        }
        this.lv2Rate = lv2Rate;
    }

    public BigDecimal getLv3Rate() {
        return lv3Rate;
    }

    public void setLv3Rate(BigDecimal lv3Rate) {
        if (lv3Rate == null || lv3Rate.compareTo(BigDecimal.ZERO) < 0 || lv3Rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Level 3 rate must be between 0 and 1");
        }
        this.lv3Rate = lv3Rate;
    }

    @Override
    public String toString() {
        return "AllocationRatio{" +
                "investorType='" + investorType + '\'' +
                ", lv1Rate=" + lv1Rate +
                ", lv2Rate=" + lv2Rate +
                ", lv3Rate=" + lv3Rate +
                '}';
    }
} 