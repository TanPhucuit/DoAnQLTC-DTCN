/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.model;

import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author 23520
 */
public class SpendingByIncomeSource {
    private int incomeId; 
    private String incomeName; 
    private List<Transaction> transactions;
    public SpendingByIncomeSource(){};
    public SpendingByIncomeSource(int incomeId, String incomeName, List<Transaction> transactions) {
        this.incomeId = incomeId;
        this.incomeName = incomeName;
        this.transactions = transactions;
    }

    public int getIncomeId() {
        return incomeId;
    }

    public String getIncomeName() {
        return incomeName;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public void setIncomeName(String incomeName) {
        this.incomeName = incomeName;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
}
