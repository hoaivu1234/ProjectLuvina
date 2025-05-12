/**
 * Copyright(C) 2025  Luvina Software Company
 * LoginResponse.java, 5/2/2025 hoaivd
 */

package com.luvina.la.payload;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * Lớp đại diện cho phản hồi đăng nhập của người dùng.
 * <p>
 * Sau khi người dùng đăng nhập thành công, hệ thống sẽ trả về một phản hồi chứa
 * mã token truy cập (access token) và kiểu token, hoặc các lỗi nếu đăng nhập không thành công.
 *
 * Lớp này thường được sử dụng trong các API trả về phản hồi đăng nhập.
 *
 * @author hoaivd
 */
@Data
public class LoginResponse {

    /**
     * Mã token truy cập.
     * Đây là mã xác thực người dùng đã đăng nhập thành công và có quyền truy cập vào các tài nguyên được bảo vệ.
     */
    private String accessToken;

    /**
     * Kiểu của token.
     * Thông thường sẽ là "Bearer" để xác nhận token truy cập.
     */
    private String tokenType;

    /**
     * Bản đồ chứa các lỗi nếu có trong quá trình đăng nhập.
     * Nếu đăng nhập không thành công, các lỗi này sẽ được trả về dưới dạng thông điệp chi tiết.
     */
    private Map<String, String> errors = new HashMap<>();

    /**
     * Constructor tạo đối tượng LoginResponse với accessToken.
     * Sử dụng kiểu token mặc định là "Bearer".
     *
     * @param accessToken Mã token truy cập cho người dùng
     */
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }

    /**
     * Constructor tạo đối tượng LoginResponse với các lỗi.
     * Dùng khi có lỗi xảy ra trong quá trình đăng nhập.
     *
     * @param errors Bản đồ các lỗi trong quá trình đăng nhập
     */
    public LoginResponse(Map<String, String> errors) {
        this.errors = errors;
    }
}

