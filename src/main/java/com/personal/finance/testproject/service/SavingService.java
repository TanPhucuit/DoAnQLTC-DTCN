package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.SavingDAO;
import com.personal.finance.testproject.model.Saving;
import java.util.List;
import java.math.BigDecimal;

public interface SavingService {
    void createSaving(Saving saving);
    void updateSaving(Saving saving);


    List<Saving> findByUserID(int userId);
    void updateRemainSaving(int savingId, int userId, BigDecimal amount);
}


