/**
 * Copyright(C) 2025  Luvina Software Company
 * ErrorResponse.java, 5/2/2025 hoaivd
 */

package com.luvina.la.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Lớp cơ sở dùng để đóng gói phản hồi trả về cho client.
 * <p>
 * Class này giúp chuẩn hóa cấu trúc dữ liệu trả về từ các API, bao gồm mã trạng thái, số lượng bản ghi, dữ liệu và thông điệp lỗi (nếu có).
 * Dữ liệu có thể được bao bọc trong đối tượng generic T để linh hoạt trong việc sử dụng với các loại dữ liệu khác nhau.
 *
 * @author hoaivd
 */
@Data
@NoArgsConstructor  // Tạo constructor không tham số
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Không bao gồm các trường null trong JSON
public class ErrorResponse<T> {

    /**
     * Mã trạng thái của phản hồi (ví dụ: 200, 400, 404, v.v...)
     */
    private int code;

    /**
     * Thông điệp lỗi nếu có (dành cho các trường hợp thất bại).
     * Được sử dụng để gửi thông tin chi tiết về lỗi xảy ra trong quá trình xử lý.
     */
    private ResponseMessage message;
}


