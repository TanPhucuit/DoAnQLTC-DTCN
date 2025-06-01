package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.Purpose;
import java.util.List;

public interface PurposeDAO {
    void insert(Purpose purpose);
    Purpose findById(int purposeId, int userId);
    List<Purpose> findByUserId(int userId);
    List<Purpose> findAll();
    void update(Purpose purpose);
    void delete(int purposeId, int userId);
} 