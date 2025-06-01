package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class BankTransfer {
    private int bankTransferId;
    private String sourceBankAccountNumber;
    private String targetBankAccountNumber;
    private BigDecimal transferAmount;
    private Date transferDate;

    // Constructors
    public BankTransfer() {}

    public BankTransfer(int bankTransferId, String sourceBankAccountNumber, String targetBankAccountNumber, BigDecimal transferAmount, Date transferDate) {
        this.bankTransferId = bankTransferId;
        this.sourceBankAccountNumber = sourceBankAccountNumber;
        this.targetBankAccountNumber = targetBankAccountNumber;
        this.transferAmount = transferAmount;
        this.transferDate = transferDate;
    }

    // Getters and Setters
    public int getBankTransferId() {
        return bankTransferId;
    }

    public void setBankTransferId(int bankTransferId) {
        if (bankTransferId <= 0) {
            throw new IllegalArgumentException("Bank transfer ID must be positive");
        }
        this.bankTransferId = bankTransferId;
    }

    public String getSourceBankAccountNumber() {
        return sourceBankAccountNumber;
    }

    public void setSourceBankAccountNumber(String sourceBankAccountNumber) {
        if (sourceBankAccountNumber == null || sourceBankAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Source bank account number cannot be null or empty");
        }
        this.sourceBankAccountNumber = sourceBankAccountNumber;
    }

    public String getTargetBankAccountNumber() {
        return targetBankAccountNumber;
    }

    public void setTargetBankAccountNumber(String targetBankAccountNumber) {
        if (targetBankAccountNumber == null || targetBankAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Target bank account number cannot be null or empty");
        }
        this.targetBankAccountNumber = targetBankAccountNumber;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        if (transferAmount == null || transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        this.transferAmount = transferAmount;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        if (transferDate == null) {
            throw new IllegalArgumentException("Transfer date cannot be null");
        }
        this.transferDate = transferDate;
    }

    @Override
    public String toString() {
        return "BankTransfer{" +
                "bankTransferId=" + bankTransferId +
                ", sourceBankAccountNumber='" + sourceBankAccountNumber + '\'' +
                ", targetBankAccountNumber='" + targetBankAccountNumber + '\'' +
                ", transferAmount=" + transferAmount +
                ", transferDate=" + transferDate +
                '}';
    }
} 