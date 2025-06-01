package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class Saving {
    private Integer saveId;
    private Integer userId;
    private Integer purposeId;
    private String bankAccountNumber;
    private String saveName;
    private BigDecimal saveAmount;
    private BigDecimal remainSave;
    private Date upDate;
    private String description;

    // Getters
    public Integer getSaveId() {
        return saveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getPurposeId() {
        return purposeId;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getSaveName() {
        return saveName;
    }

    public BigDecimal getSaveAmount() {
        return saveAmount;
    }

    public BigDecimal getRemainSave() {
        return remainSave;
    }

    public Date getUpDate() {
        return upDate;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setSaveId(Integer saveId) {
        if (saveId == null || saveId <= 0) {
            throw new IllegalArgumentException("Save ID must be positive");
        }
        this.saveId = saveId;
    }

    public void setUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.userId = userId;
    }

    public void setPurposeId(Integer purposeId) {
        if (purposeId == null || purposeId <= 0) {
            throw new IllegalArgumentException("Purpose ID must be positive");
        }
        this.purposeId = purposeId;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        if (bankAccountNumber == null || bankAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number cannot be null or empty");
        }
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setSaveName(String saveName) {
        if (saveName == null || saveName.trim().isEmpty()) {
            throw new IllegalArgumentException("Save name cannot be null or empty");
        }
        this.saveName = saveName;
    }

    public void setSaveAmount(BigDecimal saveAmount) {
        if (saveAmount == null || saveAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Save amount cannot be null or negative");
        }
        this.saveAmount = saveAmount;
    }

    public void setRemainSave(BigDecimal remainSave) {
        if (remainSave == null || remainSave.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Remain save cannot be null or negative");
        }
        if (this.saveAmount != null && remainSave.compareTo(this.saveAmount) > 0) {
            throw new IllegalArgumentException("Remain save cannot be greater than save amount");
        }
        this.remainSave = remainSave;
    }

    public void setUpDate(Date upDate) {
        if (upDate == null) {
            throw new IllegalArgumentException("Update date cannot be null");
        }
        this.upDate = upDate;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Description cannot exceed 500 characters");
        }
        this.description = description;
    }

    // Alias methods for compatibility
    public BigDecimal getAmount() {
        return getSaveAmount();
    }

    public void setAmount(BigDecimal amount) {
        setSaveAmount(amount);
    }

    public String getSaveMonth() {
        return getSaveName();
    }

    public void setSaveMonth(String saveMonth) {
        setSaveName(saveMonth);
    }

    @Override
    public String toString() {
        return "Saving{" +
                "saveId=" + saveId +
                ", userId=" + userId +
                ", purposeId=" + purposeId +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", saveName='" + saveName + '\'' +
                ", saveAmount=" + saveAmount +
                ", remainSave=" + remainSave +
                ", upDate=" + upDate +
                ", description='" + description + '\'' +
                '}';
    }
} 