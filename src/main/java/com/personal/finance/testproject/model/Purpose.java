package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Purpose {
    private int purposeId;
    private int userId;
    private String purposeName;
    private BigDecimal estimateAmount;
    private String description;
    private boolean purposeState;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    public Purpose() {}

    public Purpose(int purposeId, int userId, String purposeName, BigDecimal estimateAmount, 
                  String description, boolean purposeState) {
        this.purposeId = purposeId;
        this.userId = userId;
        this.purposeName = purposeName;
        this.estimateAmount = estimateAmount;
        this.description = description;
        this.purposeState = purposeState;
    }

    public int getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(int purposeId) {
        if (purposeId <= 0) {
            throw new IllegalArgumentException("Purpose ID must be positive");
        }
        this.purposeId = purposeId;
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

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        if (purposeName == null || purposeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Purpose name cannot be null or empty");
        }
        if (purposeName.length() > 100) {
            throw new IllegalArgumentException("Purpose name cannot exceed 100 characters");
        }
        this.purposeName = purposeName;
    }

    public BigDecimal getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(BigDecimal estimateAmount) {
        if (estimateAmount == null || estimateAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Estimate amount cannot be negative");
        }
        this.estimateAmount = estimateAmount;
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

    public boolean isPurposeState() {
        return purposeState;
    }

    public void setPurposeState(boolean purposeState) {
        this.purposeState = purposeState;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        if (targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Target amount must be positive");
        }
        this.targetAmount = targetAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        if (currentAmount == null || currentAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Current amount cannot be null or negative");
        }
        if (currentAmount.compareTo(targetAmount) > 0) {
            throw new IllegalArgumentException("Current amount cannot exceed target amount");
        }
        this.currentAmount = currentAmount;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }
        if (startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the future");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        this.endDate = endDate;
    }

    public String getName() {
        return getPurposeName();
    }

    public void setName(String name) {
        setPurposeName(name);
    }

    public BigDecimal getTarget() {
        return getTargetAmount();
    }

    public void setTarget(BigDecimal target) {
        setTargetAmount(target);
    }

    public BigDecimal getCurrent() {
        return getCurrentAmount();
    }

    public void setCurrent(BigDecimal current) {
        setCurrentAmount(current);
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "Purpose{" +
                "purposeId=" + purposeId +
                ", userId=" + userId +
                ", purposeName='" + purposeName + '\'' +
                ", targetAmount=" + targetAmount +
                ", currentAmount=" + currentAmount +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", description='" + description + '\'' +
                '}';
    }
} 