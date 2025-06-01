package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;

public class Loan {
    private int loanId;
    private int userId;
    private int purposeId;
    private String formId;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private int numTerm;
    private int dateOfPayment;
    private String description;
    private Date disburDate;
    private BigDecimal loanRemain;
    private String bankAccountNumber;
    private int numPaidTerm;
    private BigDecimal paidAmount;

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        if (loanId <= 0) {
            throw new IllegalArgumentException("LoanID must be positive");
        }
        this.loanId = loanId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        this.userId = userId;
    }

    public int getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(int purposeId) {
        if (purposeId <= 0) {
            throw new IllegalArgumentException("PurposeID must be positive");
        }
        this.purposeId = purposeId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        if (formId == null || formId.trim().isEmpty()) {
            throw new IllegalArgumentException("FormID cannot be null or empty");
        }
        this.formId = formId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        if (loanAmount == null || loanAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Loan amount cannot be null or negative");
        }
        this.loanAmount = loanAmount;
    }

    public BigDecimal getAmount() {
        return loanAmount;
    }

    public void setAmount(BigDecimal amount) {
        this.loanAmount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        if (interestRate == null || interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be null or negative");
        }
        this.interestRate = interestRate;
    }

    public int getNumTerm() {
        return numTerm;
    }

    public void setNumTerm(int numTerm) {
        if (numTerm <= 0) {
            throw new IllegalArgumentException("Term must be positive");
        }
        this.numTerm = numTerm;
    }

    public int getTerm() {
        return numTerm;
    }

    public void setTerm(int term) {
        setNumTerm(term);
    }

    public int getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(int dateOfPayment) {
        if (dateOfPayment < 1 || dateOfPayment > 31) {
            throw new IllegalArgumentException("Date of payment must be between 1 and 31");
        }
        this.dateOfPayment = dateOfPayment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDisburDate() {
        return disburDate;
    }

    public void setDisburDate(Date disburDate) {
        if (disburDate == null) {
            throw new IllegalArgumentException("Disbur date cannot be null");
        }
        this.disburDate = disburDate;
    }

    public BigDecimal getLoanRemain() {
        return loanRemain;
    }

    public void setLoanRemain(BigDecimal loanRemain) {
        if (loanRemain == null || loanRemain.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Loan remain cannot be null or negative");
        }
        if (this.loanAmount != null && loanRemain.compareTo(this.loanAmount) > 0) {
            throw new IllegalArgumentException("Loan remain cannot be greater than loan amount");
        }
        this.loanRemain = loanRemain;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        if (bankAccountNumber != null && bankAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number cannot be empty");
        }
        this.bankAccountNumber = bankAccountNumber;
    }

    public int getNumPaidTerm() {
        return numPaidTerm;
    }

    public void setNumPaidTerm(int numPaidTerm) {
        this.numPaidTerm = numPaidTerm;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getLoanMonth() {
        return String.format("%02d", disburDate.getMonth() + 1);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", userId=" + userId +
                ", purposeId=" + purposeId +
                ", formId='" + formId + '\'' +
                ", loanAmount=" + loanAmount +
                ", interestRate=" + interestRate +
                ", numTerm=" + numTerm +
                ", dateOfPayment=" + dateOfPayment +
                ", description='" + description + '\'' +
                ", disburDate=" + disburDate +
                ", loanRemain=" + loanRemain +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", numPaidTerm=" + numPaidTerm +
                ", paidAmount=" + paidAmount +
                '}';
    }
}
