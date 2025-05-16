package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.AllocationRatioDAO;
import com.personal.finance.testproject.dao.impl.AllocationRatioDAOImpl;
import com.personal.finance.testproject.model.AllocationRatio;
import java.sql.Connection;
import java.util.List;

public class AllocationRatioService {
    private final AllocationRatioDAO allocationRatioDAO;

    public AllocationRatioService(Connection connection) {
        this.allocationRatioDAO = new AllocationRatioDAOImpl(connection);
    }

    public AllocationRatio getAllocationRatioByInvestorType(String investorType) {
        return allocationRatioDAO.findByInvestorType(investorType);
    }

    public List<AllocationRatio> getAllAllocationRatios() {
        return allocationRatioDAO.findAll();
    }

    public void updateAllocationRatio(AllocationRatio allocationRatio) {
        allocationRatioDAO.update(allocationRatio);
    }

    public void updateLv1Rate(String investorType, double rate) {
        allocationRatioDAO.updateLv1Rate(investorType, rate);
    }

    public void updateLv2Rate(String investorType, double rate) {
        allocationRatioDAO.updateLv2Rate(investorType, rate);
    }

    public void updateLv3Rate(String investorType, double rate) {
        allocationRatioDAO.updateLv3Rate(investorType, rate);
    }
} 