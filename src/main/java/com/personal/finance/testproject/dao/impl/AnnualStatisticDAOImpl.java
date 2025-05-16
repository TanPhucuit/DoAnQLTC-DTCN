package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.AnnualStatisticDAO;
import com.personal.finance.testproject.model.AnnualStatistic;
import java.sql.*;
import java.time.LocalDate;

public class AnnualStatisticDAOImpl implements AnnualStatisticDAO {
    private final Connection connection;

    public AnnualStatisticDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AnnualStatistic findByUserIdAndYear(int userId, String srYear) {
        String sql = "SELECT * FROM ANNUAL_STATISTIC WHERE UserID = ? AND sr_year = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, srYear);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AnnualStatistic stat = new AnnualStatistic();
                    stat.setUserId(rs.getInt("UserID"));
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
            throw new RuntimeException("Error finding annual statistic by userId and sr_year", e);
        }
        return null;
    }
} 