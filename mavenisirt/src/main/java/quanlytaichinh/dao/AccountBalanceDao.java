/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.dao;
import quanlytaichinh.model.AccountBalance;
/**
 *
 * @author 23520
 */
public interface AccountBalanceDao {
    AccountBalance FindLatest(int userid);
    AccountBalance findByUserIdAndMonth(int userId, int month,int year);
    double getTotalSpendingForMonth(int userId, int month, int year);
    double getCurrentAssets(int userId, int month, int year);
}
