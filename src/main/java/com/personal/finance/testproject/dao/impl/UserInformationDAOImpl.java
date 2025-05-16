package com.personal.finance.testproject.dao.impl;

import com.personal.finance.testproject.dao.UserInformationDAO;
import com.personal.finance.testproject.model.UserInformation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserInformationDAOImpl implements UserInformationDAO {
    private Connection connection;

    public UserInformationDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(UserInformation userInformation) {
        String sql = "INSERT INTO USER_INFORMATION (UserID, full_name, date_of_birth, Country, City, PhoneNumber) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userInformation.getUserId());
            stmt.setString(2, userInformation.getFullName());
            stmt.setDate(3, java.sql.Date.valueOf(userInformation.getDateOfBirth()));
            stmt.setString(4, userInformation.getCountry());
            stmt.setString(5, userInformation.getCity());
            stmt.setString(6, userInformation.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting user information", e);
        }
    }

    @Override
    public UserInformation findByUserId(int userId) {
        String sql = "SELECT * FROM USER_INFORMATION WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserInformation(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user information by user ID", e);
        }
        return null;
    }

    @Override
    public List<UserInformation> findAll() {
        List<UserInformation> userInformations = new ArrayList<>();
        String sql = "SELECT * FROM USER_INFORMATION";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                userInformations.add(mapResultSetToUserInformation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all user information", e);
        }
        return userInformations;
    }

    @Override
    public void update(UserInformation userInformation) {
        String sql = "UPDATE USER_INFORMATION SET full_name = ?, date_of_birth = ?, " +
                    "Country = ?, City = ?, PhoneNumber = ? WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userInformation.getFullName());
            stmt.setDate(2, java.sql.Date.valueOf(userInformation.getDateOfBirth()));
            stmt.setString(3, userInformation.getCountry());
            stmt.setString(4, userInformation.getCity());
            stmt.setString(5, userInformation.getPhoneNumber());
            stmt.setInt(6, userInformation.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user information", e);
        }
    }

    @Override
    public void delete(int userId) {
        String sql = "DELETE FROM USER_INFORMATION WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user information", e);
        }
    }

    @Override
    public List<UserInformation> findByCountry(String country) {
        List<UserInformation> userInformations = new ArrayList<>();
        String sql = "SELECT * FROM USER_INFORMATION WHERE Country = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, country);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userInformations.add(mapResultSetToUserInformation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user information by country", e);
        }
        return userInformations;
    }

    @Override
    public List<UserInformation> findByCity(String city) {
        List<UserInformation> userInformations = new ArrayList<>();
        String sql = "SELECT * FROM USER_INFORMATION WHERE City = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, city);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    userInformations.add(mapResultSetToUserInformation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user information by city", e);
        }
        return userInformations;
    }

    private UserInformation mapResultSetToUserInformation(ResultSet rs) throws SQLException {
        UserInformation userInfo = new UserInformation();
        userInfo.setUserId(rs.getInt("UserID"));
        userInfo.setFullName(rs.getString("full_name"));
        java.sql.Date dob = rs.getDate("date_of_birth");
        if (dob == null || dob.toString().equals("0000-00-00")) {
            userInfo.setDateOfBirth(java.time.LocalDate.of(2000, 1, 1));
        } else {
            userInfo.setDateOfBirth(dob.toLocalDate());
        }
        userInfo.setCountry(rs.getString("Country"));
        userInfo.setCity(rs.getString("City"));
        userInfo.setPhoneNumber(rs.getString("PhoneNumber"));
        userInfo.setOccupation(rs.getString("occupation"));
        userInfo.setMonthlyIncome(rs.getBigDecimal("monthly_income"));
        userInfo.setMonthlyExpense(rs.getBigDecimal("monthly_expense"));
        userInfo.setMonthlySave(rs.getBigDecimal("monthly_save"));
        userInfo.setMonthlyInvest(rs.getBigDecimal("monthly_invest"));
        userInfo.setMonthlyLoan(rs.getBigDecimal("monthly_loan"));
        userInfo.setMonthlyProperty(rs.getBigDecimal("monthly_property"));
        userInfo.setRiskTolerance(rs.getString("risk_tolerance"));
        userInfo.setInvestmentGoal(rs.getString("investment_goal"));
        java.sql.Date upDate = rs.getDate("up_date");
        if (upDate == null || upDate.toString().equals("0000-00-00")) {
            userInfo.setUpDate(java.sql.Date.valueOf("2000-01-01"));
        } else {
            userInfo.setUpDate(upDate);
        }
        return userInfo;
    }
} 