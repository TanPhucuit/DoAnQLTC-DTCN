package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class Income {
    private int incomeId;
    private int userId;
    private Date incomeDate;
    private BigDecimal salary;
    private BigDecimal allowance;
    private String description;
    private String incomeName;
    private BigDecimal incomeAmount;
    private String icMonth;
    private BigDecimal remainIncome;
    private Date upDate;

    // Constructors
    public Income() {}

    public Income(int incomeId, int userId, String icMonth, String incomeName, BigDecimal incomeAmount, BigDecimal remainIncome) {
        this.incomeId = incomeId;
        this.userId = userId;
        setIcMonth(icMonth);
        this.incomeName = incomeName;
        setIncomeAmount(incomeAmount);
        setRemainIncome(remainIncome);
    }

    public Income(int userId, Date incomeDate, BigDecimal salary, BigDecimal allowance, String description) {
        this.userId = userId;
        this.incomeDate = incomeDate;
        this.salary = salary;
        this.allowance = allowance;
        this.description = description;
    }

    // Getters and Setters
    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
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

    public Date getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(Date incomeDate) {
        if (incomeDate == null) {
            throw new IllegalArgumentException("Income date cannot be null");
        }
        this.incomeDate = incomeDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        if (salary == null || salary.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salary cannot be null or negative");
        }
        this.salary = salary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        if (allowance == null || allowance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Allowance cannot be null or negative");
        }
        this.allowance = allowance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        if (incomeName == null || incomeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Income name cannot be null or empty");
        }
        this.incomeName = incomeName;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        if (incomeAmount == null || incomeAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income amount cannot be null or negative");
        }
        this.incomeAmount = incomeAmount;
    }

    public String getIcMonth() {
        return icMonth;
    }

    public void setIcMonth(String icMonth) {
        if (icMonth == null || !icMonth.matches("^([1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Invalid month value. Must be between 1 and 12");
        }
        this.icMonth = icMonth;
    }

    public BigDecimal getRemainIncome() {
        return remainIncome;
    }

    public void setRemainIncome(BigDecimal remainIncome) {
        if (remainIncome == null || remainIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Remain income cannot be null or negative");
        }
        if (this.incomeAmount != null && remainIncome.compareTo(this.incomeAmount) > 0) {
            throw new IllegalArgumentException("Remain income cannot be greater than income amount");
        }
        this.remainIncome = remainIncome;
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

    // Alias method for getIncomeAmount() to maintain compatibility
    public BigDecimal getAmount() {
        return getIncomeAmount();
    }

    // Alias method for getIcMonth() to maintain compatibility
    public String getInMonth() {
        return getIcMonth();
    }

    @Override
    public String toString() {
        return "Income{" +
                "incomeId=" + incomeId +
                ", userId=" + userId +
                ", icMonth='" + icMonth + '\'' +
                ", incomeName='" + incomeName + '\'' +
                ", incomeAmount=" + incomeAmount +
                ", remainIncome=" + remainIncome +
                ", upDate=" + upDate +
                '}';
    }
} 