package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class BankAccount {
    private int bankAccountId;
    private String bankAccountNumber;
    private String bankName;
    private int userId;
    private BigDecimal balance;
    private String note;
    private LocalDate upDate;

    public BankAccount() {}

    public BankAccount(String bankAccountNumber, String bankName, int userId, BigDecimal balance, String note) {
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        this.userId = userId;
        this.balance = balance;
        this.note = note;
        this.upDate = LocalDate.now();
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        if (bankAccountNumber == null || bankAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number cannot be null or empty");
        }
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        if (bankName == null || bankName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name cannot be null or empty");
        }
        this.bankName = bankName;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Balance cannot be null");
        }
        this.balance = balance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getUpDate() {
        return upDate;
    }

    public void setUpDate(LocalDate upDate) {
        if (upDate == null) {
            throw new IllegalArgumentException("Update date cannot be null");
        }
        this.upDate = upDate;
    }

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", userId=" + userId +
                ", balance=" + balance +
                ", note='" + note + '\'' +
                ", upDate=" + upDate +
                '}';
    }

    public void setUpDate(Date upDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

 
} 