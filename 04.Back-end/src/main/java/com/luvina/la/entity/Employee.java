/**
 * Copyright(C) 2025  Luvina Software Company
 * Employee.java, 4/29/2025 hoaivd
 */

package com.luvina.la.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.luvina.la.common.EmployeeRole;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity đại diện cho bảng employees trong cơ sở dữ liệu.
 * Lưu trữ thông tin nhân viên, bao gồm thông tin cá nhân, tài khoản đăng nhập,
 * mối liên hệ với phòng ban và các chứng chỉ của nhân viên.
 *
 * @author hoaivd
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee implements Serializable {

    private static final long serialVersionUID = 5771173953267484096L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true)
    private Long employeeId; // ID nhân viên

    @Column(name = "employee_name")
    private String employeeName; // Tên nhân viên

    @Column(name = "employee_email")
    private String employeeEmail; // Email của nhân viên

    @Column(name = "employee_name_kana")
    private String employeeNameKana; // Tên kana của nhân viên (dành cho tiếng Nhật)

    @Column(name = "employee_birth_date")
    private Date employeeBirthDate; // Ngày sinh của nhân viên

    @Column(name = "employee_telephone")
    private String employeeTelephone; // Số điện thoại nhân viên

    @Column(name = "employee_login_id")
    private String employeeLoginId; // Tên đăng nhập của nhân viên

    @Column(name = "employee_role")
    private EmployeeRole employeeRole; // Vai trò của nhân viên trong hệ thống

    @Column(name = "sort_priority")
    private String sortPriority; // Thứ tự ưu tiên khi sắp xếp

    @Column(name = "employee_login_password")
    private String employeeLoginPassword; // Mật khẩu đăng nhập của nhân viên

    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department; // Phòng ban mà nhân viên thuộc về

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeCertification> employeeCertifications = new ArrayList<>(); // Danh sách chứng chỉ của nhân viên
}

