package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.FinancialPlanDAO;
import com.personal.finance.testproject.dao.impl.FinancialPlanDAOImpl;
import com.personal.finance.testproject.model.FinancialPlan;
import java.sql.Connection;
import java.util.List;

public class FinancialPlanService {
    private final FinancialPlanDAO financialPlanDAO;

    public FinancialPlanService(Connection connection) {
        this.financialPlanDAO = new FinancialPlanDAOImpl(connection);
    }

    public FinancialPlan getFinancialPlanByUserId(int userId) {
        return financialPlanDAO.findByUserId(userId);
    }

    public List<FinancialPlan> getFinancialPlansByInvestorType(String investorType) {
        return financialPlanDAO.findByInvestorType(investorType);
    }

    public void updateFinancialPlan(FinancialPlan financialPlan) {
        financialPlanDAO.update(financialPlan);
    }

    public void updateCurProperty(int userId, double amount) {
        financialPlanDAO.updateCurProperty(userId, amount);
    }

    public void updateCurInvestProperty(int userId, double amount) {
        financialPlanDAO.updateCurInvestProperty(userId, amount);
    }

    public void updateCurEsProfit(int userId, double amount) {
        financialPlanDAO.updateCurEsProfit(userId, amount);
    }

    public void updateCurCumulativePnl(int userId, double amount) {
        financialPlanDAO.updateCurCumulativePnl(userId, amount);
    }

    public void updateWarningLossRate(int userId, double rate) {
        financialPlanDAO.updateWarningLossRate(userId, rate);
    }
} 