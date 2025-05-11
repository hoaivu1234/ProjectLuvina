package com.luvina.la.common;

/**
 * Copyright(C) 2025  Luvina Software Company
 * PaginationConstants.java, 5/11/2025 hoaivd
 */
public final class PaginationConstants {
    public static final int DEFAULT_OFFSET_VALUE = 0;
    public static final int DEFAULT_LIMIT_VALUE = 5;

    public static final String OFFSET_LABEL = "オフセット";
    public static final String LIMIT_LABEL = "リミット";

    // Ngăn chặn khởi tạo class
    private PaginationConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
