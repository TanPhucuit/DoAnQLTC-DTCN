package com.personal.finance.testproject.dao;

import com.personal.finance.testproject.model.FinancialStatus;
import java.sql.SQLException;
import java.util.List;

public interface FinanceStatusDAO {
 
    boolean checkOverMaxAmount(int userId) throws SQLException;
    boolean checkBadAllocation(int userId) throws SQLException;
} 