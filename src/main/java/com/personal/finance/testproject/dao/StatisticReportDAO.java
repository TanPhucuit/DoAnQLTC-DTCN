package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.StatisticReport;

public interface StatisticReportDAO {
    StatisticReport findByUserIdAndMonth(int userId, String srMonth);
} 