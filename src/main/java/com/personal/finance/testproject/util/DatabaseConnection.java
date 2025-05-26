package com.personal.finance.testproject.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection { // Đổi tên lớp cho rõ ràng (tùy chọn)
    // Thông tin kết nối Oracle
    // Thay đổi các giá trị này cho phù hợp với cấu hình Oracle của bạn
    private static final String URL_ORACLE = "jdbc:oracle:thin:@//localhost:1521/XE";
    private static final String USER_ORACLE = "SYSTEM";
    private static final String PASSWORD_ORACLE = "huyluu"; 

    private DatabaseConnection() {
  
    }

    public static Connection getConnection() throws SQLException {
        try {
            // 1. Nạp Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

     
            return DriverManager.getConnection(URL_ORACLE, USER_ORACLE, PASSWORD_ORACLE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
    }

    
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Kết nối tới Oracle Database thành công!");
                // Bạn có thể thực hiện thêm các thao tác với database ở đây
            } else {
                System.out.println("Không thể kết nối tới Oracle Database.");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Oracle Database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}