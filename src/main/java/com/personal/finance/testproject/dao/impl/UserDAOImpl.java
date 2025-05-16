package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.UserDAO;
import com.personal.finance.testproject.model.User;
import com.personal.finance.testproject.model.UserInformation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM SYS_USER WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT u.UserID, u.user_name, u.password, ui.full_name, ui.PhoneNumber " +
                    "FROM SYS_USER u LEFT JOIN USER_INFORMATION ui ON u.UserID = ui.UserID " +
                    "WHERE u.user_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("UserID"));
                    user.setUsername(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));
                    String fullName = rs.getString("full_name");
                    String phone = rs.getString("PhoneNumber");
                    if (fullName != null) user.setFullName(fullName);
                    if (phone != null) user.setPhoneNumber(phone);
                    return user;
                }
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM SYS_USER";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO SYS_USER (user_name, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void update(User user) throws SQLException {
        String sql = "UPDATE SYS_USER SET user_name = ?, password = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setInt(3, user.getUserId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM SYS_USER WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean validateUser(String username, String password) {
        String sql = "SELECT COUNT(*) FROM SYS_USER WHERE user_name = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error validating user", e);
        }
        return false;
    }

    @Override
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM SYS_USER WHERE user_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking username existence", e);
        }
        return false;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public User findByEmail(String email) {
        // SYS_USER không có cột email, trả về null hoặc có thể throw exception nếu cần
        return null;
    }

    @Override
    public User findByUserName(String userName) {
        try {
            return findByUsername(userName);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by username", e);
        }
    }

    @Override
    public boolean checkPassword(int userId, String password) {
        String sql = "SELECT password FROM SYS_USER WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updatePassword(int userId, String newPassword) {
        String sql = "UPDATE SYS_USER SET password = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertUser(User user, UserInformation userInfo) {
        String sql = "INSERT INTO SYS_USER (user_name, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                user.setUserId(userId);
                
                // Insert user information
                String infoSql = "INSERT INTO USER_INFORMATION (UserID, full_name, Country) VALUES (?, ?, ?)";
                try (PreparedStatement infoStmt = connection.prepareStatement(infoSql)) {
                    infoStmt.setInt(1, userId);
                    infoStmt.setString(2, userInfo.getFullName());
                    infoStmt.setString(3, userInfo.getCountry());
                    infoStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserInformation getUserInformation(int userId) {
        String sql = "SELECT * FROM USER_INFORMATION WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserInformation info = new UserInformation();
                info.setUserId(rs.getInt("UserID"));
                info.setFullName(rs.getString("full_name"));
                java.sql.Date sqlDate = rs.getDate("date_of_birth");
                if (sqlDate == null || sqlDate.toString().equals("0000-00-00")) {
                    info.setDateOfBirth(java.time.LocalDate.of(2000, 1, 1));
                } else {
                    info.setDateOfBirth(sqlDate.toLocalDate());
                }
                info.setCountry(rs.getString("Country"));
                info.setCity(rs.getString("City"));
                info.setPhoneNumber(rs.getString("PhoneNumber"));
                return info;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isUserNameExists(String userName) {
        String sql = "SELECT COUNT(*) FROM SYS_USER WHERE user_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setUsername(rs.getString("user_name"));
        user.setPassword(rs.getString("password"));
        return user;
    }
} 