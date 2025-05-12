/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentController.java, 5/2/2025 hoaivd
 */

package com.luvina.la.controller;

import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.payload.DepartmentResponse;
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
 * Controller xử lý các yêu cầu liên quan đến phòng ban (departments).
 * Cung cấp các endpoint để lấy danh sách phòng ban từ service và trả về thông tin cho client.
 *
 * @author hoaivd
 */
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * Lấy danh sách tất cả các phòng ban từ service và trả về response với mã trạng thái OK.
     *
     * @return ResponseEntity<DepartmentResponse<List<DepartmentDTO>>> đối tượng chứa mã trạng thái và danh sách các phòng ban
     */
    @GetMapping("")
    public ResponseEntity<DepartmentResponse<List<DepartmentDTO>>> getAllDepartment() {
        // Lấy danh sách các phòng ban từ dịch vụ
        List<DepartmentDTO> departments = departmentService.getAllDepartments();

        // Trả về phản hồi với mã trạng thái OK và dữ liệu phòng ban
        return ResponseEntity.ok(new DepartmentResponse(HttpStatusConstants.OK, departments));
    }
}
