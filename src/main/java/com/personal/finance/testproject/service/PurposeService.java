package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.PurposeDAO;
import com.personal.finance.testproject.dao.impl.PurposeDAOImpl;
import com.personal.finance.testproject.model.Purpose;
import java.sql.Connection;
import java.math.BigDecimal;
import java.util.List;

public class PurposeService {
    private final PurposeDAO purposeDAO;

    public PurposeService(Connection connection) {
        this.purposeDAO = new PurposeDAOImpl(connection);
    }

    public void addPurpose(int userId, String purposeName, BigDecimal estimateAmount, String description, boolean purposeState) {
        Purpose purpose = new Purpose();
        purpose.setUserId(userId);
        purpose.setPurposeName(purposeName);
        purpose.setEstimateAmount(estimateAmount);
        purpose.setDescription(description);
        purpose.setPurposeState(purposeState);
        purposeDAO.insert(purpose);
    }

    public void deletePurpose(int purposeId, int userId) {
        purposeDAO.delete(purposeId, userId);
    }

    public List<Purpose> getAllPurposes(int userId) {
        return purposeDAO.findByUserId(userId);
    }

    public List<Purpose> getAll() {
        return purposeDAO.findAll();
    }

    public void updatePurpose(Purpose purpose) {
        purposeDAO.update(purpose);
    }

    public Purpose getPurposeById(int purposeId, int userId) {
        return purposeDAO.findById(purposeId, userId);
    }
} 