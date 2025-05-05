package quanlytaichinh.model;
import quanlytaichinh.model.*;
import java.util.List;
import java.util.ArrayList;
public class ReportData {
	private double averageDailySpending;
    private double averageWeeklySpending;
    private double monthlyCumulativePnl;
    private double overallCumulativePnl;

    private long totalTransactionCount;

    private List<Type> spendingTypeDetails; 

    private double currentMonthSpending;

	
	public ReportData(double averageDailySpending, double averageWeeklySpending, double monthlyCumulativePnl,
			double overallCumulativePnl, long totalTransactionCount, List<Type> spendingTypeDetails,
			double currentMonthSpending) {
		super();
		this.averageDailySpending = averageDailySpending;
		this.averageWeeklySpending = averageWeeklySpending;
		this.monthlyCumulativePnl = monthlyCumulativePnl;
		this.overallCumulativePnl = overallCumulativePnl;
		this.totalTransactionCount = totalTransactionCount;
		this.spendingTypeDetails = spendingTypeDetails;
		this.currentMonthSpending = currentMonthSpending;
	}
	public ReportData() {
		
		this.spendingTypeDetails = new ArrayList<>();
	
	}
	public double getOverallCumulativePnl() {
		return overallCumulativePnl;
	}
	public void setOverallCumulativePnl(double overallCumulativePnl) {
		this.overallCumulativePnl = overallCumulativePnl;
	}
	public long getTotalTransactionCount() {
		return totalTransactionCount;
	}
	public void setTotalTransactionCount(long totalTransactionCount) {
		this.totalTransactionCount = totalTransactionCount;
	}
	public List<Type> getSpendingTypeDetails() {
		return spendingTypeDetails;
	}
	public void setSpendingTypeDetails(List<Type> spendingTypeDetails) {
		this.spendingTypeDetails = spendingTypeDetails;
	}
	public double getCurrentMonthSpending() {
		return currentMonthSpending;
	}
	public void setCurrentMonthSpending(double currentMonthSpending) {
		this.currentMonthSpending = currentMonthSpending;
	}
	public double getAverageDailySpending() {
		return averageDailySpending;
	}
	public void setAverageDailySpending(double averageDailySpending) {
		this.averageDailySpending = averageDailySpending;
	}
	public double getAverageWeeklySpending() {
		return averageWeeklySpending;
	}
	public void setAverageWeeklySpending(double averageWeeklySpending) {
		this.averageWeeklySpending = averageWeeklySpending;
	}
	public double getMonthlyCumulativePnl() {
		return monthlyCumulativePnl;
	}
	public void setMonthlyCumulativePnl(double monthlyCumulativePnl) {
		this.monthlyCumulativePnl = monthlyCumulativePnl;
	};
	
	
    
}
