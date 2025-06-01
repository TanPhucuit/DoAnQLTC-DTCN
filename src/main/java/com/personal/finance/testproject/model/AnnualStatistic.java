package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AnnualStatistic {
    private int userId;
    private String srYear;
    private BigDecimal averagePerDay;
    private BigDecimal averagePerWeek;
    private BigDecimal cumulativePnl;
    private LocalDate upDate;

    public AnnualStatistic() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSrYear() { return srYear; }
    public void setSrYear(String srYear) { this.srYear = srYear; }

    public BigDecimal getAveragePerDay() { return averagePerDay; }
    public void setAveragePerDay(BigDecimal averagePerDay) { this.averagePerDay = averagePerDay; }

    public BigDecimal getAveragePerWeek() { return averagePerWeek; }
    public void setAveragePerWeek(BigDecimal averagePerWeek) { this.averagePerWeek = averagePerWeek; }

    public BigDecimal getCumulativePnl() { return cumulativePnl; }
    public void setCumulativePnl(BigDecimal cumulativePnl) { this.cumulativePnl = cumulativePnl; }

    public LocalDate getUpDate() { return upDate; }
    public void setUpDate(LocalDate upDate) { this.upDate = upDate; }

    @Override
    public String toString() {
        return "AnnualStatistic{" +
                "userId=" + userId +
                ", srYear='" + srYear + '\'' +
                ", averagePerDay=" + averagePerDay +
                ", averagePerWeek=" + averagePerWeek +
                ", cumulativePnl=" + cumulativePnl +
                ", upDate=" + upDate +
                '}';
    }
} 