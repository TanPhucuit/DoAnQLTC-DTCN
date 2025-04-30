/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.dao;
import quanlytaichinh.model.InvestStorage;
import java.util.List;
/**
 *
 * @author 23520
 */
public interface InvestStorageDao {
    List<InvestStorage> getCurrentInvestments(int userId);
    double getTotalEstimatedProfit(int userId);
  
}
