/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationDTO.java, 5/12/2025 hoaivd
 */

package com.luvina.la.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO đại diện cho thông tin trình độ tiếng nhật.
 * Được sử dụng để truyền dữ liệu trình độ tiếng nhật giữa các tầng trong hệ thống.
 *
 * @author hoaivd
 */

@Getter
@Setter
public class CertificationDTO {
    private Long certificationId; // ID chứng chỉ
    private String certificationName; // Tên chứng chỉ
}
