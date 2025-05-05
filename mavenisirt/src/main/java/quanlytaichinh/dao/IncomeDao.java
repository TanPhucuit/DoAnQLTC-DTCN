/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.dao;
import quanlytaichinh.model.*;
import java.util.List;
import quanlytaichinh.model.InCome;
/**
 *
 * @author 23520
 */
public interface IncomeDao {
    boolean AddIncome(InCome ic);
   
    InCome getIncomeById(int incomeId);
    List<InCome> getIncomesByMonthAndYear(int userId, int month);
    double GetTotalFirstIncome (int userid, int month);

}
