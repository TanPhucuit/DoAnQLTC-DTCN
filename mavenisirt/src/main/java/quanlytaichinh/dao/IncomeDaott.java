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
            save.setInt(1,ic.getUserId());
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
  
    public InCome getIncomeById(int incomeId) {
        InCome income = null;
        
        String sql = "SELECT IncomeID, UserID, ic_month, income_name, income_amount, remain_income FROM INCOME WHERE IncomeID = ?";
        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {

            save.setInt(1, incomeId);
            try (ResultSet rs = save.executeQuery()) {
                if (rs.next()) {
                    income = mapResultSetToInCome(rs);
                }
            }
        } catch (SQLException e) {
             System.err.println("Lỗi khi lấy Income theo ID '" + incomeId + "': " + e.getMessage());
            e.printStackTrace();
        }
        return income;
    }

     @Override
     public List<InCome> getIncomesByMonthAndYear(int userId, int month) {
        List<InCome> incomeList = new ArrayList<>();
        String sql = "SELECT IncomeID, UserID, ic_month, income_name, income_amount, remain_income FROM INCOME WHERE UserID = ? AND ic_month = ?";
        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {
            save.setInt(1, userId);
            save.setString(2, String.valueOf(month));
            try (ResultSet rs = save.executeQuery()) {
                while (rs.next()) {
                    incomeList.add(mapResultSetToInCome(rs));
                }
            }
        } catch (SQLException e) {
             System.err.println("Lỗi khi lấy danh sách Income theo tháng '" + month + "' cho UserID '" + userId + "': " + e.getMessage());
            e.printStackTrace();
        }
        return incomeList;
     }
    private InCome mapResultSetToInCome(ResultSet rs) throws SQLException {
        InCome income = new InCome();
        income.setIncomeId(rs.getInt("IncomeID"));
        income.setUserId(rs.getInt("UserID"));
        income.setIc_month(rs.getString("ic_month"));
        income.setIncome_name(rs.getString("income_name"));
        income.setIncome_amount(rs.getDouble("income_amount"));
        income.setRemain_income(rs.getDouble("remain_income"));
        return income;
    }
    @Override
    public double GetTotalFirstIncome( int userid,int month) {
    	double total = 0.0;
    		 String sql = "Select SUM(income_amount)as TotalFirstIncomefrom INCOME where userid = ? AND ic_month= ?";
    		 try(Connection ketnoi = ketnoidb.getConnection();
    				 PreparedStatement save = ketnoi.prepareStatement(sql)){
    			 save.setInt(1, userid);
    			 save.setString(2,String.valueOf(month));
    			 try (ResultSet result = save.executeQuery()){
    				 if(result.next() )
    					 total = result.getDouble("TotalFirstIncome");
    				 else 
    					 total = 0.0;
    		 
   
    		 }
    		 }
    		 catch(SQLException e) {
    			 System.err.println("lỗi không thể lấy income ban đầu");
    			 e.printStackTrace();
    		 }
    	return total;	
    }
   
}


