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
public class InvestStorage {
    private String inStId;
    private int userId;
    private double numUnit;
    private double buyPrice;
    private double esProfit;
    private Date upDate;

    public InvestStorage() {};

    public InvestStorage(String inStId, int userId, double numUnit, double buyPrice, double esProfit, Date upDate) {
        this.inStId = inStId;
        this.userId = userId;
        this.numUnit = numUnit;
        this.buyPrice = buyPrice;
        this.esProfit = esProfit;
        this.upDate = upDate;
    }

    public String getInStId() {
        return inStId;
    }

    public int getUserId() {
        return userId;
    }

    public double getNumUnit() {
        return numUnit;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getEsProfit() {
        return esProfit;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setInStId(String inStId) {
        this.inStId = inStId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setNumUnit(double numUnit) {
        this.numUnit = numUnit;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setEsProfit(double esProfit) {
        this.esProfit = esProfit;
    }

    public void setUpDate(Date upDate) {
        this.upDate = upDate;
    }
    
}
