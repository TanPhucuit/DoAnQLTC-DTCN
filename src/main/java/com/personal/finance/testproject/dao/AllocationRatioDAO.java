package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.AllocationRatio;
import java.util.List;

public interface AllocationRatioDAO {
    // Create
    void insert(AllocationRatio allocationRatio);
    
    // Read
    AllocationRatio findByInvestorType(String investorType);
    List<AllocationRatio> findAll();
    
    // Update
    void update(AllocationRatio allocationRatio);
    
    // Delete

    
    // Additional methods
    void updateLv1Rate(String investorType, double rate);
    void updateLv2Rate(String investorType, double rate);
    void updateLv3Rate(String investorType, double rate);
} 