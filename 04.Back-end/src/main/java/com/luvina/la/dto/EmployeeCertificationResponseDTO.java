/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeCertificationResponseDTO.java, 5/19/2025 hoaivd
 */

package com.luvina.la.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO (Data Transfer Object) đại diện cho thông tin chứng chỉ của nhân viên cần trả về.
 * Được sử dụng để truyền dữ liệu nhân viên giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */
@Getter
@Setter
public class EmployeeCertificationResponseDTO {
    private Long certificationId;
    private String certificationName;
    private Date startDate;
    private Date endDate;
    private BigDecimal score;
}
