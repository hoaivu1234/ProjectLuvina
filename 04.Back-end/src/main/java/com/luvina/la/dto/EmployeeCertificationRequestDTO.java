/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeCertificationRequestDTO.java, 5/16/2025 hoaivd
 */

package com.luvina.la.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) đại diện cho thông tin chứng chỉ cẩn thêm cho nhân viên.
 * Được sử dụng để truyền dữ liệu nhân viên giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */
@Data
public class EmployeeCertificationRequestDTO {
    private String certificationId;
    private String startDate;
    private String endDate;
    private String score;
}
