package com.personal.finance.testproject.service;

import com.personal.finance.testproject.model.Transaction;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionService {
    void addTransaction(Transaction transaction);
    Transaction getTransaction(int transId, int userId);
    List<Transaction> getTransactionsByUserId(int userId);
    List<Transaction> getTransactionsByType(int userId, String typeId);
    List<Transaction> getTransactionsByLoan(int userId, int loanId);
    List<Transaction> getTransactionsByInvestment(int userId, String inStId);
    List<Transaction> getTransactionsByIncome(int userId, int incomeId);
    List<Transaction> getTransactionsBySaving(int userId, int saveId);
    List<Transaction> getTransactionsByOverPayFee(int userId, int overPayFeeId);
    List<Transaction> getTransactionsByAmountRange(int userId, BigDecimal minAmount, BigDecimal maxAmount);
    List<Transaction> getTransactionsByDateRange(int userId, LocalDate startDate, LocalDate endDate);
    List<Transaction> getTransactionsBySourceType(int userId, String sourceType);
    List<Transaction> getAllTransactions();
    void updateTransaction(Transaction transaction);
} 