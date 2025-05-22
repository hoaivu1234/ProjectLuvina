package com.luvina.la.repository;

import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeCertificationRepository extends JpaRepository<EmployeeCertification, Long> {
    List<EmployeeCertification> findByEmployee(Employee employee);

    void deleteByEmployee_EmployeeId(Long employeeId);
}
