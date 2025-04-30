package tesst;

// Import lớp ketnoidb từ package quanlytaichinh
import quanlytaichinh.ketnoidb;

// Import các lớp SQL cần thiết
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class testt {

    public static void main(String[] args) {
        System.out.println("--- Starting connection test from testt.java ---");

        // Gọi phương thức getConnection từ lớp ketnoidb
        // Dùng try-with-resources để đảm bảo connection được đóng tự động
        try (Connection connection = ketnoidb.getConnection()) {

            // Nếu không có lỗi SQLException nào được ném ra ở trên, tức là kết nối thành công
            System.out.println(">>> Connection successful! <<<");

            // (Optional) Thử chạy một câu lệnh SQL đơn giản để kiểm tra thêm
            System.out.println("Performing a simple test query...");
            String sql = "SELECT DATABASE() AS db_name"; // Lấy tên database hiện tại
            try (PreparedStatement pstmt = connection.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                 if (rs.next()) {
                     System.out.println("Connected to database: " + rs.getString("db_name"));
                 } else {
                      System.out.println("Query executed, but no result returned.");
                 }
            } // rs và pstmt được đóng tự động

        } catch (SQLException e) {
            System.err.println(">>> Connection or Query failed! See error below: <<<");
            // Kiểm tra các nguyên nhân phổ biến
            if (e.getMessage().contains("matkhau")) {
                 System.err.println(">>> KIỂM TRA LẠI: Biến môi trường 'matkhau' đã được đặt đúng giá trị chưa?");
            } else if (e.getMessage().contains("Access denied")) {
                 System.err.println(">>> KIỂM TRA LẠI: Username ('quanlyapp') hoặc mật khẩu có đúng không? User đã được cấp quyền chưa?");
            } else if (e.getMessage().contains("Communications link failure") || e.getMessage().contains("Connection refused")) {
                 System.err.println(">>> KIỂM TRA LẠI: MySQL Server đã chạy chưa? Địa chỉ 'localhost' và cổng '3306' có đúng không?");
            }
            e.printStackTrace(); // In chi tiết lỗi ra console
        } catch (Exception e) { // Bắt các lỗi không mong muốn khác
            System.err.println(">>> An unexpected error occurred: <<<");
            e.printStackTrace();
        }

        System.out.println("--- Connection test finished ---");
    }
}