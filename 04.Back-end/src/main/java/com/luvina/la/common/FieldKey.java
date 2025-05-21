package com.luvina.la.common;

import java.util.Arrays;
import java.util.Optional;

public enum FieldKey {
    ID("id", "ＩＤ"),
    EMPLOYEE_ID("employeeId", "ＩＤ"),
    EMPLOYEE_NAME("employeeName", "氏名"),
    EMPLOYEE_LOGIN_ID("employeeLoginId", "アカウント名"),
    EMPLOYEE_EMAIL("employeeEmail", "メールアドレス"),
    EMPLOYEE_TELEPHONE("employeeTelephone", "電話番号"),
    DEPARTMENT_ID("departmentId", "グループ"),
    EMPLOYEE_BIRTH_DATE("employeeBirthDate", "生年月日"),
    EMPLOYEE_NAME_KANA("employeeNameKana", "カタカナ氏名"),
    EMPLOYEE_LOGIN_PASSWORD("employeeLoginPassword", "パスワード"),
    CERTIFICATION_ID("certificationId", "資格"),
    START_DATE("startDate", "資格交付日"),
    END_DATE("endDate", "失効日"),
    SCORE("score", "点数");

    private final String key;
    private final String displayName;

    FieldKey(String key, String displayName) {
        this.key = key;
        this.displayName = displayName;
    }

    public String key() {
        return key;
    }

    public String displayName() {
        return displayName;
    }

    // Tìm FieldKey theo key string
    public static Optional<FieldKey> fromKey(String key) {
        return Arrays.stream(values())
                .filter(field -> field.key.equals(key))
                .findFirst();
    }
}


