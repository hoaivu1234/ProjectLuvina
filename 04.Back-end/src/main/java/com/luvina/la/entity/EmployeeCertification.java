package com.luvina.la.entity;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeCertification.java, 4/29/2025 hoaivd
 */

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "employees_certifications")
@Data
public class EmployeeCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_certification_id", unique = true)
    private Long employeeCertificationId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "score")
    private BigDecimal score;

    @ManyToOne()
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne()
    @JoinColumn(name = "certification_id")
    private Certification certification;
}

