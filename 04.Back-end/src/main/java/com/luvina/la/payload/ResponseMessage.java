/**
 * Copyright(C) 2025  Luvina Software Company
 * ResponseMessage.java, 5/2/2025 hoaivd
 */

package com.luvina.la.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Lớp này đại diện cho thông điệp lỗi trong ứng dụng.
 * <p>
 * Thông điệp lỗi được sử dụng để truyền tải mã lỗi và các tham số liên quan tới lỗi, giúp người dùng hoặc hệ thống có thể hiểu rõ hơn về nguyên nhân gây ra lỗi.
 * Class này thường được sử dụng trong các phản hồi lỗi của API.
 *
 * @author hoaivd
 */
@Data
@NoArgsConstructor  // Tạo constructor không tham số
@AllArgsConstructor // Tạo constructor có tất cả tham số
public class ResponseMessage {

    /**
     * Mã code message trả về
     * Ví dụ: MSG001, ER001, ...
     */
    private String code;

    /**
     * Danh sách các tham số liên quan đến lỗi.
     * Các tham số này có thể là các chi tiết bổ sung, giúp giải thích nguyên nhân hoặc thông tin về lỗi.
     * Ví dụ: đối với một lỗi xác thực, tham số có thể là thông tin về người dùng hoặc token.
     */
    private List<String> params;
}


