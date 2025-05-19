package com.luvina.la.common;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeValidationConstant.java, 5/16/2025 hoaivd
 */
public final class EmployeeValidationConstant {
    // === Regex constants for validation ===

    /** Regex: Chỉ cho phép a-z, A-Z, 0-9 và dấu gạch dưới (_) */
    public static final String LOGIN_ID_REGEX = "^[^0-9][a-zA-Z0-9_]*$";

    /** Regex: Chỉ cho phép các số nguyên dương */
    public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";

    /** Regex: Katakana Halfwidth characters (Unicode \uFF66 - \uFF9F + \uFF70) */
    public static final String HALF_WIDTH_KANA_REGEX = "^[\\uFF66-\\uFF9F\\uFF70]+$";

    /** Regex: Chỉ cho phép ký tự tiếng Anh halfsize (ASCII từ 0x00 đến 0x7F) */
    public static final String ENGLISH_HALF_SIZE_REGEX = "^[\\x00-\\x7F]+$";

    /** Regex: Chỉ cho phép chữ số halfsize (0-9) */
    public static final String NUMBER_HALF_SIZE_REGEX = "^[0-9]+$";

    /** Regex: Kiểm tra định dạng email đơn giản */
    public static final String EMAIL_FORMAT_REGEX = "^(?=[\\x00-\\x7F]+$)[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    /** Size: Kiểm tra max, min */
    public static final int LENGTH_125 = 125;

    /** Size: Kiểm tra max, min */
    public static final int LENGTH_50 = 50;

    /** Size: Kiểm tra max, min */
    public static final int LENGTH_8 = 8;

    /** Định dạng date hợp lệ */
    public static final String DATE_FORMAT = "yyyy/MM/dd";

    /** Định dạng date hợp lệ */
    public static final String DATE_FORMAT_FOR_STRICT = "uuuu/MM/dd";

    /** Regex date hợp lệ */
    public static final String DATE_FORMAT_REGEX = "^\\d{4}/\\d{2}/\\d{2}$";

    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private EmployeeValidationConstant() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
