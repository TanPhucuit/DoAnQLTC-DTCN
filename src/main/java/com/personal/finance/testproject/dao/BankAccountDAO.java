package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.BankAccount;
import java.math.BigDecimal;
import java.util.List;

public interface BankAccountDAO {
    // Create
    void insert(BankAccount bankAccount);
    
    // Read
    BankAccount findById(String bankAccountId);
    List<BankAccount> findByUserId(int userId);
    List<BankAccount> findByBankName(String bankName);
    List<BankAccount> findAll();
    
    // Update
    void update(BankAccount bankAccount);
    void updateBalance(String bankAccountId, BigDecimal amount);
    

  
} 