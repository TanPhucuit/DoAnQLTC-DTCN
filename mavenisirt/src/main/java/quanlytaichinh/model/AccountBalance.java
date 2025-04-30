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
public class AccountBalance {
     private int userId;
    private String accountBalanceID;
    private String ab_month;
    private double total_remain_income;    
    private double total_remain_save;       
    private double remain_invest;           
    private double total_loan_remain;       
    private double total_spend;  
    private double total_invest;
    private double total_invest_property;   
    private double balance;              
    private Date up_date;
    public AccountBalance(){};
    
    public AccountBalance(int userId, String accountBalanceID, String ab_month, double total_remain_income, double total_remain_save, double remain_invest, double total_loan_remain, double total_spend, double total_invest, double total_invest_property, double balance, Date up_date) {
        this.userId = userId;
        this.accountBalanceID = accountBalanceID;
        this.ab_month = ab_month;
        this.total_remain_income = total_remain_income;
        this.total_remain_save = total_remain_save;
        this.remain_invest = remain_invest;
        this.total_loan_remain = total_loan_remain;
        this.total_spend = total_spend;
        this.total_invest = total_invest;
        this.total_invest_property = total_invest_property;
        this.balance = balance;
        this.up_date = up_date;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccountBalanceID() {
        return accountBalanceID;
    }

    public String getAb_month() {
        return ab_month;
    }

    public double getTotal_remain_income() {
        return total_remain_income;
    }

    public double getTotal_remain_save() {
        return total_remain_save;
    }

    public double getRemain_invest() {
        return remain_invest;
    }

    public double getTotal_loan_remain() {
        return total_loan_remain;
    }

    public double getTotal_spend() {
        return total_spend;
    }

    public double getTotal_invest() {
        return total_invest;
    }

    public double getTotal_invest_property() {
        return total_invest_property;
    }

    public double getBalance() {
        return balance;
    }

    public Date getUp_date() {
        return up_date;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccountBalanceID(String accountBalanceID) {
        this.accountBalanceID = accountBalanceID;
    }

    public void setAb_month(String ab_month) {
        this.ab_month = ab_month;
    }

    public void setTotal_remain_income(double total_remain_income) {
        this.total_remain_income = total_remain_income;
    }

    public void setTotal_remain_save(double total_remain_save) {
        this.total_remain_save = total_remain_save;
    }

    public void setRemain_invest(double remain_invest) {
        this.remain_invest = remain_invest;
    }

    public void setTotal_loan_remain(double total_loan_remain) {
        this.total_loan_remain = total_loan_remain;
    }

    public void setTotal_spend(double total_spend) {
        this.total_spend = total_spend;
    }

    public void setTotal_invest(double total_invest) {
        this.total_invest = total_invest;
    }

    public void setTotal_invest_property(double total_invest_property) {
        this.total_invest_property = total_invest_property;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setUp_date(Date up_date) {
        this.up_date = up_date;
    }

    
}
