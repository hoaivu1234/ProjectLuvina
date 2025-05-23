/**
 * Copyright(C) 2025  Luvina Software Company
 * GlobalExceptionHandler.java, 5/4/2025 hoaivd
 */

package com.luvina.la.exception;

import com.luvina.la.common.EmployeeValidationConstant;
import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.mapper.ValidationFieldNameMapper;
import com.luvina.la.payload.ErrorResponse;
import com.luvina.la.payload.MessageResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;

/**
 * Lớp xử lý ngoại lệ toàn cục cho toàn bộ ứng dụng Spring Boot.
 * Mọi exception ném ra từ controller đều sẽ được bắt ở đây nếu không được xử lý cụ thể trước đó.
 * <p>
 * Áp dụng cho tất cả các controller thông qua annotation {@link RestControllerAdvice}.
 * Trả về đối tượng {@link ErrorResponse} chứa mã lỗi và thông điệp lỗi chuẩn hoá.
 *
 * @author hoaivd
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Xử lý mọi loại exception không xác định hoặc không bắt riêng.
     * <p>
     * Ví dụ: NullPointerException, IllegalArgumentException, v.v.
     *
     * @param exception đối tượng ngoại lệ phát sinh
     * @return phản hồi lỗi chuẩn hoá với mã 500 và mã lỗi hệ thống "ER023"
     */
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ErrorResponse> handlingRuntimeException(Exception exception) {
        MessageResponse messageResponse = new MessageResponse(ErrorCodeConstants.ER023, new ArrayList<>());
        ErrorResponse response = new ErrorResponse(HttpStatusConstants.INTERNAL_SERVER_ERROR, messageResponse);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Xử lý lỗi khi không tìm thấy API (ví dụ: gọi sai URL hoặc không có mapping).
     *
     * @param ex đối tượng ngoại lệ {@link NoHandlerFoundException}
     * @return phản hồi lỗi với mã 404 và mã lỗi hệ thống "ER022"
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(Exception ex) {
        MessageResponse messageResponse = new MessageResponse(ErrorCodeConstants.ER022, new ArrayList<>());
        ErrorResponse response = new ErrorResponse(HttpStatusConstants.NOT_FOUND , messageResponse);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Xử lý các ngoại lệ nghiệp vụ (BusinessException) do logic nghiệp vụ kiểm soát và ném ra.
     *
     * @param exception đối tượng {@link BusinessException} chứa mã lỗi và thông điệp chi tiết
     * @return phản hồi lỗi tuỳ theo mã lỗi cụ thể đã định nghĩa trong BusinessException
     */
    @ExceptionHandler(value = BusinessException.class)
    ResponseEntity<ErrorResponse> handlingAppException(BusinessException exception) {
        MessageResponse messageResponse = exception.getMessageResponse();
        int code = exception.getCode();
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setCode(code);
        errorResponse.setMessage(messageResponse);
        if (exception.getEmployeeId() != null) {
            errorResponse.setEmployeeId(exception.getEmployeeId());
        }

        return ResponseEntity.status(code).body(errorResponse);
    }

    /**
     * Xử lý ngoại lệ khi tham số truyền vào không đúng kiểu dữ liệu, ví dụ như truyền chuỗi thay vì số.
     *
     * Ví dụ: Khi gọi API với URL `/employees/abc` thay vì `/employees/1`,
     * sẽ ném ra {@link MethodArgumentTypeMismatchException} do `abc` không phải là kiểu `Long` hợp lệ.
     *
     * @param ex Ngoại lệ {@link MethodArgumentTypeMismatchException} bị ném khi không thể chuyển đổi kiểu tham số.
     * @return Đối tượng {@link ResponseEntity} chứa thông tin lỗi và mã lỗi nội bộ.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        List<String> params = new ArrayList<>();
        String displayName = ValidationFieldNameMapper.getDisplayName(ex.getName());
        params.add(displayName);

        MessageResponse messageResponse = new MessageResponse(ErrorCodeConstants.ER005, params);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatusConstants.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage(messageResponse);

        return ResponseEntity.status(HttpStatusConstants.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    /**
     * Xử lý ngoại lệ khi thiếu biến trong đường dẫn (@PathVariable).
     *
     * @param ex Ngoại lệ {@link MissingPathVariableException} được ném ra khi biến đường dẫn bị thiếu.
     * @return ResponseEntity chứa {@link ErrorResponse} với mã lỗi và thông báo tương ứng.
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariable(MissingPathVariableException ex) {
        List<String> params = new ArrayList<>();
        String displayName = ValidationFieldNameMapper.getDisplayName(ex.getVariableName());
        params.add(displayName);

        MessageResponse messageResponse = new MessageResponse(ErrorCodeConstants.ER001, params);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(HttpStatusConstants.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage(messageResponse);

        return ResponseEntity.status(HttpStatusConstants.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Xử lý ngoại lệ {@link MethodArgumentNotValidException} khi validation tham số request thất bại.
     * Hàm này trích xuất thông tin lỗi từ exception và xây dựng response error theo chuẩn hệ thống.
     * Bao gồm:
     *  Lấy field bị lỗi đầu tiên
     *  Ánh xạ tên field sang tên hiển thị
     *  Tìm kiếm tham số bổ sung cho message lỗi (nếu có)
     *  Đóng gói thông tin lỗi vào response object
     *
     * @param ex Đối tượng ngoại lệ {@link MethodArgumentNotValidException} chứa thông tin validation lỗi
     * @return {@link ResponseEntity} chứa {@link ErrorResponse}
     * với: HTTP status code 400 (BAD_REQUEST) và Body chứa mã lỗi và các tham số liên quan
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> fieldParams = new ArrayList<>();
        Map<String, List<String>> FIELD_ERROR_PARAMS_MAP = createFieldErrorParamsMap();
        Set<String> NO_FIELD_PARAM_ERROR_CODES = Set.of(
                ErrorCodeConstants.ER012
        );

        // Lấy lỗi đầu tiên
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorCode = ErrorCodeConstants.ER023; // mặc định nếu không có gì
        if (fieldError != null) {
            String displayField = fieldError.getField();
            errorCode = fieldError.getDefaultMessage(); // Message của annotation (VD: "ER001", "ER019")

            String baseFieldKey = extractFieldKey(displayField); // Tổng quát cho mọi field
            // Nếu mã lỗi không nằm trong danh sách loại trừ, thì mới add param
            if (!NO_FIELD_PARAM_ERROR_CODES.contains(errorCode)) {
                String displayName = ValidationFieldNameMapper.getDisplayName(baseFieldKey);
                fieldParams.add(displayName);

                String key = baseFieldKey + ":" + errorCode;
                List<String> additionalParams = FIELD_ERROR_PARAMS_MAP.get(key);
                if (additionalParams != null) {
                    fieldParams.addAll(additionalParams);
                }
            }
        }

        MessageResponse messageResponse = new MessageResponse(errorCode, fieldParams);
        ErrorResponse response = new ErrorResponse(HttpStatusConstants.INTERNAL_SERVER_ERROR, messageResponse);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Thêm vào list params trong trường hợp ngoài tên hạng mục còn có thêm tham số trả về.
     * @return Trả về 1 map chứa key theo định dạng tên trường: Tên errorCode và giá trị là danh sách các tham số trả về
     */
    private static Map<String, List<String>> createFieldErrorParamsMap() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("employeeBirthDate:" + ErrorCodeConstants.ER005, List.of(EmployeeValidationConstant.DATE_FORMAT));
        map.put("startDate:" + ErrorCodeConstants.ER005, List.of(EmployeeValidationConstant.DATE_FORMAT));
        map.put("endDate:" + ErrorCodeConstants.ER005, List.of(EmployeeValidationConstant.DATE_FORMAT));
        map.put("employeeLoginPassword:" + ErrorCodeConstants.ER007,
                List.of(Integer.toString(EmployeeValidationConstant.LENGTH_8),
                        Integer.toString(EmployeeValidationConstant.LENGTH_50)));

        return map;
    }

    /**
     * Trích xuất tên field từ chuỗi đầu vào bằng cách tách theo dấu chấm (.).
     *
     * @param displayField Chuỗi đầu vào cần trích xuất (ví dụ: "certifications[0].certificationId")
     * @return Tên field sau khi xử lý (ví dụ: "certificationId"), hoặc chuỗi rỗng nếu đầu vào rỗng
     */
    private String extractFieldKey(String displayField) {
        if (displayField == null || displayField.isEmpty()) return "";

        // Tách theo dấu chấm để lấy phần cuối
        String[] parts = displayField.split("\\.");
        return parts[parts.length - 1]; // Lấy tên field cuối cùng (vd: "certificationId")
    }

}
