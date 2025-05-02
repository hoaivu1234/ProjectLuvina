package com.luvina.la.payload;

import lombok.Data;

/**
 * Copyright(C) 2025  Luvina Software Company
 * BaseResponse.java, 5/2/2025 hoaivd
 */

@Data
public class BaseResponse<T> {
    private int code;
    private int count;
    private T data;
    private ErrorMessage message;

    public BaseResponse() {}

    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(int count, int code, T data) {
        this.code = code;
        this.data = data;
        this.count = count;
    }

    public BaseResponse(int code, ErrorMessage message) {
        this.code = code;
        this.message = message;
    }
}

