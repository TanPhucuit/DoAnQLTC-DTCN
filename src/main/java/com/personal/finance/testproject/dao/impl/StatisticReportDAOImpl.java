package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.StatisticReportDAO;
import com.personal.finance.testproject.model.StatisticReport;
import java.sql.*;
import java.time.LocalDate;

public class StatisticReportDAOImpl implements StatisticReportDAO {
    private final Connection connection;

    public StatisticReportDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public StatisticReport findByUserIdAndMonth(int userId, String srMonth) {
        String sql = "SELECT * FROM STATISTIC_REPORT WHERE UserID = ? AND sr_month = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, srMonth);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    StatisticReport report = new StatisticReport();
                    report.setUserId(rs.getInt("UserID"));
                    report.setAccountBalanceId(rs.getString("AccountBalanceID"));
                    report.setSrQuarter(rs.getString("sr_quarter"));
                    report.setSrMonth(rs.getString("sr_month"));
                    report.setAveragePerDay(rs.getBigDecimal("average_per_day"));
                    report.setAveragePerWeek(rs.getBigDecimal("average_per_week"));
                    report.setCumulativePnl(rs.getBigDecimal("cumulative_pnl"));
                    Date upDate = rs.getDate("up_date");
                    if (upDate == null || upDate.toString().equals("0000-00-00")) {
                        upDate = java.sql.Date.valueOf("2000-01-01");
                    }
                    report.setUpDate(upDate != null ? upDate.toLocalDate() : null);
                    return report;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding statistic report by userId and sr_month", e);
        }
        return null;
    }
} 