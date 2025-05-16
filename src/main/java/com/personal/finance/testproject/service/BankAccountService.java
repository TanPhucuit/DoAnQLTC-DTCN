package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.BankAccountDAO;
import com.personal.finance.testproject.dao.impl.BankAccountDAOImpl;
import com.personal.finance.testproject.model.BankAccount;
import java.sql.Connection;
import java.math.BigDecimal;
import java.util.List;

public class BankAccountService {
    private final BankAccountDAO bankAccountDAO;

    public BankAccountService(Connection connection) {
        this.bankAccountDAO = new BankAccountDAOImpl(connection);
    }

    public void addBankAccount(int userId, String bankName, String accountNumber, BigDecimal balance) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserId(userId);
        bankAccount.setBankName(bankName);
        bankAccount.setBankAccountNumber(accountNumber);
        bankAccount.setBalance(balance);
        bankAccountDAO.insert(bankAccount);
    }

    public void updateBankAccount(String bankAccountId, String bankName, String accountNumber, BigDecimal balance) {
        BankAccount bankAccount = bankAccountDAO.findById(bankAccountId);
        if (bankAccount != null) {
            bankAccount.setBankName(bankName);
            bankAccount.setBankAccountNumber(accountNumber);
            bankAccount.setBalance(balance);
            bankAccountDAO.update(bankAccount);
        }
    }

    public void updateBalance(String bankAccountId, BigDecimal amount) {
        bankAccountDAO.updateBalance(bankAccountId, amount);
    }



    public List<BankAccount> getAllBankAccounts(int userId) {
        return bankAccountDAO.findByUserId(userId);
    }

    public List<BankAccount> getBankAccountsByBankName(String bankName) {
        return bankAccountDAO.findByBankName(bankName);
    }

   
} 