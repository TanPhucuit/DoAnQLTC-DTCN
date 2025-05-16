package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Invest;
import java.util.List;

public interface InvestDAO {
    void insert(Invest invest);
    Invest findById(String investId, int userId);
    List<Invest> findByUserId(int userId);
    List<Invest> findByMonth(String month);
    List<Invest> findByUserIdAndMonth(int userId, String month);
    List<Invest> findAll();
    void update(Invest invest);
    void updateInvestProperty(String investId, String month, double amount);

} 