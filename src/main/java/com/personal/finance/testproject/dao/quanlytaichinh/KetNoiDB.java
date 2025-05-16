package com.personal.finance.testproject.dao.quanlytaichinh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KetNoiDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/0905?serverTimezone=Asia/Ho_Chi_Minh&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER_kn = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER_kn, DB_PASSWORD);
    }

    public static void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                System.err.println("Loi ket noi " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Dang ket noi voi " + USER_kn);
        try (Connection connection = KetNoiDB.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT CURDATE() AS today");
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("Ket noi thanh cong!");

            if (rs.next()) {
                System.out.println("Ngay hien tai trong DB: " + rs.getDate("today"));
            } else {
                System.out.println("Query executed, but no result returned.");
            }
        } catch (SQLException e) {
            System.err.println("Ket noi that bai");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Co loi xay ra trong qua trinh ket noi");
            e.printStackTrace();
        }
    }
} 