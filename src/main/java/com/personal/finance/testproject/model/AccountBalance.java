package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountBalance {
    private String accountBalanceId;
    private int userId;
    private String month;
    private BigDecimal totalRemainIncome;
    private BigDecimal totalRemainSave;
    private BigDecimal totalLoanRemain;
    private BigDecimal totalSpend;
    private BigDecimal totalInvest;
    private BigDecimal totalInvestProperty;
    private BigDecimal balance;
    private LocalDate upDate;

    public AccountBalance() {}

    public AccountBalance(int userId, String accountBalanceId, String month, 
                         BigDecimal totalRemainIncome, BigDecimal totalRemainSave,
                         BigDecimal totalLoanRemain, BigDecimal totalSpend,
                         BigDecimal totalInvest, BigDecimal totalInvestProperty,
                         BigDecimal balance, LocalDate upDate) {
        this.userId = userId;
        this.accountBalanceId = accountBalanceId;
        this.month = month;
        this.totalRemainIncome = totalRemainIncome;
        this.totalRemainSave = totalRemainSave;
        this.totalLoanRemain = totalLoanRemain;
        this.totalSpend = totalSpend;
        this.totalInvest = totalInvest;
        this.totalInvestProperty = totalInvestProperty;
        this.balance = balance;
        this.upDate = upDate;
    }

    public String getAccountBalanceId() {
        return accountBalanceId;
    }

    public void setAccountBalanceId(String accountBalanceId) {
        if (accountBalanceId == null || accountBalanceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account balance ID cannot be null or empty");
        }
        this.accountBalanceId = accountBalanceId;
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        if (month == null || !month.matches("^([1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }
        this.month = month;
    }

    public BigDecimal getTotalRemainIncome() {
        return totalRemainIncome;
    }

    public void setTotalRemainIncome(BigDecimal totalRemainIncome) {
        if (totalRemainIncome == null || totalRemainIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total remain income cannot be negative");
        }
        this.totalRemainIncome = totalRemainIncome;
    }

    public BigDecimal getTotalRemainSave() {
        return totalRemainSave;
    }

    public void setTotalRemainSave(BigDecimal totalRemainSave) {
        if (totalRemainSave == null || totalRemainSave.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total remain save cannot be negative");
        }
        this.totalRemainSave = totalRemainSave;
    }

    public BigDecimal getTotalLoanRemain() {
        return totalLoanRemain;
    }

    public void setTotalLoanRemain(BigDecimal totalLoanRemain) {
        if (totalLoanRemain == null || totalLoanRemain.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total loan remain cannot be negative");
        }
        this.totalLoanRemain = totalLoanRemain;
    }

    public BigDecimal getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(BigDecimal totalSpend) {
        if (totalSpend == null || totalSpend.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total spend cannot be negative");
        }
        this.totalSpend = totalSpend;
    }

    public BigDecimal getTotalInvest() {
        return totalInvest;
    }

    public void setTotalInvest(BigDecimal totalInvest) {
        if (totalInvest == null || totalInvest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total invest cannot be negative");
        }
        this.totalInvest = totalInvest;
    }

    public BigDecimal getTotalInvestProperty() {
        return totalInvestProperty;
    }

    public void setTotalInvestProperty(BigDecimal totalInvestProperty) {
        if (totalInvestProperty == null || totalInvestProperty.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total invest property cannot be negative");
        }
        this.totalInvestProperty = totalInvestProperty;
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

    public LocalDate getUpDate() {
        return upDate;
    }

    public void setUpDate(LocalDate upDate) {
        if (upDate == null) {
            throw new IllegalArgumentException("Update date cannot be null");
        }
        this.upDate = upDate;
    }

    @Override
    public String toString() {
        return "AccountBalance{" +
                "userId=" + userId +
                ", accountBalanceId='" + accountBalanceId + '\'' +
                ", month='" + month + '\'' +
                ", totalRemainIncome=" + totalRemainIncome +
                ", totalRemainSave=" + totalRemainSave +
                ", totalLoanRemain=" + totalLoanRemain +
                ", totalSpend=" + totalSpend +
                ", totalInvest=" + totalInvest +
                ", totalInvestProperty=" + totalInvestProperty +
                ", balance=" + balance +
                ", upDate=" + upDate +
                '}';
    }

    // Alias method for getMonth() to maintain compatibility
    public String getabmonth() {
        return getMonth();
    }

    // Alias method for getAccountBalanceId() to maintain compatibility
    public String getId() {
        return getAccountBalanceId();
    }

    // Alias method for setAccountBalanceId() to maintain compatibility
    public void setBankAccountId(String bankAccountId) {
        setAccountBalanceId(bankAccountId);
    }

    // Alias method for setAccountBalanceId() to maintain compatibility
    public void setAccountNumber(String accountNumber) {
        setAccountBalanceId(accountNumber);
    }

    // Alias method for setTotalInvestProperty() to maintain compatibility
    public void setcurproperty(BigDecimal curproperty) {
        setTotalInvestProperty(curproperty);
    }

    public String getAbMonth() {
        return month;
    }

    public void setAbMonth(String month) {
        this.month = month;
    }
} 