package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StatisticReport {
    private int userId;
    private String accountBalanceId;
    private String srQuarter;
    private String srMonth;
    private BigDecimal averagePerDay;
    private BigDecimal averagePerWeek;
    private BigDecimal cumulativePnl;
    private LocalDate upDate;

    public StatisticReport() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAccountBalanceId() { return accountBalanceId; }
    public void setAccountBalanceId(String accountBalanceId) { this.accountBalanceId = accountBalanceId; }

    public String getSrQuarter() { return srQuarter; }
    public void setSrQuarter(String srQuarter) {
        if (srQuarter != null && !srQuarter.matches("^[1-4]$")) {
            throw new IllegalArgumentException("Quarter must be between 1 and 4");
        }
        this.srQuarter = srQuarter;
    }

    public String getSrMonth() { return srMonth; }
    public void setSrMonth(String srMonth) {
        if (srMonth != null && !srMonth.matches("^(0?[1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        this.srMonth = srMonth;
    }

    public BigDecimal getAveragePerDay() { return averagePerDay; }
    public void setAveragePerDay(BigDecimal averagePerDay) {
        if (averagePerDay != null && averagePerDay.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Average per day cannot be negative");
        }
        this.averagePerDay = averagePerDay;
    }

    public BigDecimal getAveragePerWeek() { return averagePerWeek; }
    public void setAveragePerWeek(BigDecimal averagePerWeek) {
        if (averagePerWeek != null && averagePerWeek.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Average per week cannot be negative");
        }
        this.averagePerWeek = averagePerWeek;
    }

    public BigDecimal getCumulativePnl() { return cumulativePnl; }
    public void setCumulativePnl(BigDecimal cumulativePnl) {
        this.cumulativePnl = cumulativePnl != null ? cumulativePnl : BigDecimal.ZERO;
    }

    public LocalDate getUpDate() { return upDate; }
    public void setUpDate(LocalDate upDate) { this.upDate = upDate; }

    @Override
    public String toString() {
        return "StatisticReport{" +
                "userId=" + userId +
                ", accountBalanceId='" + accountBalanceId + '\'' +
                ", srQuarter='" + srQuarter + '\'' +
                ", srMonth='" + srMonth + '\'' +
                ", averagePerDay=" + averagePerDay +
                ", averagePerWeek=" + averagePerWeek +
                ", cumulativePnl=" + cumulativePnl +
                ", upDate=" + upDate +
                '}';
    }
} 