/**
 * Copyright(C) 2025  Luvina Software Company
 * ValidationFieldNameMapper.java, 5/16/2025 hoaivd
 */

package com.luvina.la.mapper;

import java.util.Map;

/**
 * Class tiện ích dùng để ánh xạ tên các trường dữ liệu (field name) trong mã nguồn
 * sang tên hiển thị (display name) tiếng Nhật dùng cho hiển thị thông báo lỗi, giao diện, v.v.
 *
 * Ví dụ: trường "employeeLoginId" sẽ được ánh xạ sang "アカウント名".
 */
public class ValidationFieldNameMapper {
    /**
     * Map ánh xạ tên trường trong hệ thống sang tên hiển thị tiếng Nhật.
     * Key: tên thuộc tính trong Java (field name).
     * Value: tên hiển thị tương ứng (Japanese display name).
     */
    public static final Map<String, String> FIELD_DISPLAY_NAME_MAP = Map.ofEntries(
            Map.entry("id", "ＩＤ"),
            Map.entry("employeeName", "氏名"),
            Map.entry("employeeLoginId", "アカウント名"),
            Map.entry("employeeEmail", "メールアドレス"),
            Map.entry("employeeTelephone", "電話番号"),
            Map.entry("departmentId", "グループ"),
            Map.entry("employeeBirthDate", "生年月日"),
            Map.entry("employeeNameKana", "カタカナ氏名"),
            Map.entry("employeeLoginPassword", "パスワード"),
            Map.entry("certificationId", "資格"),
            Map.entry("startDate", "資格交付日"),
            Map.entry("endDate", "失効日"),
            Map.entry("score", "点数")
    );

    /**
     * Trả về tên hiển thị tiếng Nhật tương ứng với tên trường được cung cấp.
     *
     * @param fieldName tên trường cần tra cứu (VD: "employeeEmail")
     * @return tên hiển thị tương ứng nếu có trong map, ngược lại trả về null
     */
    public static String getDisplayName(String fieldName) {
        return FIELD_DISPLAY_NAME_MAP.get(fieldName);
    }
}

