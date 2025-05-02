package com.luvina.la.controller;

import com.luvina.la.payload.BaseResponse;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentController.java, 5/2/2025 hoaivd
 */

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<DepartmentDTO>>> getAllDepartment() {
        List<DepartmentDTO> departmentDTOList = departmentService.getAllDepartments();
        BaseResponse<List<DepartmentDTO>> baseResponse = new BaseResponse<>(200, departmentDTOList);
        return ResponseEntity.ok(baseResponse);
    }

}
