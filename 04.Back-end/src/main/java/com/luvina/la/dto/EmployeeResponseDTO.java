package com.luvina.la.dto;

import java.util.Date;
import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeResponseDTO.java, 5/19/2025 hoaivd
 */
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
