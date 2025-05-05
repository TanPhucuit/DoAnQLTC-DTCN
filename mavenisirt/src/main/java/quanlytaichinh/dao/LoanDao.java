/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package quanlytaichinh.dao;
import java.util.List;
import quanlytaichinh.model.Loan;
/**
 *
 * @author 23520
 */
public interface LoanDao {
    List<Loan> GetIdUserLoan(int userid);
    
}
