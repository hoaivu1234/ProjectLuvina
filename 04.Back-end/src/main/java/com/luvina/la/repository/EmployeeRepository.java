package com.luvina.la.repository;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.common.EmployeeRole;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {

    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);

    Optional<Employee> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeLoginId(String employeeLoginId);

    boolean existsByEmployeeEmail(String employeeEmail);

    @Query("""
            SELECT COUNT(e) 
            FROM Employee e
            INNER JOIN e.department d
            WHERE e.employeeRole = :role
              AND (:name IS NULL OR e.employeeName LIKE %:name%)
              AND (:departmentId IS NULL OR d.departmentId = :departmentId)
            """)
    int getCountEmployee(
            @Param("role") EmployeeRole role,
            @Param("name") @Nullable String name,
            @Param("departmentId") @Nullable Long departmentId);

}
