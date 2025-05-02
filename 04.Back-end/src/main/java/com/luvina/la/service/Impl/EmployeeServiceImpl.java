package com.luvina.la.service.Impl;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.sql.SQLException;
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
    private EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDTO> getListEmployees() {
        List<Employee> employees = employeeRepository.getListEmployee();
        return employeeMapper.toList(employees);
    }
}
