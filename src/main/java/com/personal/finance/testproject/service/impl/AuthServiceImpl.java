package com.personal.finance.testproject.service.impl;

import com.personal.finance.testproject.dao.UserDAO;
import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.model.UserInformation;
import com.personal.finance.testproject.service.AuthService;
import java.sql.SQLException;

public class AuthServiceImpl implements AuthService {
    private final UserDAO userDAO;

    public AuthServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User login(String userName, String password) {
        try {
            // Validate input
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên đăng nhập không được để trống");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống");
            }

            // Check if user exists
            User user = userDAO.findByUsername(userName.trim());
            if (user == null) {
                throw new IllegalArgumentException("Tài khoản không tồn tại");
            }

            // Verify password
            if (!user.getPassword().equals(password)) {
                throw new IllegalArgumentException("Mật khẩu không đúng");
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kết nối database: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đăng nhập không xác định: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean register(User user, UserInformation userInfo) {
        try {
            // Validate input
            if (user == null || userInfo == null) {
                throw new IllegalArgumentException("Thông tin đăng ký không hợp lệ");
            }
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                throw new IllegalArgumentException("Tên đăng nhập không được để trống");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống");
            }
            if (userInfo.getFullName() == null || userInfo.getFullName().trim().isEmpty()) {
                throw new IllegalArgumentException("Họ tên không được để trống");
            }

            // Check if username already exists
            if (userDAO.isUsernameExists(user.getUsername().trim())) {
                throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
            }

            // Register user
            userDAO.insertUser(user, userInfo);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đăng ký không xác định: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        try {
            // Validate input
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu cũ không được để trống");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu mới không được để trống");
            }

            // Verify old password
            if (!userDAO.checkPassword(userId, oldPassword)) {
                throw new IllegalArgumentException("Mật khẩu cũ không đúng");
            }

            // Update password
            userDAO.updatePassword(userId, newPassword);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đổi mật khẩu không xác định: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        try {
            User user = userDAO.findByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("Tài khoản không tồn tại");
            }
            return changePassword(user.getUserId(), oldPassword, newPassword);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kết nối database: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đổi mật khẩu không xác định: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean validatePassword(String password) {
        return password != null && !password.trim().isEmpty() && password.length() >= 6;
    }

    @Override
    public boolean validateUserName(String userName) {
        return userName != null && !userName.trim().isEmpty() && userName.length() >= 3;
    }
} 