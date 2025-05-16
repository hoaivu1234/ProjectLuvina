/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRequest.java, 5/16/2025 hoaivd
 */

package com.luvina.la.dto;

import com.luvina.la.common.EmployeeValidationConstant;
import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.payload.CertificationRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class EmployeeRequestDTO {
    @NotEmpty(message = ErrorCodeConstants.ER001)
    private String employeeName;

    @NotEmpty(message = ErrorCodeConstants.ER001)
    private String employeeBirthDate;

    @NotEmpty(message = ErrorCodeConstants.ER001)
    private String employeeEmail;

    @NotEmpty(message = ErrorCodeConstants.ER001)
    private String employeeTelephone;

    @NotEmpty(message = ErrorCodeConstants.ER001)
    private String employeeNameKana;

    @NotEmpty(message = ErrorCodeConstants.ER001)
    @Size(max = 50, message = ErrorCodeConstants.ER006)
    @Pattern(regexp = EmployeeValidationConstant.LOGIN_ID_REGEX, message = ErrorCodeConstants.ER019)
    private String employeeLoginId;

    @NotEmpty(message = ErrorCodeConstants.ER001)
    private String employeeLoginPassword;

    @NotEmpty(message = ErrorCodeConstants.ER001)
    private String departmentId;

    private List<CertificationRequest> certifications;
}
