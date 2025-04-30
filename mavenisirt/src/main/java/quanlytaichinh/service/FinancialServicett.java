/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.service;
import quanlytaichinh.model.Transaction;
import quanlytaichinh.model.SpendingCategory;
import quanlytaichinh.dao.TransactionDao;
import quanlytaichinh.dao.TransactionDaott;
import quanlytaichinh.dao.AccountBalanceDao;     
import quanlytaichinh.dao.AccountBalanceDaott;    
import quanlytaichinh.dao.IncomeDao;             
import quanlytaichinh.dao.IncomeDaott;  
import quanlytaichinh.model.AccountBalance;
import quanlytaichinh.dao.LoanDaott;
import quanlytaichinh.dao.LoanDao;
import java.sql.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import quanlytaichinh.dao.FinancialPlanDao; 
import quanlytaichinh.dao.FinancialPlanDaott; 
import quanlytaichinh.dao.InvestStorageDao; 
import quanlytaichinh.dao.InvestStorageDaott;
import quanlytaichinh.model.SpendingByIncomeSource;
import quanlytaichinh.model.Loan;
import quanlytaichinh.model.FinancialPlan;
import quanlytaichinh.model.InvestStorage;
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
        public FinancialServicett() {
        this.acc = new AccountBalanceDaott();
        this.ic = new IncomeDaott();
        this.loanDao = new LoanDaott();
        this.transactionDao = new TransactionDaott();
        this.financialPlanDao = new FinancialPlanDaott();
        this.investStorageDao = new InvestStorageDaott();
        
    }
  
    @Override
    public double GetTotalInCome(int userid,int month)
    {
        Double result = 0.0;
        System.out.println("Tổng thu nhập của người dùng với id" +userid);
        result = ic.TotalIncomePerMonth(userid, month);
        return result;
    }
   @Override
         public double GetTotalSpend(int userid,int month) 
         {
             Double result = 0.0;
             System.out.println("Tổng chi tiêu của người dùng ");
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month);
                 if(ac==null){
                     System.out.println(" không tim thay user");
                     return result;
                 }
                 return ac.getTotal_spend();
                     
             
         }
      @Override
      public double GetTotalSaving(int userid,int month)
      {
             Double result = 0.0;
                System.out.println("Tổng tiết kiệm của người dùng ");
                AccountBalance ac = acc.findByUserIdAndMonth(userid, month);
                 if(ac==null){
                     System.out.println(" không tim thay user");
                     return result;
                 }
                 return ac.getTotal_remain_save();
                   
            }
      
   @Override
         public double GetTotalInvest_Property(int userid,int month) 
         {
             Double result = 0.0;
             System.out.println("Tổng giá trị tài sản đầu tư và tích trữ của người dùng ");
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month);
                 if(ac==null){
                     System.out.println(" không tim thay user");
                     return result;
                 }
                 return ac.getTotal_invest_property();
                     
             
         }
     @Override
         public double GetTotalLoanRemain(int userid,int month) 
         {
             Double result = 0.0;
             System.out.println("Tổng dư nợ của người dùng ");
            
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month);
                 if(ac==null){
                     System.out.println(" không tim thay user");
                     return result;
                 }
                 return ac.getTotal_loan_remain();
                     
          
         }
        @Override
         public double GetTotalInvest(int userid,int month) 
         {
             Double result = 0.0;
             System.out.println("Tổng vốn đầu tư của người dùng ");
   
                 AccountBalance ac = acc.findByUserIdAndMonth(userid, month);
                 if(ac==null){
                     System.out.println(" không tim thay user");
                     return result;
                 }
                 return ac.getTotal_invest();
         } 
     @Override
     public double NetAsset(int userid, int month){
         Double result = 0.0;
         System.out.println("Tổng tài sản sau khi khấu trừ dư nợ");
             AccountBalance ac = acc.findByUserIdAndMonth(userid, month);
             if(ac==null)
             {
                 System.out.println(" không tìm thấy user ");
                 return result;
             }
             result = ac.getBalance()- ac.getTotal_loan_remain();
             return result;
             
         }
         
     
@Override
public boolean addTransaction(Transaction transaction) {
    System.out.println("Thêm giao dịch mới...");
    try {
        boolean result = transactionDao.addTransaction(transaction);
        return result;
    } catch (Exception e) {
        System.err.println("Lỗi Service khi thêm giao dịch: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
@Override
public List<Transaction> GetInvestmentTransaction(int userId, int month) {
    System.out.println("lấy giao dịch đầu tư cho user " + userId + " tháng " + month);
    try {
        return transactionDao.GetInvestmentTransaction(userId, month);
    } catch (Exception e) {
        System.err.println("Lỗi Service khi lấy giao dịch đầu tư: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }
}

@Override
public List<Transaction> GetSpendingFromIncome(int userId, int month) {
    System.out.println("Lấy giao dịch chi từ thu nhập cho user " + userId + " tháng " + month);
    try {
        return transactionDao.GetSpendingFromIncome(userId, month);
    } catch (Exception e) {
        System.err.println("Lỗi Service khi lấy giao dịch chi từ thu nhập: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>(); 
    }
}

@Override
public List<SpendingCategory> GetSpendingTotalType(int userId, int month) {
    System.out.println("Service: Lấy tổng chi theo loại cho user " + userId + " tháng " + month);
     try {
        return transactionDao.GetSpendingTotalType(userId, month);
    } catch (Exception e) {
        System.err.println("Lỗi Service khi lấy tổng chi theo loại: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();
    }
}
     @Override
     public List<Loan> getLoanDetails(int userId) {
        System.out.println("Lấy chi tiết các khoản vay cho user " + userId);
        try {
            return loanDao.GetIdUserLoan(userId); 
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy chi tiết khoản vay: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    @Override
     public List<Transaction> getBuyInvestmentTransactions(int userId, int month) {
         System.out.println("Lấy giao dịch MUA đầu tư cho user " + userId + " tháng " + month);
         try {
             
             return transactionDao.getBuyInvestmentTransactions(userId, month);
         } catch (Exception e) {
             System.err.println("Lỗi khi lấy giao dịch MUA đầu tư: " + e.getMessage());
             e.printStackTrace();
             return new ArrayList<>();
         }
     }

     @Override
     public List<Transaction> getSellInvestmentTransactions(int userId, int month) {
         System.out.println("Lấy giao dịch BÁN đầu tư cho user " + userId + " tháng " + month);
         try {
         
             return transactionDao.getSellInvestmentTransactions(userId, month);
         } catch (Exception e) {
             System.err.println("Lỗi khi lấy giao dịch BÁN đầu tư: " + e.getMessage());
             e.printStackTrace();
             return new ArrayList<>();
         }
     }
     @Override
    public FinancialPlan getFinancialPlanData(int userId) {
         System.out.println("Lấy dữ liệu Financial Plan cho user " + userId);
         FinancialPlan plan = null;
         try {
            
            plan = financialPlanDao.getFinancialPlan(userId);
         } catch (Exception e) {
             System.err.println("Lỗi khi lấy Financial Plan data: " + e.getMessage());
             e.printStackTrace();
         }
         return plan; 
    }
    @Override
    public double getCumulativePnL(int userId) {
        FinancialPlan plan = getFinancialPlanData(userId);
        return (plan != null) ? plan.getCurCumulativePnl() : 0.0; 
    }

  
    public List<InvestStorage> getCurrentInvestmentDetails(int userId) {
        System.out.println("lấy chi tiết đầu tư đang nắm giữ cho user " + userId);
         try {
            return investStorageDao.getCurrentInvestments(userId);
         } catch (Exception e) {
             System.err.println("Lỗi khi lấy chi tiết đầu tư đang nắm giữ: " + e.getMessage());
             e.printStackTrace();
             return new ArrayList<>();
         }
    }
    @Override
    public double getTotalEstimatedInvestmentProfit(int userId) {
        System.out.println("Lấy tổng lợi nhuận ước tính đầu tư cho user " + userId);
        try {
            return investStorageDao.getTotalEstimatedProfit(userId);
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy tổng lợi nhuận ước tính đầu tư: " + e.getMessage());
             e.printStackTrace();
            return 0.0;
        }
    }

       @Override
     public List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month) {
         System.out.println("Service: Lấy GD chi tiêu theo nguồn thu nhập cho user " + userId + " tháng " + month);
         try {
             return transactionDao.getSpendingTransactionsGroupedByIncome(userId, month);
         } catch (Exception e) {
             System.err.println(" Lỗi khi lấy GD chi tiêu theo nguồn thu nhập: " + e.getMessage());
             e.printStackTrace();
             return new ArrayList<>();
         }
     }
     @Override
     public List<SpendingCategory> getSpendingStatisticsWithRateFromDB(int userId, int month) {
         System.out.println(" thống kê chi tiêu theo loại (dùng rate từ DB) cho user " + userId + " tháng " + month);
          try {
   
              List<SpendingCategory> categories = transactionDao.GetSpendingTotalType(userId, month);
            
              return categories;
          } catch (Exception e) {
              System.err.println("Lỗi Service khi lấy tổng chi theo loại: " + e.getMessage());
              e.printStackTrace();
              return new ArrayList<>();
          }

     }
}


  

     


