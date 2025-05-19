/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeCertificationRequestDTO.java, 5/16/2025 hoaivd
 */

package com.luvina.la.dto;

import com.luvina.la.common.EmployeeValidationConstant;
import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.validator.ExistsCertificationId;
import com.luvina.la.validator.ValidDate;
import com.luvina.la.validator.ValidDateRange;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * DTO (Data Transfer Object) đại diện cho thông tin chứng chỉ cẩn thêm cho nhân viên.
 * Được sử dụng để truyền dữ liệu nhân viên giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */
@Data
@ValidDateRange
public class EmployeeCertificationRequestDTO {
    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    // Kiểm tra có là số nguyên dương không
    @Pattern(regexp = EmployeeValidationConstant.POSITIVE_INTEGER, message = ErrorCodeConstants.ER018)
    @ExistsCertificationId(message = ErrorCodeConstants.ER004)
    private String certificationId;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    // Kiểm tra định dạng hợp lệ: yyyy/MM/dd
    @Pattern(regexp = EmployeeValidationConstant.DATE_FORMAT_REGEX, message = ErrorCodeConstants.ER005)
    @ValidDate(message = ErrorCodeConstants.ER011) // Kiểm tra ngày tháng hợp lệ. Ví dụ 2025/02/31 không hợp lệ nhưng đúng format
    private String startDate;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    // Kiểm tra định dạng hợp lệ: yyyy/MM/dd
    @Pattern(regexp = EmployeeValidationConstant.DATE_FORMAT_REGEX, message = ErrorCodeConstants.ER005)
    @ValidDate(message = ErrorCodeConstants.ER011) // Kiểm tra ngày tháng hợp lệ. Ví dụ 2025/02/31 không hợp lệ nhưng đúng format
    private String endDate;

    @NotEmpty(message = ErrorCodeConstants.ER001) // Kiểm tra bắt buộc nhập
    // Kiểm tra có là số nguyên dương không
    @Pattern(regexp = EmployeeValidationConstant.POSITIVE_INTEGER, message = ErrorCodeConstants.ER018)
    private String score;
}
