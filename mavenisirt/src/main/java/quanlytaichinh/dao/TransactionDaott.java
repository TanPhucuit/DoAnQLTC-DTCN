/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quanlytaichinh.dao;
import java.util.List;
import java.util.ArrayList;
import quanlytaichinh.ketnoidb;
import java.sql.*;
import quanlytaichinh.model.SpendingCategory;
import quanlytaichinh.model.Transaction;
import quanlytaichinh.model.SpendingByIncomeSource;

/**
 *
 * @author 23520
 */
public class TransactionDaott implements TransactionDao{
    @Override
    public List<SpendingCategory> GetSpendingTotalType(int id, int month)
    {
        List<SpendingCategory> resultL = new ArrayList<>();
        
    String sql = "SELECT t.TypeID, ty.type_description, ty.trans_type_rate, SUM(t.trans_amount) AS totalAmount FROM TRANSACTION t JOIN TYPE ty ON t.TypeID = ty.TypeID AND t.UserID = ty.UserID WHERE t.UserID = ? AND MONTH(t.trans_date) = ? AND t.TypeID LIKE 'SP_%' GROUP BY t.TypeID, ty.type_description, ty.trans_type_rate ORDER BY totalAmount DESC";
    try (Connection ketnoi = ketnoidb.getConnection();
            PreparedStatement save = ketnoi.prepareStatement(sql)){
            save.setInt(1,id);
            save.setInt(2, month);
            ResultSet result = save.executeQuery();
             while (result.next()) {
                    String typeId = result.getString("TypeID");
                    String description = result.getString("type_description");
                    double rate = result.getDouble("trans_type_rate");
                    if (result.wasNull()) {
                        rate = 0.0;
                    }
                    double total = result.getDouble("totalAmount");

                    SpendingCategory sum = new SpendingCategory(typeId, description, total, rate);
                    resultL.add(sum);
                }
    }
        catch(SQLException e)
        {
            System.err.println(" Có lỗi xảy ra khi lấy tổng chi tiêu theo loại");
            
        }
    return resultL; 
    }
    
    @Override
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO TRANSACTION (TypeID, UserID, trans_amount, trans_date, " +
                     "sold_num_unit, sold_profit, LoanID, InStID, IncomeID, SaveID, OverPayFeeID) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transaction.getTypeId());
            pstmt.setInt(2, transaction.getUserId());
            pstmt.setDouble(3, transaction.getTransAmount());
            pstmt.setDate(4, transaction.getTransDate() == null ? null : new java.sql.Date(transaction.getTransDate().getTime()));
            pstmt.setDouble(5, transaction.getSoldNumUnit());
            pstmt.setDouble(6, transaction.getSoldProfit());

            if (transaction.getLoanId() != null) { pstmt.setInt(7, transaction.getLoanId()); } else { pstmt.setNull(7, Types.INTEGER); }
            if (transaction.getInStId() == null) { pstmt.setNull(8, Types.VARCHAR); } else { pstmt.setString(8, transaction.getInStId()); }
            if (transaction.getIncomeId() != null) { pstmt.setInt(9, transaction.getIncomeId()); } else { pstmt.setNull(9, Types.INTEGER); }
            if (transaction.getSaveId() != null) { pstmt.setInt(10, transaction.getSaveId()); } else { pstmt.setNull(10, Types.INTEGER); }
            if (transaction.getOverPayFeeId() != null) { pstmt.setInt(11, transaction.getOverPayFeeId()); } else { pstmt.setNull(11, Types.INTEGER); }

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm giao dịch: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> GetInvestmentTransaction(int userId, int month) { // Đổi tên khớp interface
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND MONTH(trans_date) = ? " +
                     "AND TypeID IN ('InSt_Buy', 'InSt_Sell') " + // Dùng IN cho gọn
                     "ORDER BY trans_date DESC";

        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapRowToTransaction(rs);
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy giao dịch đầu tư: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<Transaction> GetSpendingFromIncome(int userId, int month) { 
         List<Transaction> transactions = new ArrayList<>();
         String sql = "SELECT t.*, ic.income_name " + 
                      "FROM TRANSACTION t " +
                      "JOIN INCOME ic ON t.IncomeID = ic.IncomeID AND t.UserID = ic.UserID " + 
                      "WHERE t.UserID = ? AND MONTH(t.trans_date) = ? AND t.IncomeID IS NOT NULL " +
                      "ORDER BY t.trans_date DESC";

        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);

             try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = mapRowToTransaction(rs);
                    transaction.setIncomeName(rs.getString("income_name"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy giao dịch chi tiêu từ thu nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransId(rs.getInt("TransID"));
        transaction.setTypeId(rs.getString("TypeID"));
        transaction.setUserId(rs.getInt("UserID"));
        transaction.setTransAmount(rs.getDouble("trans_amount"));
        transaction.setTransDate(rs.getDate("trans_date"));
        transaction.setSoldNumUnit(rs.getDouble("sold_num_unit"));
        transaction.setSoldProfit(rs.getDouble("sold_profit"));

        int loanId = rs.getInt("LoanID");
        transaction.setLoanId(rs.wasNull() ? null : loanId);
        transaction.setInStId(rs.getString("InStID"));
        int incomeId = rs.getInt("IncomeID");
        transaction.setIncomeId(rs.wasNull() ? null : incomeId);
        int saveId = rs.getInt("SaveID");
        transaction.setSaveId(rs.wasNull() ? null : saveId);
        int overPayFeeId = rs.getInt("OverPayFeeID");
        transaction.setOverPayFeeId(rs.wasNull() ? null : overPayFeeId);

        return transaction;
    }
    @Override
    public List<Transaction> getBuyInvestmentTransactions(int userId, int month) {
        System.out.println("DAO: Lấy giao dịch MUA đầu tư cho user " + userId + " tháng " + month);
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND TypeID = 'InSt_Buy' " +
                     "AND MONTH(trans_date) = ? AND YEAR(trans_date) = YEAR(CURRENT_DATE()) ORDER BY trans_date";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, month);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapRowToTransaction(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Lỗi khi lấy giao dịch MUA đầu tư: " + e.getMessage());
            e.printStackTrace(); 
            return new ArrayList<>(); 
        }
        return transactions;
    }

    @Override
    public List<Transaction> getSellInvestmentTransactions(int userId, int month) {
         System.out.println("Lấy giao dịch BÁN đầu tư cho user " + userId + " tháng " + month);
         List<Transaction> transactions = new ArrayList<>();
         String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND TypeID = 'InSt_Sell' " +
                      "AND MONTH(trans_date) = ? AND YEAR(trans_date) = YEAR(CURRENT_DATE()) ORDER BY trans_date";
         try (Connection conn = ketnoidb.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setInt(1, userId);
             pstmt.setInt(2, month);
             try (ResultSet rs = pstmt.executeQuery()) {
                 while (rs.next()) {
                     transactions.add(mapRowToTransaction(rs));
                 }
             }
         } catch (SQLException e) {
             System.err.println("DAO Lỗi khi lấy giao dịch BÁN đầu tư: " + e.getMessage());
             e.printStackTrace(); 
             return new ArrayList<>(); 
         }
         return transactions;
    }



    @Override
    public List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month) {
        System.out.println("lấy GD chi tiêu theo nguồn thu nhập (Simplified) cho user " + userId + " tháng " + month);
        List<SpendingByIncomeSource> results = new ArrayList<>();
        List<Transaction> currentGroupTransactions = null; 
        String currentIncomeName = null;
        int currentIncomeId = -1;

        String spendingTypeIdPattern = "SP_%"; 

        
        String sql = "SELECT t.*, i.income_name " +
                     "FROM TRANSACTION t JOIN INCOME i ON t.IncomeID = i.IncomeID " +
                     "WHERE t.UserID = ? AND t.TypeID LIKE ? " + 
                     "AND t.IncomeID IS NOT NULL " +
                     "AND MONTH(t.trans_date) = ? AND YEAR(t.trans_date) = YEAR(CURRENT_DATE()) " +
                     "ORDER BY i.IncomeID, t.trans_date"; 
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, spendingTypeIdPattern); 
            pstmt.setInt(3, month);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int incomeId = rs.getInt("IncomeID");
                    String incomeName = rs.getString("income_name");
                    Transaction transaction = mapRowToTransaction(rs);                
                    if (currentIncomeId != incomeId) {
                        if (currentIncomeId != -1 && currentGroupTransactions != null) {
                            results.add(new SpendingByIncomeSource(currentIncomeId, currentIncomeName, new ArrayList<>(currentGroupTransactions)));
                        }
                        
                        currentIncomeId = incomeId;
                        currentIncomeName = incomeName;
                        currentGroupTransactions = new ArrayList<>(); 
                    }
                    if(currentGroupTransactions != null) {
                       currentGroupTransactions.add(transaction);
                    }
                }
          
                if (currentIncomeId != -1 && currentGroupTransactions != null && !currentGroupTransactions.isEmpty()) {
                     results.add(new SpendingByIncomeSource(currentIncomeId, currentIncomeName, new ArrayList<>(currentGroupTransactions)));
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Lỗi khi lấy giao dịch chi tiêu theo nguồn thu nhập: " + e.getMessage());
            e.printStackTrace(); 
            return new ArrayList<>();
        }

        return results;
    }

}
    

