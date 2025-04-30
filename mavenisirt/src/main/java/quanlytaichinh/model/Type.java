/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.model;

/**
 *
 * @author 23520
 */
public class Type {
      private String typeId;
    private int userId;
    private double maxAmount;
    private String typeDescription;
    private double transTypeRate;

    public Type() {};

    public Type(String typeId, int userId, double maxAmount, String typeDescription, double transTypeRate) {
        this.typeId = typeId;
        this.userId = userId;
        this.maxAmount = maxAmount;
        this.typeDescription = typeDescription;
        this.transTypeRate = transTypeRate;
    }

    public String getTypeId() {
        return typeId;
    }

    public int getUserId() {
        return userId;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public double getTransTypeRate() {
        return transTypeRate;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public void setTransTypeRate(double transTypeRate) {
        this.transTypeRate = transTypeRate;
    }
    
}
