package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.AccountBalanceDAO;
import com.personal.finance.testproject.model.AccountBalance;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountBalanceService {
    private AccountBalanceDAO accountBalanceDAO;

    public AccountBalanceService(AccountBalanceDAO accountBalanceDAO) {
        this.accountBalanceDAO = accountBalanceDAO;
    }

    public void insert(AccountBalance accountBalance) {
        validateAccountBalance(accountBalance);
        accountBalanceDAO.insert(accountBalance);
    }

    public AccountBalance findById(String accountBalanceId, String month) {
        validateIdAndMonth(accountBalanceId, month);
        return accountBalanceDAO.findById(accountBalanceId, month);
    }

    public List<AccountBalance> findByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return accountBalanceDAO.findByUserId(userId);
    }

    public List<AccountBalance> findByMonth(String month) {
        validateMonth(month);
        return accountBalanceDAO.findByMonth(month);
    }

    public List<AccountBalance> findAll() {
        return accountBalanceDAO.findAll();
    }

    public void update(AccountBalance accountBalance) {
        validateAccountBalance(accountBalance);
        accountBalanceDAO.update(accountBalance);
    }

    public void updateBalance(String accountBalanceId, String month, double amount) {
        validateIdAndMonth(accountBalanceId, month);
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        accountBalanceDAO.updateBalance(accountBalanceId, month, amount);
    }

    private void validateAccountBalance(AccountBalance accountBalance) {
        if (accountBalance == null) {
            throw new IllegalArgumentException("AccountBalance cannot be null");
        }
        if (accountBalance.getUserId() <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (accountBalance.getAccountBalanceId() == null || accountBalance.getAccountBalanceId().trim().isEmpty()) {
            throw new IllegalArgumentException("AccountBalanceID cannot be null or empty");
        }
        validateMonth(accountBalance.getAbMonth());
        if (accountBalance.getUpDate() == null) {
            throw new IllegalArgumentException("UpDate cannot be null");
        }
        validateAmounts(accountBalance);
    }

    private void validateAmounts(AccountBalance accountBalance) {
        if (accountBalance.getTotalRemainIncome() != null && accountBalance.getTotalRemainIncome().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total remain income cannot be negative");
        }
        if (accountBalance.getTotalRemainSave() != null && accountBalance.getTotalRemainSave().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total remain save cannot be negative");
        }
        if (accountBalance.getTotalLoanRemain() != null && accountBalance.getTotalLoanRemain().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total loan remain cannot be negative");
        }
        if (accountBalance.getTotalSpend() != null && accountBalance.getTotalSpend().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total spend cannot be negative");
        }
        if (accountBalance.getTotalInvest() != null && accountBalance.getTotalInvest().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total invest cannot be negative");
        }
        if (accountBalance.getTotalInvestProperty() != null && accountBalance.getTotalInvestProperty().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total invest property cannot be negative");
        }
        if (accountBalance.getBalance() != null && accountBalance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }

    private void validateIdAndMonth(String accountBalanceId, String month) {
        if (accountBalanceId == null || accountBalanceId.trim().isEmpty()) {
            throw new IllegalArgumentException("AccountBalanceID cannot be null or empty");
        }
        validateMonth(month);
    }

    private void validateMonth(String month) {
        if (month == null || !month.matches("^([1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Invalid month value. Must be between 1 and 12");
        }
    }
} 