package com.luvina.la.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentResponse.java, 5/5/2025 hoaivd
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentResponse<T> {
    private int code;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalRecords;

    private T departments;
    private ErrorMessage message;

    public DepartmentResponse(int code, T data) {
        this.code = code;
        this.departments = data;
    }

    public DepartmentResponse(int count, int code, T data) {
        this.code = code;
        this.departments = data;
        this.totalRecords = count;
    }

    public DepartmentResponse(int code, ErrorMessage message) {
        this.code = code;
        this.message = message;
    }
}
