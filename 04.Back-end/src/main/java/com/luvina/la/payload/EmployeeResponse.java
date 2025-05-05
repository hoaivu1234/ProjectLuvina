package com.luvina.la.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeResponse.java, 5/5/2025 hoaivd
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeResponse<T> {
    private int code;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalRecords;

    private T employees;
    private ErrorMessage message;

    public EmployeeResponse(int code, T data) {
        this.code = code;
        this.employees = data;
    }

    public EmployeeResponse(int count, int code, T data) {
        this.code = code;
        this.employees = data;
        this.totalRecords = count;
    }

    public EmployeeResponse(int code, ErrorMessage message) {
        this.code = code;
        this.message = message;
    }
}
