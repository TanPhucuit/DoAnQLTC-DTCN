package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.UserInfoDAO;
import com.personal.finance.testproject.model.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserInfoDAOImpl implements UserInfoDAO {
    private final Connection connection;

    public UserInfoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(UserInfo userInfo) {
        String sql = "INSERT INTO USER_INFO (USER_ID, FULL_NAME, EMAIL, PHONE, ADDRESS) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userInfo.getUserId());
            stmt.setString(2, userInfo.getFullName());
            stmt.setString(3, userInfo.getEmail());
            stmt.setString(4, userInfo.getPhone());
            stmt.setString(5, userInfo.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting user info", e);
        }
    }

    @Override
    public UserInfo findByUserId(int userId) {
        String sql = "SELECT ui.*, u.user_name " +
                    "FROM USER_INFO ui " +
                    "LEFT JOIN SYS_USER u ON ui.USER_ID = u.UserID " +
                    "WHERE ui.USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserId(rs.getInt("USER_ID"));
                    userInfo.setFullName(rs.getString("FULL_NAME"));
                    userInfo.setEmail(rs.getString("EMAIL"));
                    userInfo.setPhone(rs.getString("PHONE"));
                    userInfo.setAddress(rs.getString("ADDRESS"));
                    return userInfo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user info by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserInfo> findAll() {
        String sql = "SELECT ui.*, u.user_name " +
                    "FROM USER_INFO ui " +
                    "LEFT JOIN SYS_USER u ON ui.USER_ID = u.UserID";
        List<UserInfo> userInfos = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(rs.getInt("USER_ID"));
                userInfo.setFullName(rs.getString("FULL_NAME"));
                userInfo.setEmail(rs.getString("EMAIL"));
                userInfo.setPhone(rs.getString("PHONE"));
                userInfo.setAddress(rs.getString("ADDRESS"));
                userInfos.add(userInfo);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all user infos: " + e.getMessage());
            e.printStackTrace();
        }
        return userInfos;
    }

    @Override
    public void update(UserInfo userInfo) {
        String sql = "UPDATE USER_INFO SET FULL_NAME = ?, EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE USER_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userInfo.getFullName());
            stmt.setString(2, userInfo.getEmail());
            stmt.setString(3, userInfo.getPhone());
            stmt.setString(4, userInfo.getAddress());
            stmt.setInt(5, userInfo.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user info", e);
        }
    }

 
} 