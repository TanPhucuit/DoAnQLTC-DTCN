package quanlytaichinh.service;

import quanlytaichinh.ketnoidb;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonalFinanceService {

    // Đăng nhập người dùng
    public boolean login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM SYS_USER WHERE user_name = ? AND password = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    // Đăng ký người dùng mới với kiểm tra tên đăng nhập
    public void register(String username, String password, String fullName, Date dob, String country, String city, String phone) throws SQLException {
        String sqlCheckUsername = "SELECT * FROM SYS_USER WHERE user_name = ?";
        String sqlUser = "INSERT INTO SYS_USER (user_name, password) VALUES (?, ?)";
        String sqlInfo = "INSERT INTO USER_INFORMATION (UserID, full_name, date_of_birth, Country, City, PhoneNumber) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ketnoidb.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Kiểm tra xem tên đăng nhập đã tồn tại chưa
                try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheckUsername)) {
                    pstmtCheck.setString(1, username);
                    ResultSet rsCheck = pstmtCheck.executeQuery();
                    if (rsCheck.next()) {
                        throw new SQLException("Tên đăng nhập đã tồn tại. Vui lòng chọn tên đăng nhập khác.");
                    }
                }

                // Thêm người dùng mới vào SYS_USER
                try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                    pstmtUser.setString(1, username);
                    pstmtUser.setString(2, password);
                    pstmtUser.executeUpdate();
                    ResultSet rs = pstmtUser.getGeneratedKeys();
                    if (rs.next()) {
                        int userId = rs.getInt(1);
                        // Thêm thông tin người dùng vào USER_INFORMATION
                        try (PreparedStatement pstmtInfo = conn.prepareStatement(sqlInfo)) {
                            pstmtInfo.setInt(1, userId);
                            pstmtInfo.setString(2, fullName);
                            pstmtInfo.setDate(3, new java.sql.Date(dob.getTime()));
                            pstmtInfo.setString(4, country);
                            pstmtInfo.setString(5, city);
                            pstmtInfo.setString(6, phone);
                            pstmtInfo.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Lấy danh sách tài khoản ngân hàng
    public List<BankAccount> getBankAccounts(int userId) throws SQLException {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT * FROM BANK_ACCOUNT WHERE UserID = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                BankAccount account = new BankAccount();
                account.setBankAccountNumber(rs.getString("BankAccountNumber"));
                account.setBankName(rs.getString("BankName"));
                account.setBalance(rs.getBigDecimal("Balance"));
                accounts.add(account);
            }
        }
        return accounts;
    }

    // Thực hiện chuyển khoản
    public void transfer(int userId, String sourceAccount, String targetAccount, BigDecimal amount) throws SQLException {
        String sqlCheck = "SELECT Balance FROM BANK_ACCOUNT WHERE BankAccountNumber = ? AND UserID = ?";
        String sqlUpdateSource = "UPDATE BANK_ACCOUNT SET Balance = Balance - ? WHERE BankAccountNumber = ? AND UserID = ?";
        String sqlUpdateTarget = "UPDATE BANK_ACCOUNT SET Balance = Balance + ? WHERE BankAccountNumber = ?";
        String sqlInsertTransfer = "INSERT INTO BANK_TRANSFER (SourceBankAccountNumber, TargetBankAccountNumber, Transfer_amount, Transfer_date) VALUES (?, ?, ?, CURDATE())";

        try (Connection conn = ketnoidb.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
                    pstmtCheck.setString(1, sourceAccount);
                    pstmtCheck.setInt(2, userId);
                    ResultSet rs = pstmtCheck.executeQuery();
                    if (rs.next() && rs.getBigDecimal("Balance").compareTo(amount) < 0) {
                        throw new SQLException("Số dư không đủ");
                    }
                }
                try (PreparedStatement pstmtUpdateSource = conn.prepareStatement(sqlUpdateSource)) {
                    pstmtUpdateSource.setBigDecimal(1, amount);
                    pstmtUpdateSource.setString(2, sourceAccount);
                    pstmtUpdateSource.setInt(3, userId);
                    pstmtUpdateSource.executeUpdate();
                }
                try (PreparedStatement pstmtUpdateTarget = conn.prepareStatement(sqlUpdateTarget)) {
                    pstmtUpdateTarget.setBigDecimal(1, amount);
                    pstmtUpdateTarget.setString(2, targetAccount);
                    pstmtUpdateTarget.executeUpdate();
                }
                try (PreparedStatement pstmtInsertTransfer = conn.prepareStatement(sqlInsertTransfer)) {
                    pstmtInsertTransfer.setString(1, sourceAccount);
                    pstmtInsertTransfer.setString(2, targetAccount);
                    pstmtInsertTransfer.setBigDecimal(3, amount);
                    pstmtInsertTransfer.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Thêm thu nhập mới
    public void addIncome(int userId, String month, String incomeName, BigDecimal amount) throws SQLException {
        String sql = "INSERT INTO INCOME (UserID, ic_month, income_name, income_amount, remain_income) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, month);
            pstmt.setString(3, incomeName);
            pstmt.setBigDecimal(4, amount);
            pstmt.setBigDecimal(5, amount);
            pstmt.executeUpdate();
        }
    }

    // Lấy danh sách thu nhập theo tháng
    public List<Income> getIncomesByMonth(int userId, String month) throws SQLException {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT * FROM INCOME WHERE UserID = ? AND ic_month = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, month);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Income income = new Income();
                income.setIncomeId(rs.getInt("IncomeID"));
                income.setIncomeName(rs.getString("income_name"));
                income.setIncomeAmount(rs.getBigDecimal("income_amount"));
                incomes.add(income);
            }
        }
        return incomes;
    }

    // Thêm khoản vay mới
    public void addLoan(int userId, int purposeId, String formId, BigDecimal amount, Date disburDate, BigDecimal interest, int numTerm, int dateOfPayment) throws SQLException {
        String sql = "INSERT INTO LOAN (UserID, PurposeID, FormID, loan_amount, disbur_date, interest, num_term, date_of_payment, loan_remain) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, purposeId);
            pstmt.setString(3, formId);
            pstmt.setBigDecimal(4, amount);
            pstmt.setDate(5, new java.sql.Date(disburDate.getTime()));
            pstmt.setBigDecimal(6, interest);
            pstmt.setInt(7, numTerm);
            pstmt.setInt(8, dateOfPayment);
            pstmt.setBigDecimal(9, amount);
            pstmt.executeUpdate();
        }
    }

    // Thanh toán khoản vay
    public void payLoan(int userId, int loanId, int incomeId, Date payDate) throws SQLException {
        String sqlInsertTrans = "INSERT INTO TRANSACTION (TypeID, UserID, trans_date, LoanID, IncomeID) VALUES ('loan_pay', ?, ?, ?, ?)";
        try (Connection conn = ketnoidb.getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertTrans)) {
                pstmt.setInt(1, userId);
                pstmt.setDate(2, new java.sql.Date(payDate.getTime()));
                pstmt.setInt(3, loanId);
                pstmt.setInt(4, incomeId);
                pstmt.executeUpdate();
            }
        }
    }

    // Mua tài sản đầu tư
    public void buyInvestment(int userId, String instId, BigDecimal amount, Date buyDate, int incomeId) throws SQLException {
        String sqlInsertTrans = "INSERT INTO TRANSACTION (TypeID, UserID, trans_amount, trans_date, InStID, IncomeID) VALUES ('InSt_Buy', ?, ?, ?, ?, ?)";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertTrans)) {
            pstmt.setInt(1, userId);
            pstmt.setBigDecimal(2, amount);
            pstmt.setDate(3, new java.sql.Date(buyDate.getTime()));
            pstmt.setString(4, instId);
            pstmt.setInt(5, incomeId);
            pstmt.executeUpdate();
        }
    }

    // Bán tài sản đầu tư
    public void sellInvestment(int userId, String instId, BigDecimal amount, Date sellDate) throws SQLException {
        String sqlInsertTrans = "INSERT INTO TRANSACTION (TypeID, UserID, trans_amount, trans_date, InStID) VALUES ('InSt_Sell', ?, ?, ?, ?)";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsertTrans)) {
            pstmt.setInt(1, userId);
            pstmt.setBigDecimal(2, amount);
            pstmt.setDate(3, new java.sql.Date(sellDate.getTime()));
            pstmt.setString(4, instId);
            pstmt.executeUpdate();
        }
    }

    // Lấy danh mục đầu tư
    public List<Investment> getInvestments(int userId) throws SQLException {
        List<Investment> investments = new ArrayList<>();
        String sql = "SELECT * FROM INVEST_STORAGE WHERE UserID = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Investment inv = new Investment();
                inv.setInstId(rs.getString("InStID"));
                inv.setNumUnit(rs.getBigDecimal("num_unit"));
                inv.setBuyPrice(rs.getBigDecimal("buy_price"));
                investments.add(inv);
            }
        }
        return investments;
    }

    // Thêm giao dịch chi tiêu
    public void addSpendingTransaction(int userId, String typeId, BigDecimal amount, Date transDate, Integer incomeId) throws SQLException {
        String sql = "INSERT INTO TRANSACTION (TypeID, UserID, trans_amount, trans_date, IncomeID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeId);
            pstmt.setInt(2, userId);
            pstmt.setBigDecimal(3, amount);
            pstmt.setDate(4, new java.sql.Date(transDate.getTime()));
            if (incomeId != null) {
                pstmt.setInt(5, incomeId);
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.executeUpdate();
        }
    }

    // Lấy lịch sử giao dịch
    public List<Transaction> getTransactions(int userId, Date startDate, Date endDate) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM TRANSACTION WHERE UserID = ? AND trans_date BETWEEN ? AND ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction trans = new Transaction();
                trans.setTransId(rs.getInt("TransID"));
                trans.setTypeId(rs.getString("TypeID"));
                trans.setAmount(rs.getBigDecimal("trans_amount"));
                trans.setTransDate(rs.getDate("trans_date"));
                transactions.add(trans);
            }
        }
        return transactions;
    }

    // Lấy báo cáo thống kê hàng tháng
    public StatisticReport getMonthlyReport(int userId, String month) throws SQLException {
        StatisticReport report = null;
        String sql = "SELECT * FROM STATISTIC_REPORT WHERE UserID = ? AND sr_month = ?";
        try (Connection conn = ketnoidb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, month);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                report = new StatisticReport();
                report.setAveragePerDay(rs.getBigDecimal("average_per_day"));
                report.setAveragePerWeek(rs.getBigDecimal("average_per_week"));
                report.setCumulativePnl(rs.getBigDecimal("cumulative_pnl"));
            }
        }
        return report;
    }

    // Các lớp dữ liệu đơn giản
    public static class BankAccount {
        private String bankAccountNumber;
        private String bankName;
        private BigDecimal balance;

        public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }
        public void setBankName(String bankName) { this.bankName = bankName; }
        public void setBalance(BigDecimal balance) { this.balance = balance; }
    }

    public static class Income {
        private int incomeId;
        private String incomeName;
        private BigDecimal incomeAmount;

        public void setIncomeId(int incomeId) { this.incomeId = incomeId; }
        public void setIncomeName(String incomeName) { this.incomeName = incomeName; }
        public void setIncomeAmount(BigDecimal incomeAmount) { this.incomeAmount = incomeAmount; }
    }

    public static class Investment {
        private String instId;
        private BigDecimal numUnit;
        private BigDecimal buyPrice;

        public void setInstId(String instId) { this.instId = instId; }
        public void setNumUnit(BigDecimal numUnit) { this.numUnit = numUnit; }
        public void setBuyPrice(BigDecimal buyPrice) { this.buyPrice = buyPrice; }
    }

    public static class Transaction {
        private int transId;
        private String typeId;
        private BigDecimal amount;
        private Date transDate;

        public void setTransId(int transId) { this.transId = transId; }
        public void setTypeId(String typeId) { this.typeId = typeId; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public void setTransDate(Date transDate) { this.transDate = transDate; }
    }

    public static class StatisticReport {
        private BigDecimal averagePerDay;
        private BigDecimal averagePerWeek;
        private BigDecimal cumulativePnl;

        public void setAveragePerDay(BigDecimal averagePerDay) { this.averagePerDay = averagePerDay; }
        public void setAveragePerWeek(BigDecimal averagePerWeek) { this.averagePerWeek = averagePerWeek; }
        public void setCumulativePnl(BigDecimal cumulativePnl) { this.cumulativePnl = cumulativePnl; }
    }
}