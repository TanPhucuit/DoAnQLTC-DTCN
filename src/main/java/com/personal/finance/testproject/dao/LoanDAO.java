package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Loan;
import java.math.BigDecimal;
import java.util.List;

public interface LoanDAO {
    // Create
    void addLoan(Loan loan);
    
    // Read
    Loan getLoan(int loanId, int userId);
    List<Loan> getLoansByUserId(int userId);
    List<Loan> getAllLoans();
    List<Loan> getLoansByMonth(int userId, String month);
    List<Loan> getLoansByPurposeId(int purposeId, int userId);
    List<Loan> getLoansByBankAccount(String bankAccountNumber);
    List<Loan> getLoansByFormId(String formId, int userId);
    List<Loan> getActiveLoans(int userId);
    List<Loan> getOverdueLoans(int userId);
    List<Loan> findByUserIdAndMonth(int userId, String month);
    List<Loan> findByUserIdAndYear(int userId, String year);
    
    // Update
    void updateLoan(Loan loan);
    void updateRemainLoan(int loanId, int userId, BigDecimal amount);
    void deleteLoan(int loanId, int userId);

    BigDecimal getTotalLoanRemain(int userId);
    int getActiveLoansCount(int userId);
} 