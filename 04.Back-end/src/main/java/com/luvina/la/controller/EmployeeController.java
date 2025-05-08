package com.luvina.la.controller;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.payload.ErrorMessage;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.sql.SQLException;
import java.util.ArrayList;
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

    @Autowired
    private InputValidator inputValidator;

    @GetMapping("")
    public ResponseEntity<EmployeeResponse<List<EmployeeDTO>>> getListEmployees(
            @RequestParam(name = "employee_name", required = false, defaultValue = "") String employeeName,
            @RequestParam(name = "department_id", required = false, defaultValue = "") String departmentId,
            @RequestParam(name = "ord_employee_name", required = false, defaultValue = "") String ordEmployeeName,
            @RequestParam(name = "ord_certification_name", required = false, defaultValue = "") String ordCertificationName,
            @RequestParam(name = "ord_end_date", required = false, defaultValue = "") String ordEndDate,
            @RequestParam(name = "sort_priority", required = false, defaultValue = "") String sortPriority,
            @RequestParam(name = "offset", required = false, defaultValue = "") String offset,
            @RequestParam(name = "limit", required = false, defaultValue = "") String limit) throws DataAccessException {

        // validate input params
        employeeName = EscapeCharacter.DEFAULT.escape(employeeName);
        Long departmentIdNumber = inputValidator.validateDepartmentId(departmentId);

        // Validate sort order
        ordEmployeeName = inputValidator.validateSortOrder(ordEmployeeName);
        ordCertificationName = inputValidator.validateSortOrder(ordCertificationName);
        ordEndDate = inputValidator.validateSortOrder(ordEndDate);

        int offsetNumber;
        int limitNumber;

        // validate paging param
        offsetNumber = offset.isEmpty() ? 0 : inputValidator.validatePositiveNumber(offset, "オフセット");
        limitNumber = limit.isEmpty() ? 5 : inputValidator.validatePositiveNumber(limit, "リミット");

        // Lấy tổng số bản ghi response
        int count = employeeService.getCountEmployee(employeeName, departmentIdNumber);

        // Khởi tạo response
        EmployeeResponse<List<EmployeeDTO>> response = new EmployeeResponse<>(count, 200, new ArrayList<>());
        if (count > 0) {
            response = employeeService.getListEmployees(employeeName, departmentIdNumber, ordEmployeeName, ordCertificationName,
                    ordEndDate, sortPriority, offsetNumber, limitNumber);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
