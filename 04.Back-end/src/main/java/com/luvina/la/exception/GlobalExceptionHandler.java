/**
 * Copyright(C) 2025  Luvina Software Company
 * GlobalExceptionHandler.java, 5/4/2025 hoaivd
 */

package com.luvina.la.exception;

import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.payload.BaseResponse;
import com.luvina.la.payload.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp xử lý ngoại lệ toàn cục cho toàn bộ ứng dụng Spring Boot.
 * Mọi exception ném ra từ controller đều sẽ được bắt ở đây nếu không được xử lý cụ thể trước đó.
 *
 * Áp dụng cho tất cả các controller thông qua annotation {@link RestControllerAdvice}.
 * Trả về đối tượng {@link BaseResponse} chứa mã lỗi và thông điệp lỗi chuẩn hoá.
 * @author hoaivd
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý mọi loại exception không xác định hoặc không bắt riêng.
     *
     * Ví dụ: NullPointerException, IllegalArgumentException, v.v.
     *
     * @param exception đối tượng ngoại lệ phát sinh
     * @return phản hồi lỗi chuẩn hoá với mã 500 và mã lỗi hệ thống "ER023"
     */
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<BaseResponse> handlingRuntimeException(Exception exception) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorCodeConstants.ER023, new ArrayList<>());
        BaseResponse response = new BaseResponse(HttpStatusConstants.INTERNAL_SERVER_ERROR, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Xử lý lỗi khi không tìm thấy API (ví dụ: gọi sai URL hoặc không có mapping).
     *
     * @param ex đối tượng ngoại lệ {@link NoHandlerFoundException}
     * @return phản hồi lỗi với mã 404 và mã lỗi hệ thống "ER022"
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse> handleNotFound(Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage(ErrorCodeConstants.ER022, new ArrayList<>());
        BaseResponse response = new BaseResponse(HttpStatusConstants.NOT_FOUND, errorMessage);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Xử lý các ngoại lệ nghiệp vụ (BusinessException) do logic nghiệp vụ kiểm soát và ném ra.
     *
     * @param exception đối tượng {@link BusinessException} chứa mã lỗi và thông điệp chi tiết
     * @return phản hồi lỗi tuỳ theo mã lỗi cụ thể đã định nghĩa trong BusinessException
     */
    @ExceptionHandler(value = BusinessException.class)
    ResponseEntity<BaseResponse> handlingAppException(BusinessException exception) {
        ErrorMessage errorMessage = exception.getErrorMessage();
        int code = exception.getCode();
        BaseResponse baseResponse = new BaseResponse();

        baseResponse.setCode(code);
        baseResponse.setMessage(errorMessage);

        return ResponseEntity.status(code).body(baseResponse);
    }
}
