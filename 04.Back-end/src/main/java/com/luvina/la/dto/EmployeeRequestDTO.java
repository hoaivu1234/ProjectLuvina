/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRequest.java, 5/16/2025 hoaivd
 */

package com.luvina.la.dto;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * DTO (Data Transfer Object) đại diện cho thông tin employee cần thêm.
 * Được sử dụng để truyền dữ liệu nhân viên giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */
@Data
public class EmployeeRequestDTO {
    private String employeeId;
    private String employeeName;
    private String employeeBirthDate;
    private String employeeEmail;
    private String employeeTelephone;
    private String employeeNameKana;
    private String employeeLoginId;
    private String employeeLoginPassword;
    private String departmentId;
    private List<EmployeeCertificationRequestDTO> certifications;
}
