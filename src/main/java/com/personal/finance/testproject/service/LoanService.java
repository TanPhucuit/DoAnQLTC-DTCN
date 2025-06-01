package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.LoanDAO;
import com.personal.finance.testproject.model.Loan;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LoanService {
    /**
     * Add a new loan
     * @param loan The loan to add
     */
    void addLoan(Loan loan);

    /**
     * Get a loan by its ID and user ID
     * @param loanId The loan ID
     * @param userId The user ID
     * @return The loan if found, null otherwise
     */
    Loan getLoan(int loanId, int userId);

    /**
     * Get all loans for a specific user
     * @param userId The user ID
     * @return List of loans for the user
     */
    List<Loan> getLoansByUserId(int userId);

    /**
     * Get all loans in the system
     * @return List of all loans
     */
    List<Loan> getAllLoans();

    List<Loan> getLoansByMonth(int userId, String month);
    void updateLoan(Loan loan);
    List<Loan> getLoansByPurposeId(int purposeId, int userId);
    List<Loan> getLoansByBankAccount(String bankAccountNumber);
    void updateRemainLoan(int loanId, int userId, BigDecimal amount);
    void deleteLoan(int loanId, int userId);
    List<Loan> getLoansByFormId(String formId, int userId);
    List<Loan> getActiveLoans(int userId);
    List<Loan> getOverdueLoans(int userId);
    BigDecimal getTotalLoanRemain(int userId);
    int getActiveLoansCount(int userId);
    boolean isLoanOverdue(int loanId, int userId);
} 
