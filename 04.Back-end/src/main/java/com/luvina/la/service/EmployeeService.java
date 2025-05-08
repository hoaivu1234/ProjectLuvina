package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.EmployeeResponse;
import org.springframework.data.domain.Sort;

import java.sql.SQLException;
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
}
