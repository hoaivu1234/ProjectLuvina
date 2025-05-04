package com.luvina.la.repository;

import com.luvina.la.entity.Employee;
import com.luvina.la.common.EmployeeRole;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
        WHERE e.employeeRole = :role
        ORDER BY e.employeeName
        """)
    List<Employee> getListEmployees(@Param("role") EmployeeRole role);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.employeeRole = :role")
    int getCountEmployee(@Param("role") EmployeeRole role);

}
