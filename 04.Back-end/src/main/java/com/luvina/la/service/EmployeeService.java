package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.dto.EmployeeRequestDTO;
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

    EmployeeResponse<Long> addEmployee(EmployeeRequestDTO requestDTO);

    boolean existsByEmployeeLoginId(String employeeLoginId);

    boolean existsByEmployeeEmail(String employeeEmail);;
}
