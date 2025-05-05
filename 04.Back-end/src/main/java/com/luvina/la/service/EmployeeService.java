package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.EmployeeResponse;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {
    public EmployeeResponse<List<EmployeeDTO>> getListEmployees(String employeeName, String departmentId);
}
