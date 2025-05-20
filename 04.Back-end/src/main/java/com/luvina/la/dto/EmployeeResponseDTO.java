/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeResponseDTO.java, 5/19/2025 hoaivd
 */

package com.luvina.la.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * DTO (Data Transfer Object) đại diện cho thông tin employee cần trả về.
 * Được sử dụng để truyền dữ liệu nhân viên giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */

@Getter
@Setter
public class EmployeeResponseDTO {
    private int code;
    private Long employeeId;
    private String employeeName;
    private Date employeeBirthDate;
    private Long departmentId;
    private String departmentName;
    private String employeeEmail;
    private String employeeTelephone;
    private String employeeNameKana;
    private String employeeLoginId;

    private List<EmployeeCertificationResponseDTO> certifications;
}
