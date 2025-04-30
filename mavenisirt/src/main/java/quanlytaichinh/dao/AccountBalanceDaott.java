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
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    accountBalance = new AccountBalance();
                    accountBalance.setUserId(rs.getInt("UserID"));
                    accountBalance.setAccountBalanceID(rs.getString("AccountBalanceID"));
                    accountBalance.setAb_month(rs.getString("ab_month")); 
                    accountBalance.setTotal_remain_income(rs.getDouble("total_remain_income"));
                    accountBalance.setTotal_remain_save(rs.getDouble("total_remain_save"));
                    accountBalance.setRemain_invest(rs.getDouble("remain_invest"));
                    accountBalance.setTotal_loan_remain(rs.getDouble("total_loan_remain"));
                    accountBalance.setTotal_spend(rs.getDouble("total_spend"));
                    accountBalance.setTotal_invest(rs.getDouble("total_invest"));
                    accountBalance.setTotal_invest_property(rs.getDouble("total_invest_property"));
                    accountBalance.setBalance(rs.getDouble("balance"));
                    accountBalance.setUp_date(rs.getDate("up_date")); 
                }
            } 
        } catch (SQLException e) { 
            System.err.println("Lỗi khi thực thi findLatestByUserId cho UserID '" + userId + "': " + e.getMessage());
            e.printStackTrace(); 
         
        }
        return accountBalance;
       
}
    @Override
public AccountBalance findByUserIdAndMonth(int userId, int month) {
    AccountBalance accountBalance = null;
    String sql = "SELECT * FROM ACCOUNT_BALANCE WHERE UserID = ? AND ab_month = ?";
    try (Connection conn = ketnoidb.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, userId);
        pstmt.setString(2, String.valueOf(month));
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                accountBalance = new AccountBalance();
                accountBalance.setUserId(rs.getInt("UserID"));
                accountBalance.setAccountBalanceID(rs.getString("AccountBalanceID"));
                accountBalance.setAb_month(rs.getString("ab_month"));
                accountBalance.setTotal_remain_income(rs.getDouble("total_remain_income"));
                accountBalance.setTotal_remain_save(rs.getDouble("total_remain_save"));
                accountBalance.setRemain_invest(rs.getDouble("remain_invest"));
                accountBalance.setTotal_loan_remain(rs.getDouble("total_loan_remain"));
                accountBalance.setTotal_spend(rs.getDouble("total_spend"));
                accountBalance.setTotal_invest(rs.getDouble("total_invest"));
                accountBalance.setTotal_invest_property(rs.getDouble("total_invest_property"));
                accountBalance.setBalance(rs.getDouble("balance"));
     
                accountBalance.setUp_date(rs.getDate("up_date"));
            }
        }
    } catch (SQLException e) {
        System.err.println("Lỗi khi thực thi findByUserIdAndMonth cho UserID '" + userId + "' và tháng '" + month + "': " + e.getMessage());
        e.printStackTrace();
    } 
    return accountBalance;
}
}
