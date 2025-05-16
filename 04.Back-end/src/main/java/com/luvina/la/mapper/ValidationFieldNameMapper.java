/**
 * Copyright(C) 2025  Luvina Software Company
 * ValidationFieldNameMapper.java, 5/16/2025 hoaivd
 */

package com.luvina.la.mapper;

import java.util.Map;


public class ValidationFieldNameMapper {
    public static final Map<String, String> FIELD_DISPLAY_NAME_MAP = Map.of(
            "employeeName", "氏名",
            "employeeLoginId", "アカウント名",
            "employeeEmail", "メールアドレス",
            "employeeTelephone", "電話番号"
    );

    public static String getDisplayName(String fieldName) {
        return FIELD_DISPLAY_NAME_MAP.get(fieldName);
    }
}

