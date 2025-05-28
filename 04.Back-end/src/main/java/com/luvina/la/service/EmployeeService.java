package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.dto.EmployeeRequestDTO;
import com.luvina.la.dto.EmployeeResponseDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.payload.EmployeeResponse;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeResponse<List<EmployeeDTO>> getListEmployees(
            String employeeName,
            Long departmentId,
            String ordEmployeeName,
            String ordCertificationName,
            String ordEndDate,
            String sortPriority,
            int offset,
            int limit);

    int getCountEmployee(String employeeName, Long departmentId);

    EmployeeResponse<Long> addEmployee(EmployeeRequestDTO requestDTO);

    EmployeeResponse<Long> updateEmployee(EmployeeRequestDTO updateDTO);

    boolean existsByEmployeeLoginId(String employeeLoginId);

    boolean existsByEmployeeEmail(String employeeEmail);

    EmployeeResponseDTO getEmployeeById(Employee employee);

    EmployeeResponse<Long> deleteEmployeeById(Employee employee);

    boolean existsById(Long id);

    String getEmployeeLoginIdById(Long id);

    String getEmployeeEmailById(Long id);

    Optional<Employee> findByEmployeeId(Long employeeId);
}
