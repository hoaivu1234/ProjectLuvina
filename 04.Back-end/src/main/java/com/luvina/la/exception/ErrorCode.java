package com.luvina.la.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Copyright(C) 2025  Luvina Software Company
 * ErrorCode.java, 5/4/2025 hoaivd
 */
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    EMPLOYEE_NOT_EXISTED(404, "Employee not existed", HttpStatus.NOT_FOUND);
    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
