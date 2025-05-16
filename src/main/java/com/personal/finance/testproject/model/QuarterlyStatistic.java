package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class QuarterlyStatistic {
    private int userId;
    private String srQuarter;
    private String srYear;
    private BigDecimal averagePerDay;
    private BigDecimal averagePerWeek;
    private BigDecimal cumulativePnl;
    private LocalDate upDate;

    public QuarterlyStatistic() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getSrQuarter() { return srQuarter; }
    public void setSrQuarter(String srQuarter) { this.srQuarter = srQuarter; }

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
        return "QuarterlyStatistic{" +
                "userId=" + userId +
                ", srQuarter='" + srQuarter + '\'' +
                ", srYear='" + srYear + '\'' +
                ", averagePerDay=" + averagePerDay +
                ", averagePerWeek=" + averagePerWeek +
                ", cumulativePnl=" + cumulativePnl +
                ", upDate=" + upDate +
                '}';
    }
} 