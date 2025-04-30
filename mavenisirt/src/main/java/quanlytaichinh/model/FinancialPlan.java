/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.model;

/**
 *
 * @author 23520
 */
public class FinancialPlan {
    private int userId;
    private String investorType;
    private double curProperty;
    private double curInvestProperty;
    private double curEsProfit;
    private double curCumulativePnl;
    private double warningLossRate;

    public FinancialPlan() {};

    public FinancialPlan(int userId, String investorType, double curProperty, double curInvestProperty, double curEsProfit, double curCumulativePnl, double warningLossRate) {
        this.userId = userId;
        this.investorType = investorType;
        this.curProperty = curProperty;
        this.curInvestProperty = curInvestProperty;
        this.curEsProfit = curEsProfit;
        this.curCumulativePnl = curCumulativePnl;
        this.warningLossRate = warningLossRate;
    }

    public int getUserId() {
        return userId;
    }

    public String getInvestorType() {
        return investorType;
    }

    public double getCurProperty() {
        return curProperty;
    }

    public double getCurInvestProperty() {
        return curInvestProperty;
    }

    public double getCurEsProfit() {
        return curEsProfit;
    }

    public double getCurCumulativePnl() {
        return curCumulativePnl;
    }

    public double getWarningLossRate() {
        return warningLossRate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setInvestorType(String investorType) {
        this.investorType = investorType;
    }

    public void setCurProperty(double curProperty) {
        this.curProperty = curProperty;
    }

    public void setCurInvestProperty(double curInvestProperty) {
        this.curInvestProperty = curInvestProperty;
    }

    public void setCurEsProfit(double curEsProfit) {
        this.curEsProfit = curEsProfit;
    }

    public void setCurCumulativePnl(double curCumulativePnl) {
        this.curCumulativePnl = curCumulativePnl;
    }

    public void setWarningLossRate(double warningLossRate) {
        this.warningLossRate = warningLossRate;
    }
    
}
