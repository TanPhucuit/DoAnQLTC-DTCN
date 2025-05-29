-- Tạo bảng INVEST_TRANSACTION
CREATE TABLE INVEST_TRANSACTION (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    InStID VARCHAR(20) NOT NULL,
    UserID INT NOT NULL,
    transaction_type ENUM('BUY', 'SELL') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    source_type ENUM('SALARY', 'ALLOWANCE') NULL,
    transaction_date DATE NOT NULL,
    ic_month INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (InStID, UserID) REFERENCES INVEST_STORAGE(InStID, UserID),
    FOREIGN KEY (UserID) REFERENCES SYS_USER(UserID),
    CONSTRAINT chk_amount CHECK (amount > 0),
    CONSTRAINT chk_ic_month CHECK (ic_month IS NULL OR (ic_month >= 1 AND ic_month <= 12)),
    CONSTRAINT chk_source_type CHECK (
        (transaction_type = 'BUY' AND source_type IS NOT NULL) OR
        (transaction_type = 'SELL' AND source_type IS NULL)
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tạo index
CREATE INDEX idx_invest_transaction_user ON INVEST_TRANSACTION(UserID);
CREATE INDEX idx_invest_transaction_date ON INVEST_TRANSACTION(transaction_date);
CREATE INDEX idx_invest_transaction_type ON INVEST_TRANSACTION(transaction_type);
CREATE INDEX idx_invest_transaction_month ON INVEST_TRANSACTION(ic_month); 