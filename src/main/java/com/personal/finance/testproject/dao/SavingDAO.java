package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Saving;
import java.math.BigDecimal;
import java.util.List;

public interface SavingDAO {
    // Create
    void insert(Saving saving);
    
    List<Saving> findByUserID(int userId);


    
    // Update
    void update(Saving saving);
    void updateRemainSave(int savingId, int userId, BigDecimal amount);
    

} 