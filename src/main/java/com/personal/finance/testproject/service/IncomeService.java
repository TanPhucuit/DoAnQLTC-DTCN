package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.IncomeDAO;
import com.personal.finance.testproject.model.Income;
import java.util.List;
import java.math.BigDecimal;

public interface IncomeService {
    void addIncome(Income income);
    Income getIncome(int incomeId, int userId);
    List<Income> getIncomesByUserId(int userId);
    List<Income> getIncomesByMonth(int userId, String month);
    List<Income> getAllIncomes();
    void updateIncome(Income income);
    void updateRemainIncome(int incomeId, int userId, BigDecimal amount);
}

class IncomeServiceImpl implements IncomeService {
    private final IncomeDAO incomeDAO;

    public IncomeServiceImpl(IncomeDAO incomeDAO) {
        this.incomeDAO = incomeDAO;
    }

    @Override
    public void addIncome(Income income) {
        validateIncome(income);
        incomeDAO.insert(income);
    }

    @Override
    public Income getIncome(int incomeId, int userId) {
        if (incomeId <= 0) {
            throw new IllegalArgumentException("IncomeID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return incomeDAO.findById(incomeId);
    }

    @Override
    public List<Income> getIncomesByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return incomeDAO.findByUserId(userId);
    }

    @Override
    public List<Income> getIncomesByMonth(int userId, String month) {
        validateMonth(month);
        return incomeDAO.findByMonth(userId, month);
    }

    @Override
    public List<Income> getAllIncomes() {
        return incomeDAO.findAll();
    }

    @Override
    public void updateIncome(Income income) {
        validateIncome(income);
        incomeDAO.update(income);
    }

    @Override
    public void updateRemainIncome(int incomeId, int userId, BigDecimal amount) {
        if (incomeId <= 0) {
            throw new IllegalArgumentException("IncomeID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }
        incomeDAO.updateRemainIncome(incomeId, userId, amount);
    }

    private void validateIncome(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null");
        }
        if (income.getUserId() <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (income.getIncomeId() <= 0) {
            throw new IllegalArgumentException("IncomeID must be positive");
        }
        validateMonth(income.getIcMonth());
        if (income.getIncomeName() == null || income.getIncomeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Income name cannot be null or empty");
        }
        if (income.getIncomeAmount() == null || income.getIncomeAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Income amount cannot be null or negative");
        }
        if (income.getRemainIncome() == null || income.getRemainIncome().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Remain income cannot be null or negative");
        }
        if (income.getRemainIncome().compareTo(income.getIncomeAmount()) > 0) {
            throw new IllegalArgumentException("Remain income cannot be greater than income amount");
        }
    }

    private void validateMonth(String month) {
        if (month == null || !month.matches("^([1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("Invalid month value. Must be between 1 and 12");
        }
    }
} 