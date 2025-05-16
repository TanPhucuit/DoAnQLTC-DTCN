package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.InvestStorage;
import java.util.List;
import java.math.BigDecimal;

public interface InvestStorageDAO {
    // Create
    void insert(InvestStorage investStorage);
    
    // Read
    InvestStorage findById(String inStId, int userId);
    List<InvestStorage> findByUserId(int userId);
    List<InvestStorage> findByRiskLevel(int riskLevel);
    List<InvestStorage> findAll();
    
    // Update
    void update(InvestStorage investStorage);
    void updateNumUnit(String inStId, int userId, BigDecimal numUnit);
    void updateBuyPrice(String inStId, int userId, BigDecimal buyPrice);
    void updateEsProfit(String inStId, int userId, BigDecimal esProfit);
    
    // Delete
    void delete(String inStId, int userId);
    
    // Additional methods
    BigDecimal getTotalInvestValue(int userId);
    BigDecimal getTotalEsProfit(int userId);
    List<InvestStorage> getInvestmentsByRiskLevel(int userId, int riskLevel);
    BigDecimal getTotalInvestValueByRiskLevel(int userId, int riskLevel);
} 