package com.luvina.la.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * ErrorMessage.java, 5/2/2025 hoaivd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
    private String code;
    private List<String> params;
}

