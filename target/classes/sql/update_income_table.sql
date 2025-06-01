-- Cập nhật cấu trúc bảng INCOME
ALTER TABLE INCOME
ADD COLUMN income_date DATE AFTER UserID,
ADD COLUMN salary DECIMAL(15,2) DEFAULT 0.00 AFTER income_date,
ADD COLUMN allowance DECIMAL(15,2) DEFAULT 0.00 AFTER salary,
ADD COLUMN description VARCHAR(255) AFTER allowance;

-- Cập nhật dữ liệu từ cấu trúc cũ sang mới
UPDATE INCOME i
JOIN (
    SELECT UserID, income_name, income_amount, income_date
    FROM INCOME
    WHERE income_name IN ('Lương', 'Phụ cấp')
) old ON i.UserID = old.UserID
SET i.salary = CASE WHEN old.income_name = 'Lương' THEN old.income_amount ELSE 0 END,
    i.allowance = CASE WHEN old.income_name = 'Phụ cấp' THEN old.income_amount ELSE 0 END,
    i.description = CONCAT('Chuyển đổi từ bản ghi cũ: ', old.income_name);

-- Xóa các cột không còn sử dụng
ALTER TABLE INCOME
DROP COLUMN income_name,
DROP COLUMN income_amount,
DROP COLUMN remain_income;

-- Thêm các ràng buộc
ALTER TABLE INCOME
MODIFY COLUMN income_date DATE NOT NULL,
MODIFY COLUMN salary DECIMAL(15,2) NOT NULL DEFAULT 0.00,
MODIFY COLUMN allowance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
ADD CONSTRAINT chk_salary CHECK (salary >= 0),
ADD CONSTRAINT chk_allowance CHECK (allowance >= 0); 