package com.luvina.la.controller;

import com.luvina.la.payload.BaseResponse;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.payload.ErrorMessage;
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
        try {
            List<DepartmentDTO> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(new BaseResponse(200, departments));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new BaseResponse(500,
                            new ErrorMessage("部門を取得できません", List.of())));
        }
    }

}
