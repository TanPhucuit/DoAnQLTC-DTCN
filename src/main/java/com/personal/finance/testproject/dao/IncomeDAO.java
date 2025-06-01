package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Income;
import java.util.List;
import java.math.BigDecimal;

public interface IncomeDAO {
    // Create
    void insert(Income income);
    
    // Read
    Income findById(int incomeId);
    List<Income> findByUserId(int userId);
    List<Income> findByMonth(int userId, String month);
    List<Income> findAll();
    
    // Update
    void update(Income income);
    void updateSalary(int incomeId, BigDecimal salary);
    void updateAllowance(int incomeId, BigDecimal allowance);
    void updateRemainIncome(int incomeId, int userId, BigDecimal amount);
    
    // Delete
    void delete(int incomeId);
    
    // Additional methods
    BigDecimal getTotalSalaryByMonth(int userId, int month);
    BigDecimal getTotalAllowanceByMonth(int userId, int month);
    BigDecimal getTotalIncomeByMonth(int userId, int month);
} 