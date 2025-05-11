package com.luvina.la.exception;

import com.luvina.la.payload.ErrorMessage;
import lombok.Data;

/**
 * Copyright(C) 2025  Luvina Software Company
 * BusinessException.java, 5/5/2025 hoaivd
 */
@Data
public class BusinessException extends RuntimeException{
    private int code;
    private ErrorMessage errorMessage;

    public BusinessException(int code, ErrorMessage errorMessage) {
        super(errorMessage.getCode());
        this.errorMessage = errorMessage;
        this.code = code;
    }
}
