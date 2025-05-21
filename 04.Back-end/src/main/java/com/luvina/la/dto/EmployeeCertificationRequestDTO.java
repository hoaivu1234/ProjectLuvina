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
    private String certificationId;
    private String startDate;
    private String endDate;
    private String score;
}
