/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.dao;
import quanlytaichinh.model.Transaction;
import java.util.List;
import java.util.Map;
import java.util.Date; 
import quanlytaichinh.model.SpendingCategory;
import quanlytaichinh.model.SpendingByIncomeSource;
/**
 *
 * @author 23520
 */
public interface TransactionDao {
    boolean addTransaction(Transaction transaction);

    List<Transaction> GetInvestmentTransaction(int userId, int month);

    List<Transaction> GetSpendingFromIncome(int userId, int month);

    List <SpendingCategory> GetSpendingTotalType(int userId, int month);
    List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month);
    List<Transaction> getSellInvestmentTransactions(int userId, int month);
    List<Transaction> getBuyInvestmentTransactions(int userId, int month);
}
