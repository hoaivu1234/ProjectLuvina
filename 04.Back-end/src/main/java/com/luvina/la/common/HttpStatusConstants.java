/**
 * Copyright(C) 2025  Luvina Software Company
 * HttpStatusConstants.java, 11/5/2025 hoaivd
 */

package com.luvina.la.common;

/**
 * Lưu trữ các hằng số mã trạng thái HTTP được sử dụng trong hệ thống.
 * Mã trạng thái được phân nhóm theo chuẩn HTTP:
 * - 2xx: Các mã trạng thái thành công.
 * - 4xx: Các mã trạng thái lỗi phía client.
 * - 5xx: Các mã trạng thái lỗi phía server.
 *
 * @author hoaivd
 */
public final class HttpStatusConstants {

    // 2xx Success
    public static final int OK = 200;
    public static final int CREATED = 201;
    public static final int ACCEPTED = 202;
    public static final int NO_CONTENT = 204;

    // 4xx Client Errors
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int CONFLICT = 409;

    // 5xx Server Errors
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_IMPLEMENTED = 501;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    /**
     * Constructor private để ngăn không cho khởi tạo class chứa hằng số.
     *
     * @throws AssertionError luôn luôn ném lỗi nếu bị gọi.
     */
    private HttpStatusConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
