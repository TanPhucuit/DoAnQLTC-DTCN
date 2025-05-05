package quanlytaichinh.model;
import java.util.List;
import java.util.ArrayList; 
public class InvestmentSum {
	private List<Transaction> buyTransactions;
    private List<Transaction> sellTransactions;
    private double cumulativeNetProfit;     
    private double totalEstimatedProfit;
    public InvestmentSum() {
        this.buyTransactions = new ArrayList<>();
        this.sellTransactions = new ArrayList<>();
        this.cumulativeNetProfit = 0.0;
        this.totalEstimatedProfit = 0.0;
    }
    public InvestmentSum(List<Transaction> buyTransactions, List<Transaction> sellTransactions, double cumulativeNetProfit, double totalEstimatedProfit) {
        this.buyTransactions = buyTransactions;
        this.sellTransactions = sellTransactions;
        this.cumulativeNetProfit = cumulativeNetProfit;
        this.totalEstimatedProfit = totalEstimatedProfit;
    }

    public List<Transaction> getBuyTransactions() {
        return buyTransactions;
    }

    public void setBuyTransactions(List<Transaction> buyTransactions) {
        this.buyTransactions = buyTransactions;
    }

    public List<Transaction> getSellTransactions() {
        return sellTransactions;
    }

    public void setSellTransactions(List<Transaction> sellTransactions) {
        this.sellTransactions = sellTransactions;
    }

    public double getCumulativeNetProfit() {
        return cumulativeNetProfit;
    }

    public void setCumulativeNetProfit(double cumulativeNetProfit) {
        this.cumulativeNetProfit = cumulativeNetProfit;
    }

    public double getTotalEstimatedProfit() {
        return totalEstimatedProfit;
    }

    public void setTotalEstimatedProfit(double totalEstimatedProfit) {
        this.totalEstimatedProfit = totalEstimatedProfit;
    }
	
    
}
