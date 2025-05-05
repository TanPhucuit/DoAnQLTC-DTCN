package quanlytaichinh.dao; // Hoặc package đúng của bạn

import quanlytaichinh.model.Transaction;
import quanlytaichinh.model.SpendingCategory;
import quanlytaichinh.model.SpendingByIncomeSource;
import quanlytaichinh.ketnoidb;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 23520
 */
public class TransactionDaott implements TransactionDao {

    @Override
    public boolean addTransaction(Transaction transaction) {
        String sql ="INSERT INTO TRANSACTION (TypeID, UserID, trans_amount, trans_date, sold_num_unit, sold_profit, LoanID, InStID, IncomeID, SaveID, OverPayFeeID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {

            save.setString(1, transaction.getTypeId());
            save.setInt(2, transaction.getUserId());
            save.setDouble(3, transaction.getTransAmount());
            if (transaction.getTransDate() != null) {
                save.setDate(4, new java.sql.Date(transaction.getTransDate().getTime()));
            } else {
                save.setNull(4, Types.DATE);
            }

            save.setDouble(5, transaction.getSoldNumUnit()); 
            save.setDouble(6, transaction.getSoldProfit());  
            if (transaction.getLoanId() != null) { save.setInt(7, transaction.getLoanId()); } else { save.setNull(7, Types.INTEGER); }
            if (transaction.getInStId() != null) { save.setString(8, transaction.getInStId()); } else { save.setNull(8, Types.VARCHAR); }
            if (transaction.getIncomeId() != null) { save.setInt(9, transaction.getIncomeId()); } else { save.setNull(9, Types.INTEGER); }
            if (transaction.getSaveId() != null) { save.setInt(10, transaction.getSaveId()); } else { save.setNull(10, Types.INTEGER); }
            if (transaction.getOverPayFeeId() != null) { save.setInt(11, transaction.getOverPayFeeId()); } else { save.setNull(11, Types.INTEGER); }

            int rowsAffected = save.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm Transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Transaction> GetInvestmentTransaction(int userId, int month, int year) { 
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND MONTH(trans_date) = ? AND YEAR(trans_date) = ? AND TypeID IN ('InSt_Buy', 'InSt_Sell') ORDER BY trans_date DESC";

        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {

            save.setInt(1, userId);
            save.setInt(2, month);
            save.setInt(3, year);     
            try (ResultSet result = save.executeQuery()) {
                while (result.next()) {
                    Transaction transaction = mapRowToTransaction(result);
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
    public List<Transaction> GetSpendingFromIncome(int userId, int month, int year) { 
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, ic.income_name " +
                     "FROM TRANSACTION t " +
                     "JOIN INCOME ic ON t.IncomeID = ic.IncomeID AND t.UserID = ic.UserID " +
                     "WHERE t.UserID = ? AND MONTH(t.trans_date) = ? AND YEAR(t.trans_date) = ? AND t.IncomeID IS NOT NULL " + 
                     "ORDER BY t.trans_date DESC";

        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {

            save.setInt(1, userId);
            save.setInt(2, month);
            save.setInt(3, year);

            
            try (ResultSet result = save.executeQuery()) {
                while (result.next()) {
                    Transaction transaction = mapRowToTransaction(result);
                    transaction.setIncomeName(result.getString("income_name"));
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy giao dịch chi tiêu từ thu nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

 
    private Transaction mapRowToTransaction(ResultSet result) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransId(result.getInt("TransID"));
        transaction.setTypeId(result.getString("TypeID"));
        transaction.setUserId(result.getInt("UserID"));
        transaction.setTransAmount(result.getDouble("trans_amount")); 
        transaction.setTransDate(result.getDate("trans_date"));
        transaction.setSoldNumUnit(result.getDouble("sold_num_unit")); 
        transaction.setSoldProfit(result.getDouble("sold_profit"));

        int loanId = result.getInt("LoanID");
        transaction.setLoanId(result.wasNull() ? null : loanId);
        transaction.setInStId(result.getString("InStID")); 
        int incomeId = result.getInt("IncomeID");
        transaction.setIncomeId(result.wasNull() ? null : incomeId);
        int saveId = result.getInt("SaveID");
        transaction.setSaveId(result.wasNull() ? null : saveId);
        int overPayFeeId = result.getInt("OverPayFeeID");
        transaction.setOverPayFeeId(result.wasNull() ? null : overPayFeeId);
        return transaction;
    }

    @Override
   
    public List<Transaction> getBuyInvestmentTransactions(int userId, int month, int year) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.TransID, t.TypeID, t.UserID, t.trans_amount, t.trans_date, t.InStID FROM TRANSACTION t JOIN TYPE ty ON t.TypeID = ty.TypeID AND t.UserID = ty.UserID WHERE t.UserID = ? AND ty.type_description = 'InSt_Buy' AND MONTH(t.trans_date) = ? AND YEAR(t.trans_date) = ?";
        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {
            save.setInt(1, userId);
            save.setInt(2, month);
            save.setInt(3, year);
            try (ResultSet result = save.executeQuery()) {
                while (result.next()) {
                     Transaction trans = new Transaction();
                     trans.setTransId(result.getInt("TransID"));
                     trans.setTypeId(result.getString("TypeID"));
                     trans.setUserId(result.getInt("UserID"));
                     trans.setTransAmount(result.getDouble("trans_amount"));
                     trans.setTransDate(result.getDate("trans_date"));
                     trans.setInStId(result.getString("InStID"));
                    transactions.add(trans);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Lỗi khi lấy giao dịch MUA đầu tư: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
     
    public List<Transaction> getSellInvestmentTransactions(int userId, int month, int year) {
        List<Transaction> transactions = new ArrayList<>();
        String sql ="SELECT t.TransID, t.TypeID, t.UserID, t.trans_amount, t.trans_date, t.sold_num_unit, t.sold_profit, t.InStID FROM TRANSACTION t JOIN TYPE ty ON t.TypeID = ty.TypeID AND t.UserID = ty.UserID WHERE t.UserID = ? AND ty.type_description = 'InSt_Sell' AND MONTH(t.trans_date) = ? AND YEAR(t.trans_date) = ?";

        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {
            save.setInt(1, userId);
            save.setInt(2, month);
            save.setInt(3, year);
            try (ResultSet result = save.executeQuery()) {
                while (result.next()) {
                     
                     Transaction trans = new Transaction();
                     trans.setTransId(result.getInt("TransID"));
                     trans.setTypeId(result.getString("TypeID"));
                     trans.setUserId(result.getInt("UserID"));
                     trans.setTransAmount(result.getDouble("trans_amount")); 
                     trans.setTransDate(result.getDate("trans_date"));
                     trans.setSoldNumUnit(result.getDouble("sold_num_unit")); 
                     trans.setSoldProfit(result.getDouble("sold_profit"));  
                     trans.setInStId(result.getString("InStID"));
                    transactions.add(trans);
                }
            }
        } catch (SQLException e) {
            System.err.println("DAO Lỗi khi lấy giao dịch BÁN đầu tư: " + e.getMessage());
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public List<SpendingByIncomeSource> getSpendingTransactionsGroupedByIncome(int userId, int month, int year) {
        List<SpendingByIncomeSource> resultList = new ArrayList<>();
        SpendingByIncomeSource currentSourceGroup = null;
        int previousIncomeId = -1;

        String sql ="SELECT t.TransID, t.TypeID, t.UserID, t.trans_amount, t.trans_date, t.IncomeID, i.income_name, ty.type_description FROM TRANSACTION t JOIN INCOME i ON t.IncomeID = i.IncomeID AND t.UserID = i.UserID JOIN TYPE ty ON t.TypeID = ty.TypeID AND t.UserID = ty.UserID WHERE t.UserID = ? AND t.IncomeID IS NOT NULL AND MONTH(t.trans_date) = ? AND YEAR(t.trans_date) = ? ORDER BY i.IncomeID, t.trans_date";

        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {
            save.setInt(1, userId);
            save.setInt(2, month);
            save.setInt(3, year);
            try (ResultSet result = save.executeQuery()) {
                while (result.next()) {
                    int currentIncomeId = result.getInt("IncomeID");
                    Transaction spendingDetail = new Transaction();
                    spendingDetail.setTransId(result.getInt("TransID"));
                    spendingDetail.setUserId(result.getInt("UserID"));
                    spendingDetail.setTransAmount(result.getDouble("trans_amount")); 
                    spendingDetail.setTransDate(result.getDate("trans_date"));
                    spendingDetail.setTypeId(result.getString("TypeID"));
                    if (currentIncomeId != previousIncomeId) {
                        String incomeName = result.getString("income_name");
                        currentSourceGroup = new SpendingByIncomeSource(currentIncomeId, incomeName, new ArrayList<>());
                        resultList.add(currentSourceGroup);
                        previousIncomeId = currentIncomeId;
                    }
                    if (currentSourceGroup != null) {
                         currentSourceGroup.getTransactions().add(spendingDetail);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy GD chi tiêu theo nguồn thu nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    
    public List<SpendingCategory> GetSpendingTotalType(int userId, int month, int year) { 
        List<SpendingCategory> spendingList = new ArrayList<>();
       
        String sql ="SELECT   ty.type_description,   ty.trans_type_rate,   SUM(t.trans_amount) AS totalAmount,   COALESCE(i.income_name, s.save_name, 'Khác') AS spending_source FROM TRANSACTION t JOIN TYPE ty ON t.TypeID = ty.TypeID AND t.UserID = ty.UserID LEFT JOIN INCOME i ON t.IncomeID = i.IncomeID AND t.UserID = i.UserID LEFT JOIN SAVING s ON t.SaveID = s.SaveID AND t.UserID = s.UserID WHERE t.UserID = ?   AND MONTH(t.trans_date) = ?   AND YEAR(t.trans_date) = ?   AND t.TypeID LIKE 'SP_%' GROUP BY ty.type_description, ty.trans_type_rate, spending_source ORDER BY totalAmount DESC";

        try (Connection ketnoi = ketnoidb.getConnection(); 
             PreparedStatement save = ketnoi.prepareStatement(sql)) { 

            save.setInt(1, userId); 
            save.setInt(2, month);
            save.setInt(3, year); 

         
try (ResultSet result = save.executeQuery()) {
    while (result.next()) {
        
        String typeId = result.getString("TypeID");            
        String description = result.getString("type_description");
        double rate = result.getDouble("trans_type_rate");
        if (result.wasNull()) {
            rate = 0.0;
        }
        double total = result.getDouble("totalAmount");
        String source = result.getString("spending_source"); 
        SpendingCategory category = new SpendingCategory();
        category.setTypeId(typeId);                         
        category.setTypeDescription(description);           
        category.setTotalAmount(total);                     
        category.setPercentageRate(rate);                   


        spendingList.add(category);
    }
} 
        } catch (SQLException e) {
            System.err.println(" Có lỗi xảy ra khi lấy tổng chi tiêu theo loại: " + e.getMessage());
             e.printStackTrace(); 
        }
        return spendingList;
    }
    @Override
    public long getTransactionCount(int userId, int month, int year) {
        long count = 0;
        String sql = "SELECT COUNT(*) FROM TRANSACTION WHERE UserID = ? AND MONTH(trans_date) = ? AND YEAR(trans_date) = ?";

        try (Connection ketnoi = ketnoidb.getConnection();
             PreparedStatement save = ketnoi.prepareStatement(sql)) {

            save.setInt(1, userId);
            save.setInt(2, month);
            save.setInt(3, year);

            try (ResultSet result = save.executeQuery()) {
                if (result.next()) {
                    count = result.getLong(1); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm số lượng giao dịch cho UserID " + userId + ", tháng " + month + ", năm " + year + ": " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }

}