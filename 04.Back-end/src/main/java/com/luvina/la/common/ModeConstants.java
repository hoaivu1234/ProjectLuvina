/**
 * Copyright(C) 2025  Luvina Software Company
 * ModeConstants.java, 5/22/2025 hoaivd
 */

package com.luvina.la.common;

/**
 * ModeConstants là class các chế độ khi thao tác với nhân viên trong hệ thống.
 * Bao gồm các chế độ như: Add và Update.
 *
 * @author hoaivd
 */
public final class ModeConstants {
    public static final String MODE_ADD = "add";

    public static final String MODE_UPDATE = "update";

    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private ModeConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
