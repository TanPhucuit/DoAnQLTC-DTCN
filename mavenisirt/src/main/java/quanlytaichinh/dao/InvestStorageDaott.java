/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.dao;
import quanlytaichinh.model.InvestStorage;
import quanlytaichinh.ketnoidb;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author 23520
 */
public class InvestStorageDaott implements InvestStorageDao {
    @Override
    public List<InvestStorage> getCurrentInvestments(int userId) {
        List<InvestStorage> investments = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_STORAGE WHERE UserID = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement save = conn.prepareStatement(sql)) {
            save.setInt(1, userId);
            try (ResultSet rs = save.executeQuery()) {
                while (rs.next()) {
                    InvestStorage storage = new InvestStorage();
                    storage.setInStId(rs.getString("InStID"));
                    storage.setUserId(rs.getInt("UserID"));
                    storage.setNumUnit(rs.getDouble("num_unit"));
                    storage.setBuyPrice(rs.getDouble("buy_price"));
                    storage.setEsProfit(rs.getDouble("es_profit"));
                    storage.setUpDate(rs.getDate("up_date"));
                    investments.add(storage);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách đầu tư: " + e.getMessage());
            e.printStackTrace();
        }
        return investments;
    }

    @Override
    public double getTotalEstimatedProfit(int userId) {
        double totalProfit = 0.0;
        String sql = "SELECT SUM(es_profit) AS total_es_profit FROM INVEST_STORAGE WHERE UserID = ?";
        try (Connection ketnoi= ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {
            save.setInt(1, userId);
            try (ResultSet result = save.executeQuery()) {
                if (result.next()) {
                    totalProfit = result.getDouble("total_es_profit");
                   
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tính tổng lợi nhuận ước tính: " + e.getMessage());
            e.printStackTrace();
        }
        return totalProfit;
    }
}

