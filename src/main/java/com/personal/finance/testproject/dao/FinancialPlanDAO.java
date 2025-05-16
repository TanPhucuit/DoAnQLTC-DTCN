package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.FinancialPlan;
import java.util.List;

public interface FinancialPlanDAO {
    // Create
    void insert(FinancialPlan financialPlan);
    
    // Read
    FinancialPlan findByUserId(int userId);
    List<FinancialPlan> findAll();
    List<FinancialPlan> findByInvestorType(String investorType);
    
    // Update
    void update(FinancialPlan financialPlan);
    
    // Delete
  
    
    // Additional methods
    void updateCurProperty(int userId, double amount);
    void updateCurInvestProperty(int userId, double amount);
    void updateCurEsProfit(int userId, double amount);
    void updateCurCumulativePnl(int userId, double amount);
    void updateWarningLossRate(int userId, double rate);
} 