package quanlytaichinh.model;
import java.util.*;
import quanlytaichinh.*;

public class InvestStatus {
	private double realizedPnl;           
    private double totalEstimatedPnl;      
    private double currentPortfolioValue; 
    private double userWarningLossRate;   
    private boolean isInvestmentSafe;
    private List<InvestStatusDetail> investmentDetails;
	public InvestStatus() {
		
	}
	public InvestStatus(double realizedPnl, double totalEstimatedPnl, double currentPortfolioValue,
			double userWarningLossRate, boolean isInvestmentSafe, List<InvestStatusDetail> investmentDetails) {
		
		this.realizedPnl = realizedPnl;
		this.totalEstimatedPnl = totalEstimatedPnl;
		this.currentPortfolioValue = currentPortfolioValue;
		this.userWarningLossRate = userWarningLossRate;
		this.isInvestmentSafe = isInvestmentSafe;
		this.investmentDetails = investmentDetails;
	}
	public double getRealizedPnl() {
		return realizedPnl;
	}
	public void setRealizedPnl(double realizedPnl) {
		this.realizedPnl = realizedPnl;
	}
	public double getTotalEstimatedPnl() {
		return totalEstimatedPnl;
	}
	public void setTotalEstimatedPnl(double totalEstimatedPnl) {
		this.totalEstimatedPnl = totalEstimatedPnl;
	}
	public double getCurrentPortfolioValue() {
		return currentPortfolioValue;
	}
	public void setCurrentPortfolioValue(double currentPortfolioValue) {
		this.currentPortfolioValue = currentPortfolioValue;
	}
	public double getUserWarningLossRate() {
		return userWarningLossRate;
	}
	public void setUserWarningLossRate(double userWarningLossRate) {
		this.userWarningLossRate = userWarningLossRate;
	}
	public boolean isInvestmentSafe() {
		return isInvestmentSafe;
	}
	public void setInvestmentSafe(boolean isInvestmentSafe) {
		this.isInvestmentSafe = isInvestmentSafe;
	}
	public List<InvestStatusDetail> getInvestmentDetails() {
		return investmentDetails;
	}
	public void setInvestmentDetails(List<InvestStatusDetail> investmentDetails) {
		this.investmentDetails = investmentDetails;
	}
    
}
