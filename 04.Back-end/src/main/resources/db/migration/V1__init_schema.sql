CREATE TABLE IF NOT EXISTS `employees` (
    employee_id bigint(20) NOT NULL AUTO_INCREMENT,
    department_id bigint(20) NOT NULL,
    employee_name VARCHAR(255) NOT NULL,
    employee_name_kana VARCHAR(255),
    employee_birth_date DATE,
    employee_email VARCHAR(255) NOT NULL,
    employee_telephone VARCHAR(50),
    employee_role SMALLINT NOT NULL,
    sort_priority VARCHAR(50),
    employee_login_id VARCHAR(50) NOT NULL,
    employee_login_password VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (`employee_id`) USING BTREE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO employees (department_id, employee_name, employee_email, employee_role, employee_login_id, employee_login_password)
VALUES (1, 'Administrator', 'la@luvina.net', 1 , 'admin', '$2a$10$r.XIN4K9vTioiuYQwaTop.UVQ5r5FvrKk2V5Orm9Hc6n4i9Tvjthy');