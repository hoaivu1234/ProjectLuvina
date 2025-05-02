package com.luvina.la.repository;

import com.luvina.la.entity.Employee;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);
    Optional<Employee> findByEmployeeId(Long employeeId);
//    e.employee_id, e.employee_name, e.employee_birthdate, e.employee_email, e.telephone, d.department_name" +
//            "c.certification_name, ec.end_date, ec.score
//    @Query(value = "select e from Employee e order by e.employeeName ASC")
@Query(value = """
    SELECT 
        e.*, 
        d.department_name AS departmentName, 
        c.certification_name AS certificationName,
        ec.end_date AS endDate,
        ec.score AS score
    FROM employees e 
    LEFT JOIN departments d ON e.department_id = d.department_id 
    LEFT JOIN employees_certifications ec ON e.employee_id = ec.employee_id 
    LEFT JOIN certifications c ON ec.certification_id = c.certification_id
    """, nativeQuery = true)
List<Employee> getListEmployee();
}
