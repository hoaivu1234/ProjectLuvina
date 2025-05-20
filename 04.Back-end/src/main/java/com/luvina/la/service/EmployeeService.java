package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.dto.EmployeeInsertDTO;
import com.luvina.la.dto.EmployeeResponseDTO;
import com.luvina.la.dto.EmployeeUpdateDTO;
import com.luvina.la.payload.EmployeeResponse;

import java.util.List;

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

    EmployeeResponse<Long> addEmployee(EmployeeInsertDTO requestDTO);

    EmployeeResponse<Long> updateEmployee(EmployeeUpdateDTO updateDTO);

    boolean existsByEmployeeLoginId(String employeeLoginId);

    boolean existsByEmployeeEmail(String employeeEmail);

    EmployeeResponseDTO getEmployeeById(Long id);

    EmployeeResponse<Long> deleteEmployeeById(Long id);

    boolean existsById(Long id);
}
