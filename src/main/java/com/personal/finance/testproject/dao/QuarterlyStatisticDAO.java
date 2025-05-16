package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.QuarterlyStatistic;

public interface QuarterlyStatisticDAO {
    QuarterlyStatistic findByUserIdAndQuarter(int userId, String srQuarter, String srYear);
} 