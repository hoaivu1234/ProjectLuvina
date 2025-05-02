package com.luvina.la.payload;

import lombok.Data;

import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * ErrorMessage.java, 5/2/2025 hoaivd
 */
@Data
public class ErrorMessage {
    private String code;
    private List<String> params;

    public ErrorMessage() {}

    public ErrorMessage(String code, List<String> params) {
        this.code = code;
        this.params = params;
    }
}

