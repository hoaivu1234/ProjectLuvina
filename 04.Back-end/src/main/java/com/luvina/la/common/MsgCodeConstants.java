/**
 * Copyright(C) 2025  Luvina Software Company
 * MsgCodeConstants.java, 5/18/2025 hoaivd
 */

package com.luvina.la.common;

/**
 * Lưu trữ các hằng số mã thông báo được sử dụng trong hệ thống
 * Ví dụ: MSG001, MSG002, ...
 *
 * @author hoaivd
 */
public final class MsgCodeConstants {
    public static final String MSG001 = "MSG001";
    public static final String MSG002 = "MSG002";
    public static final String MSG003 = "MSG003";
    public static final String MSG004 = "MSG004";
    public static final String MSG005 = "MSG005";


    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private MsgCodeConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
