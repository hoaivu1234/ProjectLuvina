/**
 * Copyright(C) 2025  Luvina Software Company
 * LoginRequest.java, 5/2/2025 hoaivd
 */

package com.luvina.la.payload;

import lombok.Data;

/**
 * Lớp đại diện cho yêu cầu đăng nhập của người dùng.
 * <p>
 * Đây là dữ liệu được gửi từ phía người dùng trong quá trình xác thực đăng nhập.
 * Thông tin đăng nhập bao gồm tên người dùng và mật khẩu.
 *
 * Lớp này thường được sử dụng trong các API đăng nhập để nhận thông tin xác thực từ người dùng.
 *
 * @author hoaivd
 */
@Data
public class LoginRequest {

    /**
     * Tên người dùng (username) để đăng nhập.
     * Đây là thông tin cần thiết để nhận dạng người dùng trong hệ thống.
     */
    private String username;

    /**
     * Mật khẩu của người dùng.
     * Mật khẩu được sử dụng để xác thực người dùng và đảm bảo rằng chỉ có người dùng hợp lệ mới có thể truy cập tài khoản.
     */
    private String password;
}

