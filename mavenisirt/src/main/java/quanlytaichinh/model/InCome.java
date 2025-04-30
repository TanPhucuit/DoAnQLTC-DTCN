/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.model;

/**
 *
 * @author 23520
 */
public class InCome {
    private int incomeId;
    private int userId;
    private String ic_month;
    private String income_name;
    private double income_amount; 
    private double remain_income; 
    public InCome (){};
    public InCome(int incomeId, int userId, String ic_month, String income_name, double income_amount, double remain_income) {
        this.incomeId = incomeId;
        this.userId = userId;
        this.ic_month = ic_month;
        this.income_name = income_name;
        this.income_amount = income_amount;
        this.remain_income = remain_income;
    }

    public int getIncomeId() {
        return incomeId;
    }

    public int getUserId() {
        return userId;
    }

    public String getIc_month() {
        return ic_month;
    }

    public String getIncome_name() {
        return income_name;
    }

    public double getIncome_amount() {
        return income_amount;
    }

    public double getRemain_income() {
        return remain_income;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIc_month(String ic_month) {
        this.ic_month = ic_month;
    }

    public void setIncome_name(String income_name) {
        this.income_name = income_name;
    }

    public void setIncome_amount(double income_amount) {
        this.income_amount = income_amount;
    }

    public void setRemain_income(double remain_income) {
        this.remain_income = remain_income;
    }
    
}
