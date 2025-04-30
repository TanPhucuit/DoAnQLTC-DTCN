/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.dao;
import java.util.List;
import quanlytaichinh.model.InCome;
/**
 *
 * @author 23520
 */
public interface IncomeDao {
    boolean AddIncome(InCome ic);
    // tổng tài sản hiện có trên tháng
    double TotalIncomePerMonth(int userid,int month);
    
}
