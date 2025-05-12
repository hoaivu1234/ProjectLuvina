/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentDTO.java, 5/2/2025 hoaivd
 */

package com.luvina.la.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) đại diện cho thông tin phòng ban.
 * Được sử dụng để truyền dữ liệu giữa các tầng controller và service.
 *
 * @author hoaivd
 */
@Getter
@Setter
public class DepartmentDTO {
    // Mã định danh của phòng ban
    private Long departmentId;

    // Tên của phòng ban.
    private String departmentName;
}

