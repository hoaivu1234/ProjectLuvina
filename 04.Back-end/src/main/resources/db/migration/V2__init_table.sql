CREATE TABLE IF NOT EXISTS `departments` (
    department_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    department_name varchar(50) NOT NULL,
    PRIMARY KEY (`department_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `certifications` (
    certification_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    certification_name varchar(50) NOT NULL,
    certification_level INT NOT NULL,
    PRIMARY KEY (`certification_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `employees_certifications` (
    employee_certification_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    employee_id BIGINT(20) NOT NULL,
    certification_id BIGINT(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    score DECIMAL NOT NULL,
    PRIMARY KEY (employee_certification_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id),
    FOREIGN KEY (certification_id) REFERENCES certifications(certification_id),
    CONSTRAINT chk_dates CHECK (end_date > start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Thêm dữ liệu vào bảng certifications
INSERT INTO certifications (certification_name, certification_level) VALUES
('Trình độ tiếng Nhật cấp 1', 1),
('Trình độ tiếng Nhật cấp 2', 2),
('Trình độ tiếng Nhật cấp 3', 3),
('Trình độ tiếng Nhật cấp 4', 4),
('Trình độ tiếng Nhật cấp 5', 5);

-- Thêm dữ liệu vào bảng departments
INSERT INTO departments (department_name) VALUES
('Phòng Nhân sự'),
('Phòng Kỹ thuật'),
('Phòng Kinh doanh'),
('Phòng Marketing'),
('Phòng Hỗ trợ'),
('Phòng Tài chính'),
('Phòng IT'),
('Phòng Phát triển sản phẩm'),
('Phòng Bán hàng'),
('Phòng Đào tạo'),
('Phòng Dịch vụ khách hàng'),
('Phòng Nghiên cứu'),
('Phòng Quản lý dự án'),
('Phòng Pháp lý'),
('Phòng Chiến lược'),
('Phòng Mua sắm'),
('Phòng Quan hệ công chúng'),
('Phòng Quản lý chất lượng'),
('Phòng An ninh'),
('Phòng Đối ngoại');

-- Thêm dữ liệu vào bảng employees
INSERT INTO employees (department_id, employee_name, employee_name_kana, employee_birth_date, employee_email, employee_telephone, employee_role, employee_login_id, employee_login_password) VALUES
(8, 'Phạm Thị H', 'く', '1997-08-08', 'h@example.com', '0123456782', 0, 'userH', 'passwordH'),
(9, 'Nguyễn Văn I', 'け', '1998-09-09', 'i@example.com', '0123456781', 0, 'userI', 'passwordI'),
(10, 'Trần Thị J', 'こ', '1999-10-10', 'j@example.com', '0123456780', 0, 'userJ', 'passwordJ'),
(11, 'Lê Văn K', 'さ', '2000-11-11', 'k@example.com', '0123456790', 0, 'userK', 'passwordK'),
(12, 'Phạm Thị L', 'し', '2001-12-12', 'l@example.com', '0123456700', 0, 'userL', 'passwordL'),
(13, 'Nguyễn Văn M', 'す', '2002-01-13', 'm@example.com', '0123456711', 0, 'userM', 'passwordM'),
(14, 'Trần Thị N', 'せ', '2003-02-14', 'n@example.com', '0123456722', 0, 'userN', 'passwordN'),
(15, 'Lê Văn O', 'そ', '2004-03-15', 'o@example.com', '0123456733', 0, 'userO', 'passwordO'),
(16, 'Phạm Thị P', 'た', '2005-04-16', 'p@example.com', '0123456744', 0, 'userP', 'passwordP'),
(17, 'Nguyễn Văn Q', 'ち', '2006-05-17', 'q@example.com', '0123456755', 0, 'userQ', 'passwordQ'),
(18, 'Trần Thị R', 'つ', '2007-06-18', 'r@example.com', '0123456766', 0, 'userR', 'passwordR'),
(19, 'Lê Văn S', 'て', '2008-07-19', 's@example.com', '0123456777', 0, 'userS', 'passwordS'),
(20, 'Phạm Thị T', 'と', '2009-08-20', 't@example.com', '0123456788', 0, 'userT', 'passwordT'),
(1, 'Nguyễn /Văn A', 'あ', '1990-01-01', 'a@example.com', '0123456789', 0, 'userA', 'passwordA'),
(2, 'Trần /Thị- B', 'い', '1991-02-02', 'b@example.com', '0123456788', 0, 'userB', 'passwordB'),
(3, 'Lê ,Văn__ C', 'う', '1992-03-03', 'c@example.com', '0123456787', 0, 'userC', 'passwordC'),
(4, 'Phạm Thị; D', 'え', '1993-04-04', 'd@example.com', '0123456786', 0, 'userD', 'passwordD'),
(5, 'Nguyễn Vă,n E', 'お', '1994-05-05', 'e@example.com', '0123456785', 0, 'userE', 'passwordE'),
(6, 'Trần Thị %F', 'か', '1995-06-06', 'f@example.com', '0123456784', 0, 'userF', 'passwordF'),
(7, 'Lê Văn% G', 'き', '1996-07-07', 'g@example.com', '0123456783', 0, 'userG', 'passwordG');

-- Thêm dữ liệu vào bảng employees_certifications
INSERT INTO employees_certifications (employee_id, certification_id, start_date, end_date, score) VALUES
-- Các bản ghi đã hết hạn (end_date < 2025-05-07)
(1, 1, '2020-03-15', '2021-03-14', 85),
(2, 1, '2019-11-10', '2020-11-09', 80),
(3, 2, '2020-08-12', '2021-08-11', 75),
(10, 3, '2020-10-15', '2021-10-14', 70),
(17, 2, '2020-04-05', '2021-04-04', 92),

-- Các bản ghi còn hiệu lực (end_date >= 2025-05-07)
(21, 2, '2024-06-20', '2026-06-19', 90),
(2, 3, '2024-02-05', '2026-02-04', 70),
(4, 1, '2024-04-30', '2026-04-29', 88),
(5, 4, '2024-07-22', '2026-07-21', 92),
(6, 3, '2024-01-18', '2026-01-17', 78),
(7, 2, '2024-05-09', '2026-05-08', 82),
(8, 1, '2024-09-25', '2026-09-24', 89),
(9, 5, '2024-12-01', '2026-11-30', 95),
(11, 2, '2024-03-08', '2026-03-07', 76),
(12, 1, '2024-07-14', '2026-07-13', 91),
(13, 4, '2024-12-05', '2026-12-04', 84),
(14, 5, '2024-08-20', '2026-08-19', 79),
(15, 4, '2024-02-28', '2026-02-27', 87),
(16, 3, '2024-11-11', '2026-11-10', 81),
(18, 1, '2024-01-30', '2026-01-29', 90),
(19, 5, '2024-06-15', '2026-06-14', 88),
(20, 4, '2024-09-22', '2026-09-21', 85),
