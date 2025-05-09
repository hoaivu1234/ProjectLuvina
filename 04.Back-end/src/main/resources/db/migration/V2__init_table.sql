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
