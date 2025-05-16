package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.InvestProperty;
import java.util.List;

public interface InvestPropertyDAO {
    void insert(InvestProperty investProperty);
    InvestProperty findById(String investPropertyId, String month);
    List<InvestProperty> findByUserId(int userId);
    List<InvestProperty> findByMonth(String month);
    List<InvestProperty> findByUserIdAndMonth(int userId, String month);
    List<InvestProperty> findAll();
    void update(InvestProperty investProperty);

} 