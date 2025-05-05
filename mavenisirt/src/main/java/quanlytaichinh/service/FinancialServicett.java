/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.service;
import quanlytaichinh.model.*;

import java.sql.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import quanlytaichinh.dao.*;
import quanlytaichinh.model.*;
/**
 *
 * @author 23520
 */
public class FinancialServicett implements FinancialService {
    private AccountBalanceDao acc;
    private IncomeDao ic;
    private TransactionDao transactionDao;
    private LoanDao loanDao;
     private FinancialPlanDao financialPlanDao;
    private InvestStorageDao investStorageDao;
    private StatisticReportDao statisticReportDao;
    private TypeDao typeDao; 
    
  
    public FinancialServicett(AccountBalanceDao acc, IncomeDao ic, TransactionDao transactionDao, LoanDao loanDao,
			FinancialPlanDao financialPlanDao, InvestStorageDao investStorageDao, StatisticReportDao statisticReportDao,
			TypeDao typeDao) {
		this.acc = acc;
		this.ic = ic;
		this.transactionDao = transactionDao;
		this.loanDao = loanDao;
		this.financialPlanDao = financialPlanDao;
		this.investStorageDao = investStorageDao;
		this.statisticReportDao = statisticReportDao;
		this.typeDao = typeDao;
	}
	@Override
    public double GetTotalInCome(int userid,int month,int year)
    {
        Double result = 0.0;
        System.out.println("Tổng thu nhập của người dùng với id" +userid);
        AccountBalance ac = acc.findByUserIdAndMonth(userid, month,year);
        if(ac!= null)
            ac.getTotal_remain_income();
            return 0.0;
    }
   @Override
         public double GetTotalSpend(int userid,int month,int year) 
         {
             Double result = 0.0;
             System.out.println("Tổng chi tiêu của người dùng ");
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month,year);
                 if(ac!= null)
            ac.getTotal_spend();
            return 0.0;
             
         }
      @Override
      public double GetTotalSaving(int userid,int month,int year)
      {
             Double result = 0.0;
                System.out.println("Tổng tiết kiệm của người dùng ");
                AccountBalance ac = acc.findByUserIdAndMonth(userid, month,year);
                if(ac!= null)
            ac.getTotal_remain_save();
            return 0.0;
            }
      
   @Override
         public double GetTotalInvest_Property(int userid,int month,int year) 
         {
             Double result = 0.0;
             System.out.println("Tổng giá trị tài sản đầu tư và tích trữ của người dùng ");
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month,year);
                 if(ac!= null)
            ac.getTotal_invest_property();
            return 0.0;
                     
             
         }
     @Override
         public double GetTotalLoanRemain(int userid,int month,int year) 
         {
             Double result = 0.0;
             System.out.println("Tổng dư nợ của người dùng ");
            
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month,year);
                 if(ac!= null)
            ac.getTotal_loan_remain();
            return 0.0;
          
         }
        @Override
         public double GetTotalInvest(int userid,int month,int year) 
         {
             Double result = 0.0;
             System.out.println("Tổng vốn đầu tư của người dùng ");
   
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month,year);
               if(ac!= null)
            ac.getTotal_invest();
            return 0.0;
         }
     @Override
     public double NetAsset(int userid, int month,int year){
         Double result = 0.0;
         System.out.println("Tổng tài sản sau khi khấu trừ dư nợ");
             AccountBalance ac = acc.findByUserIdAndMonth(userid, month,year);
            if(ac== null)
            return 0.0;
             result = ac.getBalance()- ac.getTotal_loan_remain();
             return result;
             
         }
         
     
@Override
    public List<Loan> getLoanDetails(int userId) {
        System.out.println("Service: Lấy chi tiết các khoản vay cho user " + userId);
        try {
            return loanDao.GetIdUserLoan(userId); 
        } catch (Exception e) {
            System.err.println("Lỗi Service khi lấy chi tiết khoản vay: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public InvestmentSum getInvestmentSum(int userId, int month, int year) {
        System.out.println("Service: Lấy thống kê đầu tư cho user " + userId + " tháng " + month + " năm " + year);
        InvestmentSum sum = new InvestmentSum(); 

        try {
           
            List<Transaction> buyTransactions = transactionDao.getBuyInvestmentTransactions(userId, month, year);
            List<Transaction> sellTransactions = transactionDao.getSellInvestmentTransactions(userId, month, year);
            FinancialPlan plan = financialPlanDao.getFinancialPlan(userId);
            double cumulativePnl = 0.0;
            double estimatedProfit = 0.0;
            if (plan != null) {
                cumulativePnl = plan.getCurCumulativePnl(); 
                estimatedProfit = plan.getCurEsProfit();    
            }

         
            sum.setBuyTransactions(buyTransactions);
            sum.setSellTransactions(sellTransactions);
            sum.setCumulativeNetProfit(cumulativePnl);
            sum.setTotalEstimatedProfit(estimatedProfit);

        } catch (Exception e) {
            System.err.println("Lỗi Service khi lấy thống kê đầu tư: " + e.getMessage());
            e.printStackTrace();
        }
        return sum;
    }

    
    @Override
    public List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month, int year) {
        System.out.println("Service: Lấy GD chi tiêu theo nguồn thu nhập cho user " + userId + " tháng " + month + " năm " + year);
        try {     
            return transactionDao.getSpendingTransactionsGroupedByIncome(userId, month, year);
        } catch (Exception e) {
            System.err.println(" Lỗi Service khi lấy GD chi tiêu theo nguồn thu nhập: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @Override
    public List<SpendingCategory> GetSpendingTotalType(int userId, int month, int year) {
        System.out.println("Service: Lấy tổng chi theo loại cho user " + userId + " tháng " + month + " năm " + year);
         try {            
             return transactionDao.GetSpendingTotalType(userId, month, year);
         } catch (Exception e) {
             System.err.println("Lỗi Service khi lấy tổng chi theo loại: " + e.getMessage());
             e.printStackTrace();
             return new ArrayList<>();
         }
    }    
    @Override
    public boolean addTransaction(Transaction transaction) {       
        System.out.println("Thêm giao dịch mới...");
        try {
            return transactionDao.addTransaction(transaction);
        } catch (Exception e) {
            System.err.println("Lỗi Service khi thêm giao dịch: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public ReportData getReportData(int userId, int month, int year) {
        System.out.println("Service: Lấy dữ liệu báo cáo user " + userId + " T" + month + "/" + year);
        ReportData reportData = new ReportData();
        try {    	           
            StatisticReport monthlyStats = statisticReportDao.getMonthlyReport(userId, month, year);
            if (monthlyStats != null) {
                reportData.setAverageDailySpending(monthlyStats.getAverage_per_day());
                reportData.setAverageWeeklySpending(monthlyStats.getAverage_per_week());
                reportData.setMonthlyCumulativePnl(monthlyStats.getCumulative_pnl());
            }
            FinancialPlan plan = financialPlanDao.getFinancialPlan(userId);
            if (plan != null) {
                reportData.setOverallCumulativePnl(plan.getCurCumulativePnl());
            }            
            long transactionCount = transactionDao.getTransactionCount(userId, month, year);
            reportData.setTotalTransactionCount(transactionCount);
            List<Type> types = typeDao.getTypesByUserId(userId);
            reportData.setSpendingTypeDetails(types); 
             double currentMonthSpend = this.GetTotalSpend(userId, month, year);
             reportData.setCurrentMonthSpending(currentMonthSpend);
        } catch (Exception e) {
            System.err.println("Lỗi Service khi lấy dữ liệu báo cáo: " + e.getMessage());
            e.printStackTrace();
        }
        return reportData;
    }
    @Override
    
    public SpendingStatus GetSpendingStatus(int userid,int month,int year)
    {
    	
    	double totalincome = 0.0;
    	double totalspend = 0.0;
    	totalincome = ic.GetTotalFirstIncome(userid, month);
    	AccountBalance ac = acc.findByUserIdAndMonth(userid, month, year);
    	if(ac == null)
    		totalspend = ac.getTotal_spend();
    	else return null;
    	return new SpendingStatus(totalincome,totalspend);
    }
    @Override
    public InvestStatus getCurrentInvestmentStatus(int userId) {
        System.out.println("Service: Lấy trạng thái đầu tư hiện tại cho user " + userId);
        InvestStatus statusData = new InvestStatus();

        try {         
            FinancialPlan plan = financialPlanDao.getFinancialPlan(userId);
            double currentPortfolioValue = 0.0;
            double totalEstimatedPnl = 0.0;

            if (plan != null) {
                statusData.setRealizedPnl(plan.getCurCumulativePnl()); 
                statusData.setTotalEstimatedPnl(plan.getCurEsProfit());   
                statusData.setCurrentPortfolioValue(plan.getCurInvestProperty()); 
                statusData.setUserWarningLossRate(plan.getWarningLossRate());   

                currentPortfolioValue = plan.getCurInvestProperty();
                totalEstimatedPnl = plan.getCurEsProfit();
            } else {
                 System.out.println("Service: Không tìm thấy Financial Plan cho user " + userId);
            }

            
            boolean isSafe = true;
            if (totalEstimatedPnl < 0 && currentPortfolioValue > 0) {
               
                double lossPercentage = (Math.abs(totalEstimatedPnl) / currentPortfolioValue) * 100.0;
                if (lossPercentage > 30.0) { 
                    isSafe = false; 
                }
            }
            statusData.setInvestmentSafe(isSafe);

           
            List<InvestStorage> currentInvestments = investStorageDao.getCurrentInvestments(userId);
            List<InvestStatusDetail> details = new ArrayList<>();
            if (currentInvestments != null) {
                for (InvestStorage storage : currentInvestments) {
                    InvestStatusDetail detail = new InvestStatusDetail(
                        storage.getInStId(),        
                        storage.getNumUnit(),       
                        storage.getEsProfit()       
                    );
                    details.add(detail);
                }
            }
            statusData.setInvestmentDetails(details);

        } catch (Exception e) {
            System.err.println("Lỗi Service khi lấy trạng thái đầu tư: " + e.getMessage());
            e.printStackTrace();
          
        }

        return statusData;
    }
}


  

     


