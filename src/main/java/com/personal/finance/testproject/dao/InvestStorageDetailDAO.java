package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.InvestStorageDetail;
import java.util.List;
import java.math.BigDecimal;

public interface InvestStorageDetailDAO {
    // Create
    void insert(InvestStorageDetail detail);
    
    // Read
    InvestStorageDetail findById(String inStId);
    List<InvestStorageDetail> findByRiskLevel(int riskLevel);
    List<InvestStorageDetail> findAll();
    
    // Update
    void update(InvestStorageDetail detail);
    void updateCurPrice(String inStId, BigDecimal curPrice);
    void updateZScore(String inStId, BigDecimal zScore);
    void updateStandardDeviation(String inStId, BigDecimal standardDeviation);
    
    // Delete
    void delete(String inStId);
    
    // Additional methods
    List<InvestStorageDetail> getDetailsByRiskRange(int minRisk, int maxRisk);
    BigDecimal getAverageCurPriceByRiskLevel(int riskLevel);
    BigDecimal getMaxStandardDeviation();
    BigDecimal getMinStandardDeviation();
} 