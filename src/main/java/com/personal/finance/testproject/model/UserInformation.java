package com.personal.finance.testproject.model;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDate;

public class UserInformation {
    private int userId;
    private String fullName;
    private LocalDate dateOfBirth;
    private String country;
    private String city;
    private String phoneNumber;
    private String occupation;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
    private BigDecimal monthlySave;
    private BigDecimal monthlyInvest;
    private BigDecimal monthlyLoan;
    private BigDecimal monthlyProperty;
    private String riskTolerance;
    private String investmentGoal;
    private Date upDate;
    private String gender;
    private String email;
    private String address;

    // Constructors
    public UserInformation() {}

    public UserInformation(int userId, String fullName, LocalDate dateOfBirth, 
                         String country, String city, String phoneNumber) {
        this.userId = userId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }

    public UserInformation(int userId, String occupation, BigDecimal monthlyIncome,
                          BigDecimal monthlyExpense, BigDecimal monthlySave,
                          BigDecimal monthlyInvest, BigDecimal monthlyLoan,
                          BigDecimal monthlyProperty, String riskTolerance,
                          String investmentGoal, Date upDate) {
        this.userId = userId;
        this.occupation = occupation;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpense = monthlyExpense;
        this.monthlySave = monthlySave;
        this.monthlyInvest = monthlyInvest;
        this.monthlyLoan = monthlyLoan;
        this.monthlyProperty = monthlyProperty;
        this.riskTolerance = riskTolerance;
        this.investmentGoal = investmentGoal;
        this.upDate = upDate;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }
        if (fullName.length() > 100) {
            throw new IllegalArgumentException("Full name cannot exceed 100 characters");
        }
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        if (!phoneNumber.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Phone number must be 10 digits");
        }
        this.phoneNumber = phoneNumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        if (occupation == null || occupation.trim().isEmpty()) {
            throw new IllegalArgumentException("Occupation cannot be null or empty");
        }
        this.occupation = occupation;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly income cannot be negative");
        }
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyExpense() {
        return monthlyExpense;
    }

    public void setMonthlyExpense(BigDecimal monthlyExpense) {
        if (monthlyExpense == null || monthlyExpense.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly expense cannot be negative");
        }
        this.monthlyExpense = monthlyExpense;
    }

    public BigDecimal getMonthlySave() {
        return monthlySave;
    }

    public void setMonthlySave(BigDecimal monthlySave) {
        if (monthlySave == null || monthlySave.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly save cannot be negative");
        }
        this.monthlySave = monthlySave;
    }

    public BigDecimal getMonthlyInvest() {
        return monthlyInvest;
    }

    public void setMonthlyInvest(BigDecimal monthlyInvest) {
        if (monthlyInvest == null || monthlyInvest.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly invest cannot be negative");
        }
        this.monthlyInvest = monthlyInvest;
    }

    public BigDecimal getMonthlyLoan() {
        return monthlyLoan;
    }

    public void setMonthlyLoan(BigDecimal monthlyLoan) {
        if (monthlyLoan == null || monthlyLoan.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly loan cannot be negative");
        }
        this.monthlyLoan = monthlyLoan;
    }

    public BigDecimal getMonthlyProperty() {
        return monthlyProperty;
    }

    public void setMonthlyProperty(BigDecimal monthlyProperty) {
        if (monthlyProperty == null || monthlyProperty.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Monthly property cannot be negative");
        }
        this.monthlyProperty = monthlyProperty;
    }

    public String getRiskTolerance() {
        return riskTolerance;
    }

    public void setRiskTolerance(String riskTolerance) {
        if (riskTolerance == null || riskTolerance.trim().isEmpty()) {
            throw new IllegalArgumentException("Risk tolerance cannot be null or empty");
        }
        this.riskTolerance = riskTolerance;
    }

    public String getInvestmentGoal() {
        return investmentGoal;
    }

    public void setInvestmentGoal(String investmentGoal) {
        if (investmentGoal == null || investmentGoal.trim().isEmpty()) {
            throw new IllegalArgumentException("Investment goal cannot be null or empty");
        }
        this.investmentGoal = investmentGoal;
    }

    public Date getUpDate() {
        return upDate;
    }

    public void setUpDate(Date upDate) {
        if (upDate == null) {
            throw new IllegalArgumentException("Update date cannot be null");
        }
        this.upDate = upDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        if (!gender.matches("^(Male|Female|Other)$")) {
            throw new IllegalArgumentException("Gender must be Male, Female, or Other");
        }
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null && address.length() > 200) {
            throw new IllegalArgumentException("Address cannot exceed 200 characters");
        }
        this.address = address;
    }

    // Alias methods for compatibility
    public String getName() {
        return getFullName();
    }

    public void setName(String name) {
        setFullName(name);
    }

    public LocalDate getBirthDate() {
        return getDateOfBirth();
    }

    public void setBirthDate(LocalDate birthDate) {
        setDateOfBirth(birthDate);
    }

    public String getPhone() {
        return getPhoneNumber();
    }

    public void setPhone(String phone) {
        setPhoneNumber(phone);
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", occupation='" + occupation + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                ", monthlyExpense=" + monthlyExpense +
                ", monthlySave=" + monthlySave +
                ", monthlyInvest=" + monthlyInvest +
                ", monthlyLoan=" + monthlyLoan +
                ", monthlyProperty=" + monthlyProperty +
                ", riskTolerance='" + riskTolerance + '\'' +
                ", investmentGoal='" + investmentGoal + '\'' +
                ", upDate=" + upDate +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
} 