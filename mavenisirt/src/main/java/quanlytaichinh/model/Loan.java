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
public class Loan {
    private int loanId;
    private int userId;
    private int purposeId;
    private String formId; 
    private double loanAmount; 
    private Date disburDate;
    private double interest;
    private int numTerm; 
    private double payPerTerm;  
    private int dateOfPayment;
    private int numPaidTerm; 
    private double usedAmount; 
    private double paidAmount; 
    private double loanRemain; 
    private Date upDate; 
    private String purpose_name;
    
    public Loan(){};

    public Loan(int loanId, int userId, int purposeId, String formId, double loanAmount, Date disburDate, double interest, int numTerm, double payPerTerm, int dateOfPayment, int numPaidTerm, double usedAmount, double paidAmount, double loanRemain, Date upDate, String purpose_name) {
        this.loanId = loanId;
        this.userId = userId;
        this.purposeId = purposeId;
        this.formId = formId;
        this.loanAmount = loanAmount;
        this.disburDate = disburDate;
        this.interest = interest;
        this.numTerm = numTerm;
        this.payPerTerm = payPerTerm;
        this.dateOfPayment = dateOfPayment;
        this.numPaidTerm = numPaidTerm;
        this.usedAmount = usedAmount;
        this.paidAmount = paidAmount;
        this.loanRemain = loanRemain;
        this.upDate = upDate;
        this.purpose_name = purpose_name;
    }

    

    public int getLoanId() {
        return loanId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPurposeId() {
        return purposeId;
    }

    public String getFormId() {
        return formId;
    }

    public double getLoanAmount() {
        return loanAmount;
    }

    public Date getDisburDate() {
        return disburDate;
    }

    public double getInterest() {
        return interest;
    }

    public int getNumTerm() {
        return numTerm;
    }

    public double getPayPerTerm() {
        return payPerTerm;
    }

    public int getDateOfPayment() {
        return dateOfPayment;
    }

    public int getNumPaidTerm() {
        return numPaidTerm;
    }

    public double getUsedAmount() {
        return usedAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getLoanRemain() {
        return loanRemain;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPurposeId(int purposeId) {
        this.purposeId = purposeId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setDisburDate(Date disburDate) {
        this.disburDate = disburDate;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public void setNumTerm(int numTerm) {
        this.numTerm = numTerm;
    }

    public void setPayPerTerm(double payPerTerm) {
        this.payPerTerm = payPerTerm;
    }

    public void setDateOfPayment(int dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public void setNumPaidTerm(int numPaidTerm) {
        this.numPaidTerm = numPaidTerm;
    }

    public void setUsedAmount(double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public void setLoanRemain(double loanRemain) {
        this.loanRemain = loanRemain;
    }

    public void setUpDate(Date upDate) {
        this.upDate = upDate;
    }

    public String getPurpose_name() {
        return purpose_name;
    }

    public void setPurpose_name(String purpose_name) {
        this.purpose_name = purpose_name;
    }
    
}
