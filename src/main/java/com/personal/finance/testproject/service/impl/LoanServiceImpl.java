package com.personal.finance.testproject.service.impl;

import com.personal.finance.testproject.dao.LoanDAO;
import com.personal.finance.testproject.model.Loan;
import com.personal.finance.testproject.service.LoanService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class LoanServiceImpl implements LoanService {
    private final LoanDAO loanDAO;

    public LoanServiceImpl(LoanDAO loanDAO) {
        this.loanDAO = loanDAO;
    }

    @Override
    public void addLoan(Loan loan) {
        validateLoan(loan);
        loanDAO.addLoan(loan);
    }

    @Override
    public Loan getLoan(int loanId, int userId) {
        if (loanId <= 0) {
            throw new IllegalArgumentException("LoanID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getLoan(loanId, userId);
    }

    @Override
    public List<Loan> getLoansByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getLoansByUserId(userId);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanDAO.getAllLoans();
    }

    @Override
    public List<Loan> getLoansByMonth(int userId, String month) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (month == null || month.trim().isEmpty()) {
            throw new IllegalArgumentException("Month cannot be empty");
        }
        return loanDAO.getLoansByMonth(userId, month);
    }

    @Override
    public void updateLoan(Loan loan) {
        validateLoan(loan);
        loanDAO.updateLoan(loan);
    }

    @Override
    public List<Loan> getLoansByPurposeId(int purposeId, int userId) {
        if (purposeId <= 0) {
            throw new IllegalArgumentException("PurposeID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getLoansByPurposeId(purposeId, userId);
    }

    @Override
    public List<Loan> getLoansByBankAccount(String bankAccountNumber) {
        if (bankAccountNumber == null || bankAccountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number cannot be empty");
        }
        return loanDAO.getLoansByBankAccount(bankAccountNumber);
    }

    @Override
    public void updateRemainLoan(int loanId, int userId, BigDecimal amount) {
        if (loanId <= 0) {
            throw new IllegalArgumentException("LoanID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        loanDAO.updateRemainLoan(loanId, userId, amount);
    }

    @Override
    public void deleteLoan(int loanId, int userId) {
        if (loanId <= 0) {
            throw new IllegalArgumentException("LoanID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        loanDAO.deleteLoan(loanId, userId);
    }

    @Override
    public List<Loan> getLoansByFormId(String formId, int userId) {
        if (formId == null || formId.trim().isEmpty()) {
            throw new IllegalArgumentException("FormID cannot be empty");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getLoansByFormId(formId, userId);
    }

    @Override
    public List<Loan> getActiveLoans(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getActiveLoans(userId);
    }

    @Override
    public List<Loan> getOverdueLoans(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getOverdueLoans(userId);
    }

    @Override
    public BigDecimal getTotalLoanRemain(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getTotalLoanRemain(userId);
    }

    @Override
    public int getActiveLoansCount(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        return loanDAO.getActiveLoansCount(userId);
    }

    @Override
    public boolean isLoanOverdue(int loanId, int userId) {
        if (loanId <= 0) {
            throw new IllegalArgumentException("LoanID must be positive");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        Loan loan = loanDAO.getLoan(loanId, userId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan not found");
        }
        return loan.getLoanRemain().compareTo(BigDecimal.ZERO) > 0 && 
               loan.getNumPaidTerm() < loan.getNumTerm() &&
               loan.getDisburDate().before(new Date());
    }

    private void validateLoan(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("Loan cannot be null");
        }
        if (loan.getUserId() <= 0) {
            throw new IllegalArgumentException("UserID must be positive");
        }
        if (loan.getLoanAmount() == null || loan.getLoanAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (loan.getNumTerm() <= 0) {
            throw new IllegalArgumentException("Number of terms must be positive");
        }
        if (loan.getDisburDate() == null) {
            throw new IllegalArgumentException("Disbursement date cannot be null");
        }
    }
} 