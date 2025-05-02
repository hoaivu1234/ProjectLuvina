package com.luvina.la.controller;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.ErrorMessage;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<BaseResponse<List<EmployeeDTO>>> getListEmployees() {
        try {
            List<EmployeeDTO> employeeDTOList = employeeService.getListEmployees();
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse(employeeDTOList.size() ,200, employeeDTOList));
        } catch (SQLException ex) {
            return ResponseEntity.internalServerError()
                    .body(new BaseResponse(500,
                            new ErrorMessage("従業員を取得できません", List.of())));
        }
    }
}
