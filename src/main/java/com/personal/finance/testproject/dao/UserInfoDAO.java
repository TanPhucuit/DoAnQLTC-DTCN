package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.UserInfo;
import java.util.List;

public interface UserInfoDAO {
    // Create
    void insert(UserInfo userInfo);
    
    // Read
    UserInfo findByUserId(int userId);
    List<UserInfo> findAll();
    
    // Update
    void update(UserInfo userInfo);
    

} 