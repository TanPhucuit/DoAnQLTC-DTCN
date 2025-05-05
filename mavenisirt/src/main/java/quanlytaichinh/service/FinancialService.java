/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.service;
import java.util.List;
import quanlytaichinh.model.SpendingCategory;
import quanlytaichinh.dao.FinancialPlanDao; 
import quanlytaichinh.dao.FinancialPlanDaott; 
import quanlytaichinh.dao.InvestStorageDao; 
import quanlytaichinh.dao.InvestStorageDaott;

import quanlytaichinh.model.*;
/**
 *
 * @author 23520
 */
public interface FinancialService {
    double GetTotalInCome(int id,int month,int year);
    double GetTotalSpend(int id, int month,int year);
    double GetTotalSaving(int id, int month,int year);
    double GetTotalInvest_Property(int id, int month,int year);
    double GetTotalLoanRemain(int id, int month,int year);
    double GetTotalInvest(int id, int month,int year);
    double NetAsset(int id, int month,int year);
   
    List<Loan> getLoanDetails(int userId);
  
    InvestmentSum getInvestmentSum(int userId, int month, int year);

    
    List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month, int year);


    List<SpendingCategory> GetSpendingTotalType(int userId, int month, int year);

   boolean addTransaction(Transaction transaction);
  //  List<Transaction> GetInvestmentTransaction(int userId, int month); 
    //List<Transaction> GetSpendingFromIncome(int userId, int month); 
  
    //List<SpendingCategory> GetSpendingTotalType(int userId, int month);
    //FinancialPlan getFinancialPlanData(int userId);
    //List<Transaction> getBuyInvestmentTransactions(int userId, int month);
    //List<Transaction> getSellInvestmentTransactions(int userId, int month);
    //double getCumulativePnL(int userId);
    //List<InvestStorage> getCurrentInvestmentDetails(int userId);
    //double getTotalEstimatedInvestmentProfit(int userId);
    //List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month);
    //List<SpendingCategory> getSpendingStatisticsWithRateFromDB(int userId, int month);
   ReportData getReportData(int userId, int month, int year);
    SpendingStatus GetSpendingStatus( int user,int month,int year);
    InvestStatus getCurrentInvestmentStatus(int userId);
}
