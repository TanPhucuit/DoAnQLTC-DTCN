/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.model;
import java.util.Date;
/**
 *
 * @author 23520
 */
public class Transaction {
    private int transId;
    private String typeId;
    private int userId;
    private double transAmount;
    private Date transDate;
    private double soldNumUnit;
    private double soldProfit;
    private Integer loanId;
    private String inStId;
    private Integer incomeId;
    private Integer saveId;
    private Integer overPayFeeId;
    private String incomeName;
    public Transaction() {};

    public Transaction(int transId, String typeId, int userId, double transAmount, Date transDate, double soldNumUnit, double soldProfit, Integer loanId, String inStId, Integer incomeId, Integer saveId, Integer overPayFeeId, String incomeName) {
        this.transId = transId;
        this.typeId = typeId;
        this.userId = userId;
        this.transAmount = transAmount;
        this.transDate = transDate;
        this.soldNumUnit = soldNumUnit;
        this.soldProfit = soldProfit;
        this.loanId = loanId;
        this.inStId = inStId;
        this.incomeId = incomeId;
        this.saveId = saveId;
        this.overPayFeeId = overPayFeeId;
        this.incomeName = incomeName;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }
    
    public int getTransId() {
        return transId;
    }

    public String getTypeId() {
        return typeId;
    }

    public int getUserId() {
        return userId;
    }

    public double getTransAmount() {
        return transAmount;
    }

    public Date getTransDate() {
        return transDate;
    }

    public double getSoldNumUnit() {
        return soldNumUnit;
    }

    public double getSoldProfit() {
        return soldProfit;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public String getInStId() {
        return inStId;
    }

    public Integer getIncomeId() {
        return incomeId;
    }

    public Integer getSaveId() {
        return saveId;
    }

    public Integer getOverPayFeeId() {
        return overPayFeeId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTransAmount(double transAmount) {
        this.transAmount = transAmount;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public void setSoldNumUnit(double soldNumUnit) {
        this.soldNumUnit = soldNumUnit;
    }

    public void setSoldProfit(double soldProfit) {
        this.soldProfit = soldProfit;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public void setInStId(String inStId) {
        this.inStId = inStId;
    }

    public void setIncomeId(Integer incomeId) {
        this.incomeId = incomeId;
    }

    public void setSaveId(Integer saveId) {
        this.saveId = saveId;
    }

    public void setOverPayFeeId(Integer overPayFeeId) {
        this.overPayFeeId = overPayFeeId;
    }
    
    
}
