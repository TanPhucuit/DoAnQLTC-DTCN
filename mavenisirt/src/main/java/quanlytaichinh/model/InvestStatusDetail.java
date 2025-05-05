package quanlytaichinh.model;

public class InvestStatusDetail {
	private String investmentId;         
    private double currentUnits;         
    private double estimatedPnl;
	public InvestStatusDetail(String investmentId, double currentUnits, double estimatedPnl) {
	
		this.investmentId = investmentId;
		this.currentUnits = currentUnits;
		this.estimatedPnl = estimatedPnl;
	}
    public InvestStatusDetail () {}
	public String getInvestmentId() {
		return investmentId;
	}
	public void setInvestmentId(String investmentId) {
		this.investmentId = investmentId;
	}
	public double getCurrentUnits() {
		return currentUnits;
	}
	public void setCurrentUnits(double currentUnits) {
		this.currentUnits = currentUnits;
	}
	public double getEstimatedPnl() {
		return estimatedPnl;
	}
	public void setEstimatedPnl(double estimatedPnl) {
		this.estimatedPnl = estimatedPnl;
	};
    
}
