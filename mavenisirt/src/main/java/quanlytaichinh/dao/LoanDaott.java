	/*
	 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
	 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
	 */
	package quanlytaichinh.dao;
	import quanlytaichinh.dao.LoanDao;
	import quanlytaichinh.model.Purpose;
	import java.util.List;
	import java.util.ArrayList;
	import quanlytaichinh.ketnoidb;
	import quanlytaichinh.model.Loan;
	import java.sql.*;
	/**
	 *
	 * @author 23520
	 */
	public class LoanDaott implements LoanDao {
	    @Override
	    public List<Loan> GetIdUserLoan(int id){
	        System.out.println("kêt nối ");
	        List<Loan> call = new ArrayList();
	        String sql ="SELECT l.loanID, l.UserID,l.PurposeID,l.FormID, l.loan_amount, l.disbur_date,l.interest, l.num_term,l.pay_per_term,l.date_of_payment,l.num_paid_term,l.used_amount,l.paid_amount,l.loan_remain,l.up_date,p.purpose_name FROM LOAN l join PURPOSE p on l.PurposeID =p. PurposeID WHERE l.UserID =?";
	        try (Connection ketnoi = ketnoidb.getConnection();
	                PreparedStatement save = ketnoi.prepareStatement(sql)){
	                save.setInt(1,id);
	                try(ResultSet result = save.executeQuery()){
	                 while (result.next()) {
	                    Loan loan = new Loan();
	                    loan.setLoanId(result.getInt("LoanID"));
	                    loan.setUserId(result.getInt("UserID"));
	                    loan.setPurposeId(result.getInt("PurposeID"));
	                    loan.setFormId(result.getString("FormID"));
	                    loan.setLoanAmount(result.getDouble("loan_amount"));
	                    loan.setDisburDate(result.getDate("disbur_date"));
	                    loan.setInterest(result.getDouble("interest"));
	                    loan.setNumTerm(result.getInt("num_term"));
	                    loan.setPayPerTerm(result.getDouble("pay_per_term"));
	                    loan.setDateOfPayment(result.getInt("date_of_payment"));
	                    loan.setNumPaidTerm(result.getInt("num_paid_term"));
	                    loan.setUsedAmount(result.getDouble("used_amount"));
	                    loan.setPaidAmount(result.getDouble("paid_amount"));
	                    loan.setLoanRemain(result.getDouble("loan_remain"));
	                    loan.setUpDate(result.getDate("up_date"));
	                    loan.setPurpose_name (result.getString("purpose_name"));
	
	                    call.add(loan);
	                }}
	                 
	        }catch(SQLException e){
	            System.err.println(" Lỗi khi lấy danh sách khoản vay ");
	            
	            
	        }
	        return call;
	        
	    }
	
            private Loan mapResultSetToLoan(ResultSet result) throws SQLException {
         Loan loan = new Loan();
         loan.setUserId(result.getInt("UserID"));
         loan.setPurposeId(result.getInt("PurposeID"));
         loan.setFormId(result.getString("FormID"));
         loan.setLoanAmount(result.getDouble("loan_amount"));
         loan.setDisburDate(result.getDate("disbur_date"));
         loan.setInterest(result.getDouble("interest"));
         loan.setNumTerm(result.getInt("num_term"));
         loan.setPayPerTerm(result.getDouble("pay_per_term"));
         loan.setDateOfPayment(result.getInt("date_of_payment"));
         loan.setNumPaidTerm(result.getInt("num_paid_term"));
         loan.setUsedAmount(result.getDouble("used_amount"));
         loan.setPaidAmount(result.getDouble("paid_amount"));
         loan.setLoanRemain(result.getDouble("loan_remain"));
         loan.setUpDate(result.getDate("up_date"));
         loan.setPurpose_name(result.getString("purpose_name"));
         return loan;
    }
	}