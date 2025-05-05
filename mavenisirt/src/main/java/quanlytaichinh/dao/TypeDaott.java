/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.dao;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import quanlytaichinh.model.Type;
import quanlytaichinh.ketnoidb;
/**
 *
 * @author 23520
 */

public class TypeDaott implements TypeDao {
	private Type mapResultSetToType(ResultSet result) throws SQLException {
        Type type = new Type();
        type.setTypeId(result.getString("TypeID"));
        type.setUserId(result.getInt("UserID"));
        type.setMaxAmount(result.getDouble("max_amount"));
        type.setTypeDescription(result.getString("type_description"));
        type.setTransTypeRate(result.getDouble("trans_type_rate")); 
        return type;
    }
    @Override
     public List<Type> getTypesByUserId(int userId){
    	List<Type> typeList = new ArrayList<>();
    	 String sql = "SELECT TypeID, UserID, max_amount, type_description, trans_type_rate FROM TYPE WHERE UserID = ?";
    	 try (Connection ketnoi = ketnoidb.getConnection();
                 PreparedStatement save = ketnoi.prepareStatement(sql)) {

                save.setInt(1, userId);

                try (ResultSet result = save.executeQuery()) {
                    while (result.next()) {
                        Type type = mapResultSetToType(result); 
                        typeList.add(type);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi lấy danh sách Type cho UserID " + userId + ": " + e.getMessage());
                e.printStackTrace();
            }
            return typeList;
        }
         
     
}
