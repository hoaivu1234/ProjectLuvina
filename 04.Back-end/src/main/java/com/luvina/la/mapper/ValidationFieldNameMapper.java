/**
 * Copyright(C) 2025  Luvina Software Company
 * ValidationFieldNameMapper.java, 5/16/2025 hoaivd
 */

package com.luvina.la.mapper;

import com.luvina.la.common.FieldKey;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class tiện ích dùng để ánh xạ tên các trường dữ liệu (field name) trong mã nguồn
 * sang tên hiển thị (display name) tiếng Nhật dùng cho hiển thị thông báo lỗi, giao diện, v.v.
 *
 * Ví dụ: trường "employeeLoginId" sẽ được ánh xạ sang "アカウント名".
 */
public class ValidationFieldNameMapper {

    private static final Map<String, String> FIELD_DISPLAY_NAME_MAP = Arrays.stream(FieldKey.values())
            .collect(Collectors.toMap(FieldKey::key, FieldKey::displayName));

    /**
     * Trả về display name từ key.
     */
    public static String getDisplayName(String fieldKey) {
        return FIELD_DISPLAY_NAME_MAP.get(fieldKey);
    }

    // Optional: lấy trực tiếp từ FieldKey enum
    public static String getDisplayKey(FieldKey fieldKey) {
        return fieldKey.displayName();
    }
}


