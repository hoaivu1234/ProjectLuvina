package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.payload.BaseResponse;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {
    public BaseResponse<List<EmployeeDTO>> getListEmployees();
}
