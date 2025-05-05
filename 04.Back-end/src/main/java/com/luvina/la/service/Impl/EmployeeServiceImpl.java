package com.luvina.la.service.Impl;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.payload.ErrorMessage;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeServiceImpl.java, 4/29/2025 hoaivd
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InputValidator inputValidator;
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse<List<EmployeeDTO>> getListEmployees(String employeeName, String departmentId) {
        employeeName = inputValidator.validateEmployeeName(employeeName);
        Long id = inputValidator.validateDepartmentId(departmentId);

        List<Employee> employees = employeeRepository.getListEmployees(EmployeeRole.USER, employeeName, id);
        int count = employeeRepository.getCountEmployee(EmployeeRole.USER, employeeName, id);
        return new EmployeeResponse<>(count, 200, employeeMapper.toList(employees));
    }
}
