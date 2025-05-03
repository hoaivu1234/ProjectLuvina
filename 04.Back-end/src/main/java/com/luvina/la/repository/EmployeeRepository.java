package com.luvina.la.repository;

import com.luvina.la.entity.Employee;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);
    Optional<Employee> findByEmployeeId(Long employeeId);

    @Query("""
    SELECT DISTINCT e
    FROM Employee e
    INNER JOIN FETCH e.department d
    LEFT JOIN FETCH e.employeeCertifications ec
    LEFT JOIN FETCH ec.certification c
    ORDER BY e.employeeName
    """)
    List<Employee> getListEmployees();
}
