package com.luvina.la.common;

/**
 * Copyright(C) 2025  Luvina Software Company
 * ValidationConstants.java, 5/11/2025 hoaivd
 */

public final class ValidationConstants {

    // Regex pattern cho ký tự đặc biệt
    public static final String SPECIAL_CHAR_REGEX = "[%,-./;]";

    // Tên các trường (field names) dùng trong validation message
    public static final String FIELD_DEPARTMENT_ID = "departmentId";
    public static final String FIELD_EMPLOYEE_NAME = "employeeName";

    // Ngăn chặn khởi tạo class
    private ValidationConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
