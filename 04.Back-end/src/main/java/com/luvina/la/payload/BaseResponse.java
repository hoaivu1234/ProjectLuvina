package com.luvina.la.payload;

/**
 * Copyright(C) 2025  Luvina Software Company
 * BaseResponse.java, 5/2/2025 hoaivd
 */
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

    public BaseResponse(int code, ErrorMessage message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorMessage getMessage() {
        return message;
    }

    public void setMessage(ErrorMessage message) {
        this.message = message;
    }
}

