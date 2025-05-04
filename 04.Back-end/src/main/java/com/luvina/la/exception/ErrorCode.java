package com.luvina.la.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Copyright(C) 2025  Luvina Software Company
 * ErrorCode.java, 5/4/2025 hoaivd
 */
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);
    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
