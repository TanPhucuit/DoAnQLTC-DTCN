package quanlytaichinh.model;
import java.util.List;

public class SpendingStatus {
		double TotalIncome;
		double TotalSpending;
		public SpendingStatus() {};
		public SpendingStatus(double totalIncome, double totalSpending) {
			TotalIncome = totalIncome;
			TotalSpending = totalSpending;
		}
		public double getTotalIncome() {
			return TotalIncome;
		}
		public void setTotalIncome(double totalIncome) {
			TotalIncome = totalIncome;
		}
		public double getTotalSpending() {
			return TotalSpending;
		}
		public void setTotalSpending(double totalSpending) {
			TotalSpending = totalSpending;
		}
		
		
}
