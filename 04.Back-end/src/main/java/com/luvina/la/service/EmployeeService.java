package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {
    public List<EmployeeDTO> getListEmployees() throws SQLException;
}
