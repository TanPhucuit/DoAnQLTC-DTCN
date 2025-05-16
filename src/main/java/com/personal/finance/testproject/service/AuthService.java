package com.personal.finance.testproject.service;

import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.model.UserInformation;

public interface AuthService {
    User login(String userName, String password);
    boolean register(User user, UserInformation userInfo);
    boolean changePassword(int userId, String oldPassword, String newPassword);
    boolean changePassword(String username, String oldPassword, String newPassword);
    boolean validatePassword(String password);
    boolean validateUserName(String userName);
} 