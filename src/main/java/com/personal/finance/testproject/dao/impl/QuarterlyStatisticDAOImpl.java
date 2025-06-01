package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.QuarterlyStatisticDAO;
import com.personal.finance.testproject.model.QuarterlyStatistic;
import java.sql.*;
import java.time.LocalDate;

public class QuarterlyStatisticDAOImpl implements QuarterlyStatisticDAO {
    private final Connection connection;

    public QuarterlyStatisticDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public QuarterlyStatistic findByUserIdAndQuarter(int userId, String srQuarter, String srYear) {
        String sql = "SELECT * FROM QUARTERLY_STATISTIC WHERE UserID = ? AND sr_quarter = ? AND sr_year = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, srQuarter);
            stmt.setString(3, srYear);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    QuarterlyStatistic stat = new QuarterlyStatistic();
                    stat.setUserId(rs.getInt("UserID"));
                    stat.setSrQuarter(rs.getString("sr_quarter"));
                    stat.setSrYear(rs.getString("sr_year"));
                    stat.setAveragePerDay(rs.getBigDecimal("average_per_day"));
                    stat.setAveragePerWeek(rs.getBigDecimal("average_per_week"));
                    stat.setCumulativePnl(rs.getBigDecimal("cumulative_pnl"));
                    Date upDate = rs.getDate("up_date");
                    stat.setUpDate(upDate != null ? upDate.toLocalDate() : null);
                    return stat;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding quarterly statistic by userId, sr_quarter, sr_year", e);
        }
        return null;
    }
} 