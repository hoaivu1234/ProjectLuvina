/**
 * Copyright(C) 2025  Luvina Software Company
 * ValidationConstants.java, 5/11/2025 hoaivd
 */

package com.luvina.la.common;

/**
 * Lưu trữ các hằng số liên quan đến việc kiểm tra (validation) trong hệ thống.
 * Bao gồm các biểu thức chính quy (regex) và tên các trường (field names)
 * được sử dụng trong các thông báo lỗi khi thực hiện validation.
 *
 * @author hoaivd
 */
public final class InputValidationConstants {

    /**
     * Biểu thức chính quy cho các ký tự đặc biệt như "%", ",", "-", ".", "/", ";"
     * Dùng để kiểm tra tính hợp lệ của chuỗi đầu vào.
     */
    public static final String SPECIAL_CHAR_REGEX = "[%,-./;]";

    /**
     * Tên trường (field) cho mã phòng ban trong validation.
     */
    public static final String FIELD_DEPARTMENT_ID = "departmentId";

    /**
     * Tên trường (field) cho tên nhân viên trong validation.
     */
    public static final String FIELD_EMPLOYEE_NAME = "employeeName";

    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private InputValidationConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
