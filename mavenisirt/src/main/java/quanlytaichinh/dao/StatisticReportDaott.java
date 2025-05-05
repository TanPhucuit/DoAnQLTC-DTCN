package quanlytaichinh.dao;
import quanlytaichinh.ketnoidb;
import quanlytaichinh.model.*;
import java.sql.*;

public class StatisticReportDaott implements StatisticReportDao {
	@Override
    public StatisticReport getMonthlyReport(int userId, int month, int year) {
        StatisticReport report = null;
        String sql = "SELECT UserID, AccountBalanceID, sr_month, average_per_day, average_per_week, " +
                     "cumulative_pnl, up_date FROM STATISTIC_REPORT " +
                     "WHERE UserID = ? AND sr_month = ?";

        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {

            save.setInt(1, userId);
            save.setString(2, String.valueOf(month));

            try (ResultSet result =save.executeQuery()) {
                if (result.next()) {
                    report = new StatisticReport();
                    report.setUserId(result.getInt("UserID"));
                    report.setAccountBalanceID(result.getString("AccountBalanceID"));
                    report.setSr_month(result.getString("sr_month"));
                    report.setAverage_per_day(result.getDouble("average_per_day")); 
                    report.setAverage_per_week(result.getDouble("average_per_week"));
                    report.setCumulative_pnl(result.getDouble("cumulative_pnl"));   
                    report.setUp_date(result.getDate("up_date"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy báo cáo tháng " + month + " cho UserID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return report;
    }
}
