package quanlytaichinh.dao;
import quanlytaichinh.model.*;
public interface StatisticReportDao {
	StatisticReport getMonthlyReport(int userId, int month, int year);
}
