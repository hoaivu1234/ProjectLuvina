package com.luvina.la.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRole.java, 5/4/2025 hoaivd
 */

@AllArgsConstructor
@Getter
public enum EmployeeRole {
    USER(0),
    ADMIN(1);

    private final int value;
}
