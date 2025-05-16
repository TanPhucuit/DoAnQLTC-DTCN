package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Income;
import java.util.List;
import java.math.BigDecimal;

public interface IncomeDAO {
    // Create
    void insert(Income income);
    
    // Read
    Income findById(int incomeId, int userId);
    List<Income> findByUserId(int userId);
    List<Income> findByMonth(int userId, String month);
    List<Income> findByYear(int userId, String year);
    List<Income> findAll();
    
    // Update
    void update(Income income);
    void updateRemainIncome(int incomeId, int userId, BigDecimal amount);
    
    // Delete
} 