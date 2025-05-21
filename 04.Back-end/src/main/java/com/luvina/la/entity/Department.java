/**
 * Copyright(C) 2025  Luvina Software Company
 * Department.java, 4/29/2025 hoaivd
 */

package com.luvina.la.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity đại diện cho bảng departments trong cơ sở dữ liệu.
 * Quản lý thông tin về các phòng ban và mối quan hệ với nhân viên thuộc phòng ban đó.
 *
 * @author hoaivd
 */
@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id", unique = true)
    private Long departmentId; // ID phòng ban

    @Column(name = "department_name")
    private String departmentName; // Tên phòng ban

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>(); // Danh sách nhân viên thuộc phòng ban

    public Department(Long departmentId) {
        this.departmentId = departmentId;
    }
}

