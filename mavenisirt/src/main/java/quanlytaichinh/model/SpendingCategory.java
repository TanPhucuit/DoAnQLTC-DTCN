/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.model;

/**
 *
 * @author 23520
 */
public class SpendingCategory {
    private String typeId;
    private String typeDescription;
    private double totalAmount;
    private double percentageRate;
    private String source;
    public SpendingCategory(){};

    public SpendingCategory(String typeId, String typeDescription, double totalAmount, double percentageRate, String source) {
        this.typeId = typeId;
        this.typeDescription = typeDescription;
        this.totalAmount = totalAmount;
        this.percentageRate = percentageRate;
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

   

    public String getTypeId() {
        return typeId;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getPercentageRate() {
        return percentageRate;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPercentageRate(double percentageRate) {
        this.percentageRate = percentageRate;
    }
    
}
