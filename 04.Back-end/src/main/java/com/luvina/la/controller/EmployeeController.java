package com.luvina.la.controller;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.ErrorMessage;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeController.java, 5/2/2025 hoaivd
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<EmployeeDTO>>> getListEmployees() throws DataAccessException {
        BaseResponse<List<EmployeeDTO>> response = employeeService.getListEmployees();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
