package com.personal.finance.testproject.service;

import com.personal.finance.testproject.dao.UserDAO;
import com.personal.finance.testproject.dao.UserInfoDAO;
import com.personal.finance.testproject.dao.impl.UserDAOImpl;
import com.personal.finance.testproject.dao.impl.UserInfoDAOImpl;
import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.model.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {
    private final UserDAO userDAO;
    private final UserInfoDAO userInfoDAO;
    private final Connection connection;

    public AuthenticationService(Connection connection) {
        this.userDAO = new UserDAOImpl(connection);
        this.userInfoDAO = new UserInfoDAOImpl(connection);
        this.connection = connection;
    }

    public User login(String username, String password) throws SQLException {
        String query = "SELECT u.UserID, u.user_name, u.password, ui.full_name, ui.PhoneNumber " +
                       "FROM SYS_USER u LEFT JOIN USER_INFORMATION ui ON u.UserID = ui.UserID " +
                       "WHERE u.user_name = ? AND u.password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("UserID");
                String userName = rs.getString("user_name");
                String pass = rs.getString("password");
                String fullName = rs.getString("full_name");
                String phone = rs.getString("PhoneNumber");
                if (fullName == null) fullName = "";
                if (phone == null) phone = "";
                return new User(userId, userName, pass, fullName, phone);
            }
        }
        return null;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        // First verify current password
        String verifyQuery = "SELECT UserID FROM SYS_USER WHERE user_name = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(verifyQuery)) {
            stmt.setString(1, username);
            stmt.setString(2, oldPassword);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Mật khẩu hiện tại không đúng");
            }
        }

        // Update to new password
        String updateQuery = "UPDATE SYS_USER SET password = ? WHERE user_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateQuery)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
            return true;
        }
    }

    public boolean register(String username, String password, String fullName, String phone, String address) {
        try {
            if (userDAO.findByUsername(username) != null) {
                return false;
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userDAO.insert(user);
            if (user.getUserId() == 0) return false;
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getUserId());
            userInfo.setFullName(fullName);
            userInfo.setPhone(phone);
            userInfo.setAddress(address);
            userInfoDAO.insert(userInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void register(String username, String password) throws SQLException {
        String query = "INSERT INTO SYS_USER (user_name, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        }
    }

    public Connection getConnection() {
        return connection;
    }
} 