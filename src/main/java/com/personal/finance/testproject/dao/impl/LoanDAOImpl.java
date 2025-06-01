package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.LoanDAO;
import com.personal.finance.testproject.model.Loan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {
    private final Connection connection;

    public LoanDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addLoan(Loan loan) {
        String sql = "INSERT INTO LOAN (user_id, purpose_id, form_id, amount, interest_rate, term, date_of_payment, disbur_date, remain_loan, bank_account_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loan.getUserId());
            stmt.setInt(2, loan.getPurposeId());
            stmt.setString(3, loan.getFormId());
            stmt.setBigDecimal(4, loan.getLoanAmount());
            stmt.setBigDecimal(5, loan.getInterestRate());
            stmt.setInt(6, loan.getNumTerm());
            stmt.setInt(7, loan.getDateOfPayment());
            stmt.setDate(8, new java.sql.Date(loan.getDisburDate().getTime()));
            stmt.setBigDecimal(9, loan.getLoanRemain());
            stmt.setString(10, loan.getBankAccountNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding loan", e);
        }
    }

    @Override
    public Loan getLoan(int loanId, int userId) {
        String sql = "SELECT * FROM LOAN WHERE loan_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToLoan(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loan", e);
        }
        return null;
    }

    @Override
    public List<Loan> getLoansByUserId(int userId) {
        String sql = "SELECT * FROM LOAN WHERE user_id = ?";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loans by user ID", e);
        }
        return loans;
    }

    @Override
    public List<Loan> getAllLoans() {
        String sql = "SELECT * FROM LOAN";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all loans", e);
        }
        return loans;
    }

    @Override
    public List<Loan> getLoansByMonth(int userId, String month) {
        String sql = "SELECT * FROM LOAN WHERE user_id = ? AND DATE_FORMAT(disbur_date, '%Y-%m') = ?";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loans by month", e);
        }
        return loans;
    }

    @Override
    public void updateLoan(Loan loan) {
        String sql = "UPDATE LOAN SET purpose_id = ?, form_id = ?, amount = ?, interest_rate = ?, term = ?, date_of_payment = ?, disbur_date = ?, remain_loan = ?, bank_account_number = ? WHERE loan_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loan.getPurposeId());
            stmt.setString(2, loan.getFormId());
            stmt.setBigDecimal(3, loan.getLoanAmount());
            stmt.setBigDecimal(4, loan.getInterestRate());
            stmt.setInt(5, loan.getNumTerm());
            stmt.setInt(6, loan.getDateOfPayment());
            stmt.setDate(7, new java.sql.Date(loan.getDisburDate().getTime()));
            stmt.setBigDecimal(8, loan.getLoanRemain());
            stmt.setString(9, loan.getBankAccountNumber());
            stmt.setInt(10, loan.getLoanId());
            stmt.setInt(11, loan.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating loan", e);
        }
    }

    @Override
    public List<Loan> getLoansByPurposeId(int purposeId, int userId) {
        String sql = "SELECT * FROM LOAN WHERE purpose_id = ? AND user_id = ?";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, purposeId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loans by purpose ID", e);
        }
        return loans;
    }

    @Override
    public List<Loan> getLoansByBankAccount(String bankAccountNumber) {
        String sql = "SELECT * FROM LOAN WHERE bank_account_number = ?";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bankAccountNumber);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loans by bank account", e);
        }
        return loans;
    }

    @Override
    public void updateRemainLoan(int loanId, int userId, BigDecimal amount) {
        String sql = "UPDATE LOAN SET remain_loan = ? WHERE loan_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, amount);
            stmt.setInt(2, loanId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating remain loan", e);
        }
    }

    @Override
    public List<Loan> getLoansByFormId(String formId, int userId) {
        String sql = "SELECT * FROM LOAN WHERE form_id = ? AND user_id = ?";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, formId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loans by form ID", e);
        }
        return loans;
    }

    @Override
    public List<Loan> getActiveLoans(int userId) {
        String sql = "SELECT * FROM LOAN WHERE user_id = ? AND remain_loan > 0";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting active loans", e);
        }
        return loans;
    }

    @Override
    public List<Loan> getOverdueLoans(int userId) {
        String sql = "SELECT * FROM LOAN WHERE user_id = ? AND remain_loan > 0 AND disbur_date < CURRENT_DATE";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting overdue loans", e);
        }
        return loans;
    }

    @Override
    public BigDecimal getTotalLoanRemain(int userId) {
        String sql = "SELECT SUM(remain_loan) FROM LOAN WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting total loan remain", e);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public int getActiveLoansCount(int userId) {
        String sql = "SELECT COUNT(*) FROM LOAN WHERE user_id = ? AND remain_loan > 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting active loans count", e);
        }
        return 0;
    }

    @Override
    public List<Loan> findByUserIdAndMonth(int userId, String month) {
        String sql = "SELECT * FROM LOAN WHERE user_id = ? AND DATE_FORMAT(disbur_date, '%Y-%m') = ?";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, month);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loans by month", e);
        }
        return loans;
    }

    @Override
    public List<Loan> findByUserIdAndYear(int userId, String year) {
        String sql = "SELECT * FROM LOAN WHERE user_id = ? AND DATE_FORMAT(disbur_date, '%Y') = ?";
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, year);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting loans by year", e);
        }
        return loans;
    }

    @Override
    public void deleteLoan(int loanId, int userId) {
        String sql = "DELETE FROM LOAN WHERE loan_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, loanId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting loan", e);
        }
    }

    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setUserId(rs.getInt("user_id"));
        loan.setPurposeId(rs.getInt("purpose_id"));
        loan.setFormId(rs.getString("form_id"));
        loan.setLoanAmount(rs.getBigDecimal("amount"));
        loan.setInterestRate(rs.getBigDecimal("interest_rate"));
        loan.setNumTerm(rs.getInt("term"));
        loan.setDateOfPayment(rs.getInt("date_of_payment"));
        java.sql.Date disburDate = rs.getDate("disbur_date");
        if (disburDate == null || disburDate.toString().equals("0000-00-00")) {
            disburDate = java.sql.Date.valueOf("2000-01-01");
        }
        loan.setDisburDate(disburDate);
        loan.setLoanRemain(rs.getBigDecimal("remain_loan"));
        loan.setBankAccountNumber(rs.getString("bank_account_number"));
        return loan;
    }

    private void validateLoan(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        if (loan.getUserId() <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (loan.getFormId() == null || loan.getFormId().trim().isEmpty()) {
            throw new IllegalArgumentException("Form ID cannot be null or empty");
        }
        if (loan.getLoanAmount() == null || loan.getLoanAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (loan.getDisburDate() == null) {
            throw new IllegalArgumentException("Disbur date cannot be null");
        }
    }
} 