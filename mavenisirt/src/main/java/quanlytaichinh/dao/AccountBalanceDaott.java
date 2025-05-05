/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import quanlytaichinh.ketnoidb;
import quanlytaichinh.model.AccountBalance;

/**
 *
 * @author 23520
 */
public class AccountBalanceDaott implements AccountBalanceDao {
    
    public AccountBalanceDaott() {};

    @Override 
    
    public AccountBalance FindLatest(int userId) {
        AccountBalance accountBalance = null;
        String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE UserID = ? ORDER BY up_date DESC LIMIT 1";

        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement save = conn.prepareStatement(sql)) {

            save.setInt(1, userId);
            try (ResultSet result = save.executeQuery()) {
                if (result.next()) {
                    accountBalance = new AccountBalance();
                    accountBalance.setUserId(result.getInt("UserID"));
                    accountBalance.setAccountBalanceID(result.getString("AccountBalanceID"));
                    accountBalance.setAb_month(result.getString("ab_month")); 
                    accountBalance.setTotal_remain_income(result.getDouble("total_remain_income"));
                    accountBalance.setTotal_remain_save(result.getDouble("total_remain_save"));
                    accountBalance.setRemain_invest(result.getDouble("remain_invest"));
                    accountBalance.setTotal_loan_remain(result.getDouble("total_loan_remain"));
                    accountBalance.setTotal_spend(result.getDouble("total_spend"));
                    accountBalance.setTotal_invest(result.getDouble("total_invest"));
                    accountBalance.setTotal_invest_property(result.getDouble("total_invest_property"));
                    accountBalance.setBalance(result.getDouble("balance"));
                    accountBalance.setUp_date(result.getDate("up_date")); 
                }
            } 
        } catch (SQLException e) { 
            System.err.println("Lỗi khi tìm kiếm id cho UserID '" + userId + "': " + e.getMessage());
            e.printStackTrace(); 
         
        }
        return accountBalance;
       
}
    @Override
public AccountBalance findByUserIdAndMonth(int userId, int month,int year) {
    AccountBalance accountBalance = null;
    String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE UserID = ? AND ab_month = ? AND YEAR(up_date) = ?";
    try (Connection conn = ketnoidb.getConnection();
         PreparedStatement save = conn.prepareStatement(sql)) {

        save.setInt(1, userId);
        save.setString(2, String.valueOf(month));
        save.setInt(3,year);
        try (ResultSet result = save.executeQuery()) {
            if (result.next()) {
                accountBalance = new AccountBalance();
                accountBalance.setUserId(result.getInt("UserID"));
                accountBalance.setAccountBalanceID(result.getString("AccountBalanceID"));
                accountBalance.setAb_month(result.getString("ab_month"));
                accountBalance.setTotal_remain_income(result.getDouble("total_remain_income"));
                accountBalance.setTotal_remain_save(result.getDouble("total_remain_save"));
                accountBalance.setRemain_invest(result.getDouble("remain_invest"));
                accountBalance.setTotal_loan_remain(result.getDouble("total_loan_remain"));
                accountBalance.setTotal_spend(result.getDouble("total_spend"));
                accountBalance.setTotal_invest(result.getDouble("total_invest"));
                accountBalance.setTotal_invest_property(result.getDouble("total_invest_property"));
                accountBalance.setBalance(result.getDouble("balance"));
     
                accountBalance.setUp_date(result.getDate("up_date"));
            }
        }
    } catch (SQLException e) {
        System.err.println("Lỗi khi tìm kiếm ID cho UserID '" + userId + "' và tháng '" + month + "': " + e.getMessage());
        e.printStackTrace();
    } 
    return accountBalance;
}
@Override
   
    public double getCurrentAssets(int userId, int month, int year) {
        double currentAssets = 0.0;
      
        String sql = "SELECT total_remain_income, total_invest_property, total_remain_save FROM ACCOUNT_BALANCE WHERE UserID = ? AND ab_month = ? AND YEAR(up_date) = ?";
      
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement save = conn.prepareStatement(sql)) {

            save.setInt(1, userId);
            save.setString(2, String.valueOf(month));
            save.setInt(3, year);
            try (ResultSet result = save.executeQuery()) {
                if (result.next()) {
                    double income = result.getDouble("total_remain_income");
                    double investProp = result.getDouble("total_invest_property");
                    double temp = result.getDouble("total_remain_save");
                    currentAssets = income + investProp + temp; 
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tổng tài sản hiện tại cho UserID '" + userId + "', tháng '" + month + "', năm '" + year + "': " + e.getMessage());
        }
        return currentAssets;
    }
@Override
    
    public double getTotalSpendingForMonth(int userId, int month, int year) {
        double totalSpending = 0.0;
        String sql = "SELECT total_spend FROM ACCOUNT_BALANCE WHERE UserID = ? AND ab_month = ? AND YEAR(up_date) = ?";
                     

        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement save = conn.prepareStatement(sql)) {

            save.setInt(1, userId);
            save.setString(2, String.valueOf(month));
            save.setInt(3, year);
            try (ResultSet result = save.executeQuery()) {
                if (result.next()) {
                    totalSpending = result.getDouble("total_spend"); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy Tổng chi cho UserID '" + userId + "', tháng '" + month + "', năm '" + year + "': " + e.getMessage());
        }
        return totalSpending;
    }
    private AccountBalance mapResultSetToAccountBalance(ResultSet result) throws SQLException {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setUserId(result.getInt("UserID"));
        accountBalance.setAccountBalanceID(result.getString("AccountBalanceID"));
        accountBalance.setAb_month(result.getString("ab_month")); 
        accountBalance.setTotal_remain_income(result.getDouble("total_remain_income"));
        accountBalance.setTotal_remain_save(result.getDouble("total_remain_save"));
        accountBalance.setRemain_invest(result.getDouble("remain_invest"));
        accountBalance.setTotal_loan_remain(result.getDouble("total_loan_remain"));
        accountBalance.setTotal_spend(result.getDouble("total_spend"));
        accountBalance.setTotal_invest(result.getDouble("total_invest"));
        accountBalance.setTotal_invest_property(result.getDouble("total_invest_property"));
        accountBalance.setBalance(result.getDouble("balance"));
        accountBalance.setUp_date(result.getDate("up_date"));
        return accountBalance;
    }
}
