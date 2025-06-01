package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.UserInformation;
import java.util.List;

public interface UserInformationDAO {
    // Create
    void insert(UserInformation userInformation);
    
    // Read
    UserInformation findByUserId(int userId);
    List<UserInformation> findAll();
    
    // Update
    void update(UserInformation userInformation);
    
    // Delete
    void delete(int userId);
    
    // Additional methods
    List<UserInformation> findByCountry(String country);
    List<UserInformation> findByCity(String city);
} 