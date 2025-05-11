package com.luvina.la.common;

/**
 * Copyright(C) 2025  Luvina Software Company
 * HttpStatusConstants.java, 5/11/2025 hoaivd
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

    // Ngăn chặn khởi tạo class
    private HttpStatusConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}
