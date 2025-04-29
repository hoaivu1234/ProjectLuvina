package com.luvina.la.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * Certification.java, 4/29/2025 hoaivd
 */
@Entity
@Table(name = "certifications")
@Data
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id", unique = true)
    private Long certificationId;

    @Column(name = "certification_name")
    private String certificationName;

    @Column(name = "certification_level")
    private int certificationLevel;

    @OneToMany(mappedBy = "certification", cascade = CascadeType.ALL)
    private List<EmployeeCertification> employeeCertifications = new ArrayList<>();

}
