/**
 * Copyright(C) 2025  Luvina Software Company
 * ErrorCodeConstants.java, 11/5/2025 hoaivd
 */

package com.luvina.la.common;

/**
 * Lưu trữ các hằng số mã lỗi được sử dụng trong hệ thống để đại diện cho các lỗi phía client (4xx)
 * và lỗi phía server (5xx).
 *
 * Mã lỗi được phân nhóm theo chuẩn HTTP:
 * - ER001 ~ ER014, ER016 ~ ER022: lỗi phía client.
 * - ER015, ER023: lỗi phía server.
 *
 * @author hoaivd
 */
public final class ErrorCodeConstants {

    // --- 4xx Client Errors ---
    public static final String ER001 = "ER001";
    public static final String ER002 = "ER002";
    public static final String ER003 = "ER003";
    public static final String ER004 = "ER004";
    public static final String ER005 = "ER005";
    public static final String ER006 = "ER006";
    public static final String ER007 = "ER007";
    public static final String ER008 = "ER008";
    public static final String ER009 = "ER009";
    public static final String ER010 = "ER010";
    public static final String ER011 = "ER011";
    public static final String ER012 = "ER012";
    public static final String ER013 = "ER013";
    public static final String ER014 = "ER014";
    public static final String ER016 = "ER016";
    public static final String ER017 = "ER017";
    public static final String ER018 = "ER018";
    public static final String ER019 = "ER019";
    public static final String ER020 = "ER020";
    public static final String ER021 = "ER021";
    public static final String ER022 = "ER022";

    // --- 5xx Server Errors ---
    public static final String ER015 = "ER015";
    public static final String ER023 = "ER023";

    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private ErrorCodeConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
