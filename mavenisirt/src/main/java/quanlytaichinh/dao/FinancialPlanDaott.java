/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.dao;
import quanlytaichinh.model.FinancialPlan;
import quanlytaichinh.ketnoidb;
import java.sql.*;
/**
 *
 * @author 23520
 */
public class FinancialPlanDaott implements FinancialPlanDao{
     @Override
    public FinancialPlan getFinancialPlan(int userId) {
        FinancialPlan plan = null;
        String sql = "SELECT * FROM FINANCIAL_PLAN WHERE UserID = ? LIMIT 1";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    plan = new FinancialPlan();
                    plan.setUserId(rs.getInt("UserID"));
                    plan.setInvestorType(rs.getString("Investor_type"));
                    plan.setCurProperty(rs.getDouble("cur_property"));
                    plan.setCurInvestProperty(rs.getDouble("cur_invest_property"));
                    plan.setCurEsProfit(rs.getDouble("cur_es_profit"));
                    plan.setCurCumulativePnl(rs.getDouble("cur_cumulative_pnl"));
                    plan.setWarningLossRate(rs.getDouble("warning_loss_rate"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy Financial Plan: " + e.getMessage());
            e.printStackTrace();
        }
        return plan;
    }
}

