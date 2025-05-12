/**
 * Copyright(C) 2025  Luvina Software Company
 * Certification.java, 4/29/2025 hoaivd
 */

package com.luvina.la.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity đại diện cho bảng certifications trong cơ sở dữ liệu.
 * Lưu thông tin về chứng chỉ của nhân viên như tên, cấp độ và các mối quan hệ với chứng chỉ của nhân viên.
 *
 * @author hoaivd
 */
@Entity
@Table(name = "certifications")
@Data
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certification_id", unique = true)
    private Long certificationId; // ID chứng chỉ

    @Column(name = "certification_name")
    private String certificationName; // Tên chứng chỉ

    @Column(name = "certification_level")
    private int certificationLevel; // Cấp độ chứng chỉ

    @OneToMany(mappedBy = "certification", cascade = CascadeType.ALL)
    private List<EmployeeCertification> employeeCertifications = new ArrayList<>(); // Danh sách nhân viên có chứng chỉ này
}

