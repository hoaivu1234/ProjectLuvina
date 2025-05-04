package com.luvina.la.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright(C) 2025  Luvina Software Company
 * BaseResponse.java, 5/2/2025 hoaivd
 */

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private int code;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalRecords;

    private T data;
    private ErrorMessage message;

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(int count, int code, T data) {
        this.code = code;
        this.data = data;
        this.totalRecords = count;
    }

    public BaseResponse(int code, ErrorMessage message) {
        this.code = code;
        this.message = message;
    }
}

