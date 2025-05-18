/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationResponse.java, 5/12/2025 hoaivd
 */

package com.luvina.la.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class phản hồi dùng để đóng gói dữ liệu trả về cho các API liên quan đến trình độ tiếng nhật.
 * Class này cung cấp cấu trúc chuẩn để gửi phản hồi, bao gồm mã trạng thái, số lượng bản ghi, dữ liệu phòng ban, và thông điệp lỗi (nếu có).
 * Dữ liệu trình độ tiếng nhật có thể được bao bọc trong đối tượng generic T để linh hoạt trong việc sử dụng với các kiểu dữ liệu khác nhau.
 *
 * @author hoaivd
 */
@Data
@NoArgsConstructor  // Tạo constructor không tham số
@JsonInclude(JsonInclude.Include.NON_NULL)  // Không bao gồm các trường null trong JSON
public class CertificationResponse<T> {

    /**
     * Mã trạng thái của phản hồi (ví dụ: 200, 400, 404, v.v...).
     */
    private int code;

    /**
     * Tổng số bản ghi trả về. Chỉ được đưa vào nếu giá trị không phải là mặc định (0).
     * Được sử dụng khi có phân trang hoặc khi cần báo cáo tổng số bản ghi.
     */
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)  // Không bao gồm giá trị mặc định (0) trong JSON
    private int totalRecords;

    /**
     * Dữ liệu phòng ban trả về từ API.
     * Đây có thể là danh sách trình độ tiếng nhật hoặc một đối trình độ tiếng nhật cụ thể, tùy thuộc vào kiểu dữ liệu T.
     */
    private T certifications;

    /**
     * Thông điệp lỗi nếu có (dành cho các trường hợp thất bại).
     * Được sử dụng để gửi thông tin chi tiết về lỗi xảy ra trong quá trình xử lý.
     */
    private ResponseMessage message;

    /**
     * Constructor khởi tạo với mã trạng thái và dữ liệu trình độ tiếng nhật trả về.
     *
     * @param code Mã trạng thái của phản hồi.
     * @param data Dữ liệu trình độ tiếng nhật trả về.
     */
    public CertificationResponse(int code, T data) {
        this.code = code;
        this.certifications = data;
    }

    /**
     * Constructor khởi tạo với tổng số bản ghi, mã trạng thái và dữ liệu trình độ tiếng nhật trả về.
     *
     * @param count Tổng số bản ghi trả về.
     * @param code Mã trạng thái của phản hồi.
     * @param data Dữ liệu trình độ tiếng nhật trả về.
     */
    public CertificationResponse(int count, int code, T data) {
        this.code = code;
        this.certifications = data;
        this.totalRecords = count;
    }

    /**
     * Constructor khởi tạo với mã trạng thái và thông điệp lỗi.
     *
     * @param code Mã trạng thái của phản hồi.
     * @param message Thông điệp lỗi.
     */
    public CertificationResponse(int code, ResponseMessage message) {
        this.code = code;
        this.message = message;
    }
}
