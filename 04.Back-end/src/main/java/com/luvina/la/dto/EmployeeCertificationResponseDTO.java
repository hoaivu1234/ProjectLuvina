package com.luvina.la.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeCertificationResponseDTO.java, 5/19/2025 hoaivd
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
