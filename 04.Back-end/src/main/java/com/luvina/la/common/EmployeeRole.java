package com.luvina.la.common;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRole.java, 5/4/2025 hoaivd
 */
public enum EmployeeRole {
    USER(0),
    ADMIN(1);

    private final int value;

    EmployeeRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
