/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeCertification.java, 4/29/2025 hoaivd
 */

package com.luvina.la.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity đại diện cho bảng employees_certifications trong cơ sở dữ liệu.
 * Lưu trữ thông tin về các chứng chỉ mà nhân viên đã đạt được, bao gồm thời gian bắt đầu,
 * kết thúc, điểm số và mối liên hệ với nhân viên và chứng chỉ.
 *
 *
 */
@Entity
@Table(name = "employees_certifications")
@Data
public class EmployeeCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_certification_id", unique = true)
    private Long employeeCertificationId; // ID chứng chỉ của nhân viên

    @Column(name = "start_date")
    private Date startDate; // Ngày bắt đầu của chứng chỉ

    @Column(name = "end_date")
    private Date endDate; // Ngày kết thúc của chứng chỉ

    @Column(name = "score")
    private BigDecimal score; // Điểm số đạt được trong chứng chỉ

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee; // Nhân viên đã đạt chứng chỉ này

    @ManyToOne()
    @JoinColumn(name = "certification_id")
    private Certification certification; // Chứng chỉ mà nhân viên đã đạt được
}


