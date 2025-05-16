package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.AccountBalance;
import java.util.List;

public interface AccountBalanceDAO {
    // Create
    void insert(AccountBalance accountBalance);
    
    // Read
    AccountBalance findById(String accountBalanceId, String month);
    List<AccountBalance> findByUserId(int userId);
    List<AccountBalance> findByMonth(String month);
    List<AccountBalance> findAll();
    AccountBalance findByUserIdAndMonth(int userId, String abMonth);
    
    // Update
    void update(AccountBalance accountBalance);
    void updateBalance(String accountBalanceId, String month, double amount);
    

 
} 