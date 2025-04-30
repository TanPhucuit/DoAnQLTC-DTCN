/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.model;

/**
 *
 * @author 23520
 */
public class Purpose {
    private int purposeId;
    private int userId;
    private String purposeName;
    private double estimateAmount; 
    private String description;
    private boolean purposeState;

    public Purpose(){};
    public Purpose(int purposeId, int userId, String purposeName, double estimateAmount, String description, boolean purposeState) {
        this.purposeId = purposeId;
        this.userId = userId;
        this.purposeName = purposeName;
        this.estimateAmount = estimateAmount;
        this.description = description;
        this.purposeState = purposeState;
    }

    public int getPurposeId() {
        return purposeId;
    }

    public int getUserId() {
        return userId;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public double getEstimateAmount() {
        return estimateAmount;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPurposeState() {
        return purposeState;
    }

    public void setPurposeId(int purposeId) {
        this.purposeId = purposeId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }

    public void setEstimateAmount(double estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPurposeState(boolean purposeState) {
        this.purposeState = purposeState;
    }
    
}
