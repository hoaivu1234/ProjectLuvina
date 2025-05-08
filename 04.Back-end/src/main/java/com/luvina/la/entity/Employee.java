package com.luvina.la.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.luvina.la.common.EmployeeRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter

public class Employee implements Serializable {

    private static final long serialVersionUID = 5771173953267484096L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true)
    private Long employeeId;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "employee_email")
    private String employeeEmail;

    @Column(name = "employee_name_kana")
    private String employeeNameKana;

    @Column(name = "employee_birth_date")
    private Date employeeBirthDate;

    @Column(name = "employee_telephone")
    private String employeeTelephone;

    @Column(name = "employee_login_id")
    private String employeeLoginId;

    @Column(name = "employee_role")
    private EmployeeRole employeeRole;

    @Column(name = "sort_priority")
    private String sortPriority;

    @Column(name = "employee_login_password")
    private String employeeLoginPassword;

    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeCertification> employeeCertifications = new ArrayList<>();
}
