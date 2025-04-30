/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.dao;

import quanlytaichinh.model.Sysuser;
import quanlytaichinh.ketnoidb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
/**
 *
 * @author 23520
 */
public class SysuserDaott implements SysuserDao {
    @Override 
    public Sysuser FindUser(String username){
        Sysuser user = null;
        String sql = "SELECT userID,user_name,password from SYS_USER where user_name = ?"; 
        try (Connection ketnoi = ketnoidb.getConnection();
                PreparedStatement save = ketnoi.prepareStatement(sql)){
                save.setString(1,username);
                try(ResultSet rs = save.executeQuery()){
                    if(rs.next()){
                        user = new Sysuser();
                        user.setUserid(rs.getInt("userID"));
                        user.setUsername(rs.getString("user_name"));
                        user.setPassword(rs.getString("password"));
                    }
                }   
          }
              catch(SQLException e){
           System.err.println("Không tìm thấy user"+ username+ e.getMessage()); 
           e.printStackTrace();
        }
        return user;
    }
    
    public boolean AddUser(Sysuser name)
    {
        boolean Status = false;
        String sql = "INSERT INTO SYS_USER(user_name,password) values(?,?)  ";
        try(Connection ketnoi = ketnoidb.getConnection();
                PreparedStatement save = ketnoi.prepareStatement(sql)){
                save.setString(1,name.getUsername());
                save.setString(2,name.getPassword());
                int rowsAffected = save.executeUpdate();
                if (rowsAffected > 0)
                    Status = true;
                
        }
        catch(SQLException e)
        {
            System.err.println("Không thể insert" + e.getMessage());
        }
        return Status;
    }
   @Override
    public List<Sysuser> findallUser() { 
        List<Sysuser> userList = new ArrayList<>(); 
        String sql = "SELECT UserID, user_name, password FROM SYS_USER"; 

        try (Connection conn = ketnoidb.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) { 
            while (rs.next()) { 
                Sysuser user = new Sysuser(); 
                user.setUserid(rs.getInt("UserID"));
                user.setUsername(rs.getString("user_name"));
                user.setPassword(rs.getString("password"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực thi findUser (findAll): " + e.getMessage());
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean UpdateUser(Sysuser user) { 
        boolean status = false;
        String sql = "UPDATE SYS_USER SET user_name = ?, password = ? WHERE UserID = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername()); 
            pstmt.setString(2, user.getPassword()); 
            pstmt.setInt(3, user.getUserid()); 

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) { 
                status = true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi thực thi UpdateUser: " + e.getMessage());
            e.printStackTrace();
        }
        return status; 
    }
   @Override
    public boolean DeleteUser(int userid) { 
        boolean status = false;
        String sql = "DELETE FROM SYS_USER WHERE UserID = ?";

        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userid);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                status = true;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi thực thi DeleteUser: " + e.getMessage());
            e.printStackTrace();
        }
        return status; 
    }
    @Override
    public Sysuser findIdUser(int userId) {
        Sysuser user = null;
        String sql = "SELECT UserID, user_name, password FROM SYS_USER WHERE UserID = ?"; 

        try (Connection conn =ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new Sysuser();
                    user.setUserid(rs.getInt("UserID"));
                    user.setUsername(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực thi findById cho UserID '" + userId + "': " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }
}