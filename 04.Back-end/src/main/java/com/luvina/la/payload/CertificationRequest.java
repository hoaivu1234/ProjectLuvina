/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationRequest.java, 5/16/2025 hoaivd
 */

package com.luvina.la.payload;

import lombok.Data;

@Data
public class CertificationRequest {
    private String certificationId;
    private String certificationStartDate;
    private String certificationEndDate;
    private String employeeCertificationScore;
}
