/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeUpdateDTO.java, 5/20/2025 hoaivd
 */

package com.luvina.la.dto;

import com.luvina.la.common.EmployeeValidationConstant;
import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.validator.*;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO (Data Transfer Object) đại diện cho thông tin employee cần sửa.
 * Được sử dụng để truyền dữ liệu nhân viên giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */
@Data
public class EmployeeUpdateDTO {
    @ExistsEmployeeId(message = ErrorCodeConstants.ER001)
    @Size(max = EmployeeValidationConstant.LENGTH_125, message = ErrorCodeConstants.ER006) // Kiểm tra max length
    private String employeeId;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    @Size(max = EmployeeValidationConstant.LENGTH_125, message = ErrorCodeConstants.ER006) // Kiểm tra max length
    private String employeeName;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    // Kiểm tra định dạng hợp lệ: yyyy/MM/dd
    @Pattern(regexp = EmployeeValidationConstant.DATE_FORMAT_REGEX, message = ErrorCodeConstants.ER005)
    @ValidDate(message = ErrorCodeConstants.ER011) // Kiểm tra ngày tháng hợp lệ. Ví dụ 2025/02/31 không hợp lệ nhưng đúng format
    private String employeeBirthDate;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    @Size(max = EmployeeValidationConstant.LENGTH_125, message = ErrorCodeConstants.ER006) // Kiểm tra max length
    // Kiểm tra định dạng email phải có @ , sau @ có dấu .
    @Pattern(regexp = EmployeeValidationConstant.EMAIL_FORMAT_REGEX, message = ErrorCodeConstants.ER005)
    @UniqueEmployeeEmail(message = ErrorCodeConstants.ER003)
    private String employeeEmail;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    @Size(max = EmployeeValidationConstant.LENGTH_50, message = ErrorCodeConstants.ER006) // Kiểm tra max length
    @Pattern(regexp = EmployeeValidationConstant.NUMBER_HALF_SIZE_REGEX, message = ErrorCodeConstants.ER008)
    private String employeeTelephone;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    @Size(max = EmployeeValidationConstant.LENGTH_125, message = ErrorCodeConstants.ER006) // Kiểm tra max length
    // Kiểm tra định dạng thỏa mãn: kana halfsize
    @Pattern(regexp = EmployeeValidationConstant.HALF_WIDTH_KANA_REGEX, message = ErrorCodeConstants.ER009)
    private String employeeNameKana;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    @Size(max = EmployeeValidationConstant.LENGTH_50, message = ErrorCodeConstants.ER006) // Kiểm tra max length
    // Kiểm tra định dạng thỏa mãn : [a-zA-Z0-0_] và không bắt đầu bằng số
    @Pattern(regexp = EmployeeValidationConstant.LOGIN_ID_REGEX, message = ErrorCodeConstants.ER019)
    @UniqueEmployeeLoginId(message = ErrorCodeConstants.ER003)
    private String employeeLoginId;

    // Kiểm tra max, min length
    @Size(max = EmployeeValidationConstant.LENGTH_50, min = EmployeeValidationConstant.LENGTH_8, message = ErrorCodeConstants.ER007)
    private String employeeLoginPassword;

    @NotEmpty(message = ErrorCodeConstants.ER002) // Kiểm tra bắt buộc chọn
    // Kiểm tra có là số nguyên dương không
    @Pattern(regexp = EmployeeValidationConstant.POSITIVE_INTEGER, message = ErrorCodeConstants.ER018)
    @ExistsDepartmentId(message = ErrorCodeConstants.ER004)
    private String departmentId;

    @Valid
    private List<EmployeeCertificationRequestDTO> certifications;
}
