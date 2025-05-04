package com.luvina.la.service.Impl;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.ErrorMessage;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public BaseResponse<List<EmployeeDTO>> getListEmployees(){
        try {
            List<Employee> employees = employeeRepository.getListEmployees(EmployeeRole.USER);
            int count = employeeRepository.getCountEmployee(EmployeeRole.USER);
            return new BaseResponse<>(count, 200, employeeMapper.toList(employees));
        } catch (DataAccessException ex) {
            return new BaseResponse<>(
                    500,
                    new ErrorMessage("従業員を取得できません", List.of())
            );
        }
    }
}
