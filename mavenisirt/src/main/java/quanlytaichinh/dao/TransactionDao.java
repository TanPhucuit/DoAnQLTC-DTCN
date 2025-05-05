package quanlytaichinh.dao; // Hoặc package đúng của bạn

import quanlytaichinh.model.Transaction;
import quanlytaichinh.model.SpendingCategory;
import quanlytaichinh.model.SpendingByIncomeSource;
import java.util.List;

 
public interface TransactionDao {

  
    boolean addTransaction(Transaction transaction);
    long getTransactionCount(int userId, int month, int year);


    List<Transaction> GetInvestmentTransaction(int userId, int month, int year); 

    List<Transaction> GetSpendingFromIncome(int userId, int month, int year); 

    
    List<Transaction> getBuyInvestmentTransactions(int userId, int month, int year);

    
    List<Transaction> getSellInvestmentTransactions(int userId, int month, int year);

    
    List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month, int year);

  
    List<SpendingCategory> GetSpendingTotalType(int userId, int month, int year);
}