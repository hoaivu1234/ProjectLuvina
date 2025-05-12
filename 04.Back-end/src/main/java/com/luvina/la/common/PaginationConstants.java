/**
 * Copyright(C) 2025  Luvina Software Company
 * PaginationConstants.java, 5/11/2025 hoaivd
 */

package com.luvina.la.common;

/**
 * Lưu trữ các hằng số liên quan đến phân trang, bao gồm giá trị mặc định cho offset và limit,
 * cũng như các nhãn tương ứng cho các tham số phân trang.
 * Các hằng số này được sử dụng trong các thao tác phân trang trong hệ thống.
 *
 * @author hoaivd
 */
public final class PaginationConstants {

    /**
     * Giá trị mặc định cho offset trong phân trang.
     * Dùng để bắt đầu từ mục đầu tiên khi không có giá trị offset cụ thể.
     */
    public static final int DEFAULT_OFFSET_VALUE = 0;

    /**
     * Giá trị mặc định cho limit trong phân trang.
     * Dùng để giới hạn số lượng mục hiển thị trong một trang.
     */
    public static final int DEFAULT_LIMIT_VALUE = 5;

    /**
     * Nhãn cho offset trong giao diện người dùng.
     */
    public static final String OFFSET_LABEL = "オフセット";

    /**
     * Nhãn cho limit trong giao diện người dùng.
     */
    public static final String LIMIT_LABEL = "リミット";

    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private PaginationConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}

