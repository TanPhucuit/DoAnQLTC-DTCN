package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.InvestDAO;
import com.personal.finance.testproject.dao.InvestPropertyDAO;
import com.personal.finance.testproject.dao.impl.InvestDAOImpl;
import com.personal.finance.testproject.dao.impl.InvestPropertyDAOImpl;
import com.personal.finance.testproject.model.Invest;
import com.personal.finance.testproject.model.InvestProperty;
import java.sql.Connection;
import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

public class InvestService {
    private final InvestDAO investDAO;
    private final InvestPropertyDAO investPropertyDAO;

    public InvestService(Connection connection) {
        this.investDAO = new InvestDAOImpl(connection);
        this.investPropertyDAO = new InvestPropertyDAOImpl(connection);
    }

    public void addInvestment(int userId, String investName, String month, BigDecimal amount) {
        Invest invest = new Invest();
        invest.setUserId(userId);
        invest.setInvestId("INV" + System.currentTimeMillis());
        invest.setInvestName(investName);
        invest.setInMonth(month);
        invest.setAmount(amount);
        invest.setUpDate(new Date());
        investDAO.insert(invest);
    }

    public List<Invest> getAllInvestments(int userId) {
        return investDAO.findByUserId(userId);
    }

    public List<Invest> getInvestmentsByMonth(int userId, String month) {
        return investDAO.findByUserIdAndMonth(userId, month);
    }

    public void addInvestProperty(int userId, String propertyName, String month, BigDecimal amount) {
        InvestProperty property = new InvestProperty();
        property.setUserId(userId);
        property.setPropertyId("PROP" + System.currentTimeMillis());
        property.setPropertyName(propertyName);
        property.setPrMonth(month);
        property.setAmount(amount);
        property.setUpDate(new Date());
        investPropertyDAO.insert(property);
    }

    public List<InvestProperty> getAllInvestProperties(int userId) {
        return investPropertyDAO.findByUserId(userId);
    }

    public List<InvestProperty> getInvestPropertiesByMonth(int userId, String month) {
        return investPropertyDAO.findByUserIdAndMonth(userId, month);
    }
} 
