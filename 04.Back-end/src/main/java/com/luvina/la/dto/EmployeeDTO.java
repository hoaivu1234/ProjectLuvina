/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeDTO.java, 5/2/2025 hoaivd
 */

package com.luvina.la.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO (Data Transfer Object) đại diện cho thông tin nhân viên.
 * Được sử dụng để truyền dữ liệu nhân viên giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 6868189362900231672L;

    private Long employeeId;                // Mã nhân viên
    private String employeeName;            // Tên nhân viên
    private String employeeEmail;           // Email nhân viên
    private String employeeNameKana;        // Tên Katakana (tiếng Nhật)
    private Date employeeBirthDate;         // Ngày sinh
    private String employeeTelephone;       // Số điện thoại
    private String departmentName;          // Tên phòng ban
    private String certificationName;       // Tên chứng chỉ
    private Date endDate;                   // Ngày hết hạn chứng chỉ
    private BigDecimal score;               // Điểm số chứng chỉ
    private String employeeLoginId;         // Tên đăng nhập
    private String employeeLoginPassword;   // Mật khẩu đăng nhập (không nên trả về phía client)
}
