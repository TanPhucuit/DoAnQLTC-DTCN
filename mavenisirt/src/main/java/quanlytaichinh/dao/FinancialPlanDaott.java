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
        String sql = "SELECT * FROM FINANCIAL_PLAN WHERE UserID = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet result= pstmt.executeQuery()) {
                if (result.next()) {
                    plan = new FinancialPlan();
                    plan.setUserId(result.getInt("UserID"));
                    plan.setInvestorType(result.getString("Investor_type"));
                    plan.setCurProperty(result.getDouble("cur_property"));
                    plan.setCurInvestProperty(result.getDouble("cur_invest_property"));
                    plan.setCurEsProfit(result.getDouble("cur_es_profit"));
                    plan.setCurCumulativePnl(result.getDouble("cur_cumulative_pnl"));
                    plan.setWarningLossRate(result.getDouble("warning_loss_rate"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy Financial Plan: " + e.getMessage());
        }
        return plan;
    }
}

