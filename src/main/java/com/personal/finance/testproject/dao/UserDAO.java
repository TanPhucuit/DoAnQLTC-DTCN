package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.model.UserInformation;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    // Create
    void insert(User user) throws SQLException;
    
    // Read
    User findById(int id) throws SQLException;
    User findByUsername(String username) throws SQLException;
    User findByEmail(String email);
    List<User> findAll() throws SQLException;
    
    // Update
    void update(User user) throws SQLException;
    
    // Delete
    void delete(int id) throws SQLException;
    
    // Additional methods
    boolean validateUser(String username, String password);
    boolean isUsernameExists(String username);
    Connection getConnection();

    User findByUserName(String userName);
    boolean checkPassword(int userId, String password);
    void updatePassword(int userId, String newPassword);
    void insertUser(User user, UserInformation userInfo);
    UserInformation getUserInformation(int userId);
    boolean isUserNameExists(String userName);
} 