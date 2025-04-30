/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;      
import java.sql.SQLException;

public class ketnoidb {

   private static final String DB_URL = "jdbc:mysql://localhost:3306/QLY?serverTimezone=Asia/Ho_Chi_Minh&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER_kn = "quanlyapp"; 

private static final String DB_PASSWORD = System.getProperty("matkhau");

public static Connection getConnection() throws SQLException {
    if (DB_PASSWORD == null || DB_PASSWORD.isEmpty()) {
        System.err.println("Lỗi connection 'matkhau' chưa được thiết lập!");
        throw new SQLException("Lỗi cấu hình mật khẩu cơ sở dữ liệu).");
    }
    return DriverManager.getConnection(DB_URL, USER_kn, DB_PASSWORD);
}
    public static void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                System.err.println("Lỗi kết nối " + e.getMessage());
                e.printStackTrace();
            }
        }
    } 
    public static void main(String[] args) {
        System.out.println("Đang kết nối với " + USER_kn);
        try (Connection connection = ketnoidb.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT CURDATE() AS today"); 
             ResultSet rs = pstmt.executeQuery()) { 

            System.out.println("Kết nối thành công!");

            if (rs.next()) {
                System.out.println("Ngày hiện tại trong DB: " + rs.getDate("today"));
            } else {
                 System.out.println("Query executed, but no result returned.");
            }
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Có lỗi xảy ra trong quá trình kết nối");
            e.printStackTrace();
        }
    } 

} 