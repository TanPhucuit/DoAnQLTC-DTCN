package quanlytaichinh.model;
import java.sql.*;
import quanlytaichinh.model.*;
public class StatisticReport {
	private int userId;
    private String accountBalanceID; 
    private String sr_month;         
    private double average_per_day;
    private double average_per_week;
    private double cumulative_pnl;    
    private Date up_date;
	public StatisticReport(int userId, String accountBalanceID, String sr_month, double average_per_day,
			double average_per_week, double cumulative_pnl, Date up_date) {
		this.userId = userId;
		this.accountBalanceID = accountBalanceID;
		this.sr_month = sr_month;
		this.average_per_day = average_per_day;
		this.average_per_week = average_per_week;
		this.cumulative_pnl = cumulative_pnl;
		this.up_date = up_date;
		
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAccountBalanceID() {
		return accountBalanceID;
	}
	public void setAccountBalanceID(String accountBalanceID) {
		this.accountBalanceID = accountBalanceID;
	}
	public String getSr_month() {
		return sr_month;
	}
	public void setSr_month(String sr_month) {
		this.sr_month = sr_month;
	}
	public double getAverage_per_day() {
		return average_per_day;
	}
	public void setAverage_per_day(double average_per_day) {
		this.average_per_day = average_per_day;
	}
	public double getAverage_per_week() {
		return average_per_week;
	}
	public void setAverage_per_week(double average_per_week) {
		this.average_per_week = average_per_week;
	}
	public double getCumulative_pnl() {
		return cumulative_pnl;
	}
	public void setCumulative_pnl(double cumulative_pnl) {
		this.cumulative_pnl = cumulative_pnl;
	}
	public Date getUp_date() {
		return up_date;
	}
	public void setUp_date(Date up_date) {
		this.up_date = up_date;
	} 
	
    public StatisticReport() {}; 
    
}
