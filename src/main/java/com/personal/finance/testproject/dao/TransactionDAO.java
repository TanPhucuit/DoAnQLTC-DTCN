package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Transaction;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Date;

public interface TransactionDAO {
    void insert(Transaction transaction);
    Transaction findById(int transactionId, int userId);
    List<Transaction> findByUserId(int userId);
    List<Transaction> findByTypeId(int userId, String typeId);
    List<Transaction> findByLoanId(int userId, int loanId);
    List<Transaction> findByInStId(int userId, String inStId);
    List<Transaction> findByIncomeId(int userId, int incomeId);
    List<Transaction> findBySaveId(int userId, int saveId);
    List<Transaction> findByOverPayFeeId(int userId, int overPayFeeId);
    List<Transaction> findByAmountRange(int userId, BigDecimal minAmount, BigDecimal maxAmount);
    List<Transaction> findByDateRange(int userId, Date startDate, Date endDate);
    List<Transaction> findBySourceType(int userId, String sourceType);
    List<Transaction> findAll();
    void update(Transaction transaction);
    void delete(int transId, int userId);
} 