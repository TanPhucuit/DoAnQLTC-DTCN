package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.BankTransferDAO;
import com.personal.finance.testproject.dao.impl.BankTransferDAOImpl;
import com.personal.finance.testproject.model.BankTransfer;
import java.sql.Connection;
import java.math.BigDecimal;
import java.util.List;

public class BankTransferService {
    private final BankTransferDAO bankTransferDAO;

    public BankTransferService(Connection connection) {
        this.bankTransferDAO = new BankTransferDAOImpl(connection);
    }

    public void transfer(int userId, String fromAccount, String toAccount, BigDecimal amount) {
        bankTransferDAO.transfer(userId, fromAccount, toAccount, amount);
    }

    public List<BankTransfer> getTransfersByUserId(int userId) {
        return bankTransferDAO.findByUserId(userId);
    }
} 