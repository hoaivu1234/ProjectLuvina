/**
 * Copyright(C) 2025  Luvina Software Company
 * BusinessException.java, 5/5/2025 hoaivd
 */

 package com.luvina.la.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.luvina.la.payload.MessageResponse;
import lombok.Data;

/**
 * Ngoại lệ đại diện cho các lỗi nghiệp vụ (business logic) trong hệ thống.
 * Được sử dụng khi một điều kiện logic trong quá trình xử lý nghiệp vụ bị vi phạm,
 * ví dụ: dữ liệu không hợp lệ, trạng thái không cho phép thực hiện hành động,...
 *
 * Ngoại lệ này kế thừa từ {@link RuntimeException}, cho phép không cần khai báo trong throws.
 *
 * Bao gồm thông tin mã lỗi (HTTP hoặc custom code) và {@link MessageResponse} mô tả lỗi.
 *
 * @author hoaivd
 */
@Data
public class BusinessException extends RuntimeException {

    /**
     * Mã lỗi trả về cho client, có thể là HTTP status code hoặc mã định nghĩa riêng của hệ thống.
     */
    private int code;

    private Long employeeId;

    /**
     * Thông tin chi tiết về lỗi, bao gồm mã lỗi và thông điệp hiển thị.
     */
    private MessageResponse messageResponse;

    /**
     * Khởi tạo một ngoại lệ BusinessException với mã lỗi và thông tin lỗi chi tiết.
     *
     * @param code Mã lỗi trả về
     * @param messageResponse Thông tin lỗi bao gồm mã lỗi và nội dung hiển thị
     */
    public BusinessException(int code, MessageResponse messageResponse) {
        // Gọi constructor của RuntimeException với chuỗi mã lỗi từ ResponseMessage
        super(messageResponse.getCode());
        this.messageResponse = messageResponse;
        this.code = code;
    }

    /**
     * Khởi tạo một ngoại lệ BusinessException với mã lỗi và thông tin lỗi chi tiết.
     *
     * @param code Mã lỗi trả về
     * @param messageResponse Thông tin lỗi bao gồm mã lỗi, Id nhân viên và nội dung hiển thị
     */
    public BusinessException(int code, Long employeeId, MessageResponse messageResponse) {
        // Gọi constructor của RuntimeException với chuỗi mã lỗi từ ResponseMessage
        super(messageResponse.getCode());
        this.messageResponse = messageResponse;
        this.code = code;
        this.employeeId = employeeId;
    }

}
