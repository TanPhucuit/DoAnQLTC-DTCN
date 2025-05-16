package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.AnnualStatistic;

public interface AnnualStatisticDAO {
    AnnualStatistic findByUserIdAndYear(int userId, String srYear);
} 