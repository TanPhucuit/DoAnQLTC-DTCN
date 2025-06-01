package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Spend {
    private int userId;
    private String spendId;
    private String spMonth;
    private String spendName;
    private BigDecimal spendAmount;
    private Date upDate;
    private LocalDate spendDate;
    private String description;

    public Spend() {
    }

    public Spend(int userId, String spendId, String spMonth, String spendName, 
                BigDecimal spendAmount, Date upDate) {
        this.userId = userId;
        this.spendId = spendId;
        this.spMonth = spMonth;
        this.spendName = spendName;
        this.spendAmount = spendAmount;
        this.upDate = upDate;
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

    public String getSpendId() {
        return spendId;
    }

    public void setSpendId(String spendId) {
        if (spendId == null || spendId.trim().isEmpty()) {
            throw new IllegalArgumentException("Spend ID cannot be null or empty");
        }
        this.spendId = spendId;
    }

    public String getSpMonth() {
        return spMonth;
    }

    public void setSpMonth(String spMonth) {
        if (spMonth == null || !spMonth.matches("^([1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        this.spMonth = spMonth;
    }

    public String getSpendName() {
        return spendName;
    }

    public void setSpendName(String spendName) {
        if (spendName == null || spendName.trim().isEmpty()) {
            throw new IllegalArgumentException("Spend name cannot be null or empty");
        }
        if (spendName.length() > 100) {
            throw new IllegalArgumentException("Spend name cannot exceed 100 characters");
        }
        this.spendName = spendName;
    }

    public BigDecimal getSpendAmount() {
        return spendAmount;
    }

    public void setSpendAmount(BigDecimal spendAmount) {
        if (spendAmount == null || spendAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Spend amount must be positive");
        }
        this.spendAmount = spendAmount;
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

    public LocalDate getSpendDate() {
        return spendDate;
    }

    public void setSpendDate(LocalDate spendDate) {
        if (spendDate == null) {
            throw new IllegalArgumentException("Spend date cannot be null");
        }
        if (spendDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Spend date cannot be in the future");
        }
        this.spendDate = spendDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Description cannot exceed 500 characters");
        }
        this.description = description;
    }

    // Alias methods for compatibility
    public String getName() {
        return getSpendName();
    }

    public void setName(String name) {
        setSpendName(name);
    }

    public BigDecimal getAmount() {
        return getSpendAmount();
    }

    public void setAmount(BigDecimal amount) {
        setSpendAmount(amount);
    }

    public LocalDate getDate() {
        return getSpendDate();
    }

    public void setDate(LocalDate date) {
        setSpendDate(date);
    }

    @Override
    public String toString() {
        return "Spend{" +
                "spendId=" + spendId +
                ", userId=" + userId +
                ", spendName='" + spendName + '\'' +
                ", spendAmount=" + spendAmount +
                ", spendDate=" + spendDate +
                ", description='" + description + '\'' +
                '}';
    }
} 