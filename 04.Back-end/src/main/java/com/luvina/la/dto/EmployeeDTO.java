package com.luvina.la.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class EmployeeDTO implements Serializable {

    private static final long serialVersionUID = 6868189362900231672L;

    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private String employeeNameKana;
    private Date employeeBirthDate;
    private String employeeTelephone;
    private String departmentName;
    private String certificationName;
    private Date endDate;
    private BigDecimal score;
    private String employeeLoginId;
    private String employeeLoginPassword;
}
