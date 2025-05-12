/**
 * Copyright(C) 2025  Luvina Software Company
 * BaseResponse.java, 5/2/2025 hoaivd
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
@JsonInclude(JsonInclude.Include.NON_NULL)  // Không bao gồm các trường null trong JSON
public class BaseResponse<T> {

    /**
     * Mã trạng thái của phản hồi (ví dụ: 200, 400, 404, v.v...)
     */
    private int code;

    /**
     * Tổng số bản ghi trả về. Được bao gồm trong phản hồi khi có nhiều bản ghi (dành cho các API phân trang).
     * Chỉ được đưa vào nếu giá trị không phải là mặc định (0).
     */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // Không bao gồm giá trị mặc định (0) trong JSON
    private int totalRecords;

    /**
     * Dữ liệu thực tế mà API trả về. Dữ liệu này có thể là bất kỳ kiểu dữ liệu nào, tùy thuộc vào đối tượng generic T.
     */
    private T data;

    /**
     * Thông điệp lỗi nếu có (dành cho các trường hợp thất bại).
     * Được sử dụng để gửi thông tin chi tiết về lỗi xảy ra trong quá trình xử lý.
     */
    private ErrorMessage message;

    /**
     * Constructor khởi tạo với mã trạng thái và dữ liệu trả về.
     *
     * @param code Mã trạng thái của phản hồi.
     * @param data Dữ liệu trả về.
     */
    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    /**
     * Constructor khởi tạo với tổng số bản ghi, mã trạng thái và dữ liệu trả về.
     *
     * @param count Tổng số bản ghi trả về.
     * @param code Mã trạng thái của phản hồi.
     * @param data Dữ liệu trả về.
     */
    public BaseResponse(int count, int code, T data) {
        this.code = code;
        this.data = data;
        this.totalRecords = count;
    }

    /**
     * Constructor khởi tạo với mã trạng thái và thông điệp lỗi.
     *
     * @param code Mã trạng thái của phản hồi.
     * @param message Thông điệp lỗi.
     */
    public BaseResponse(int code, ErrorMessage message) {
        this.code = code;
        this.message = message;
    }
}


