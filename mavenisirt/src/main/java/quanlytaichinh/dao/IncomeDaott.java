/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.dao;
    import java.sql.*;
import quanlytaichinh.model.InCome;
import quanlytaichinh.ketnoidb;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author 23520
 */
public class IncomeDaott implements IncomeDao {
    public IncomeDaott(){};
    
    
    @Override
    public boolean AddIncome (InCome ic){
        String sql ="INSERT INTO INCOME (UserID,ic_month,income_name,income_amount)VALUES (?,?,?,?)";
        try(Connection ketnoi = ketnoidb.getConnection();
                PreparedStatement save = ketnoi.prepareStatement(sql) ){
            save.setInt(1,ic.getIncomeId());
            save.setString(2,ic.getIc_month());
            save.setString(3,ic.getIncome_name());
            save.setDouble(4, ic.getIncome_amount());
            int rowsinserted = save.executeUpdate();
            if(rowsinserted > 0)
                return true;
            
            
        }
        catch (SQLException e)
        {
            System.err.println("lỗi thêm income");
            return false;
        }
        return false;
    }
    
    @Override 
    public double TotalIncomePerMonth(int id, int month)
    {
        double total = 0.0;
        String sql = " SELECT SUM(income_amount) AS ToTalInCome FROM income where UserId =? AND ic_month =?";
        try(Connection ketnoi = ketnoidb.getConnection();
                PreparedStatement save = ketnoi.prepareStatement(sql)){
            save.setInt(1,id);
            save.setString(2,String.valueOf(month));
            try(ResultSet result = save.executeQuery()){
                if(result.next())
                    total = result.getDouble("ToTalInCome");
            }
        }
        catch(SQLException e)
        {
            System.err.println("lỗi xảy ra, không thể tính được tổng tài sản");
            e.printStackTrace();
        }
        return total;
    }
   
}
