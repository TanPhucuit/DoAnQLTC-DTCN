package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Spend;
import java.util.List;

public interface SpendDAO {
    void insert(Spend spend);
    Spend findById(String spendId, String month);
    List<Spend> findByUserId(int userId);
    List<Spend> findByMonth(String month);
    List<Spend> findByUserIdAndMonth(int userId, String month);
    List<Spend> findAll();
    void update(Spend spend);
  
} 