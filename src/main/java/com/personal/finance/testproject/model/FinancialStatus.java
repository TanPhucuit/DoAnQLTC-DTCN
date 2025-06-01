package com.personal.finance.testproject.model;

public class FinancialStatus {
    private boolean investStatus;   // Kết quả hàm check_invest_profit
    private boolean expenseStatus;  // Kết quả hàm over_max_amount
    private boolean financialStatus; // true nếu cả 2 đều an toàn, false nếu 1 trong 2 không an toàn

    public FinancialStatus() {}

    public FinancialStatus(boolean investStatus, boolean expenseStatus) {
        this.investStatus = investStatus;
        this.expenseStatus = expenseStatus;
        this.financialStatus = investStatus && expenseStatus;
    }

    public boolean isInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(boolean investStatus) {
        this.investStatus = investStatus;
        this.financialStatus = this.investStatus && this.expenseStatus;
    }

    public boolean isExpenseStatus() {
        return expenseStatus;
    }

    public void setExpenseStatus(boolean expenseStatus) {
        this.expenseStatus = expenseStatus;
        this.financialStatus = this.investStatus && this.expenseStatus;
    }

    public boolean isFinancialStatus() {
        return financialStatus;
    }

    @Override
    public String toString() {
        return "FinancialStatus{" +
                "investStatus=" + investStatus +
                ", expenseStatus=" + expenseStatus +
                ", financialStatus=" + financialStatus +
                '}';
    }
} 