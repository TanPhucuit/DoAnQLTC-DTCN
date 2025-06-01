package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.BankTransfer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BankTransferDAO {
    // Create
    void insert(BankTransfer bankTransfer);
    
    // Read
    BankTransfer findById(int transferId);
    List<BankTransfer> findBySourceAccount(String accountNumber);
    List<BankTransfer> findByTargetAccount(String accountNumber);
    List<BankTransfer> findByDateRange(Date startDate, Date endDate);
    List<BankTransfer> findAll();
    
    // Update
    void update(BankTransfer bankTransfer);
    
    // Delete
  
    void transfer(int userId, String fromAccount, String toAccount, BigDecimal amount);
    List<BankTransfer> findByUserId(int userId);
} 