/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRequestValidator.java, 5/16/2025 hoaivd
 */

package com.luvina.la.validator;


import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.common.EmployeeValidationConstant;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.payload.ErrorMessage;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class EmployeeRequestValidator {
    private static final Pattern LOGIN_ID_REGEX = Pattern.compile(EmployeeValidationConstant.LOGIN_ID_REGEX);
    public void validateEmployeeLoginId(String employeeLoginId) {
        if (employeeLoginId == null || employeeLoginId.isEmpty()) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER001,
                    EmployeeValidationConstant.EMPLOYEE_LOGIN_ID_PARAM
            );
        }

        if (!LOGIN_ID_REGEX.matcher(employeeLoginId).find()) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER019,
                    ""
            );
        }

        int length = employeeLoginId.length();
        if (length > 50) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER006,
                    EmployeeValidationConstant.EMPLOYEE_LOGIN_ID_PARAM
            );
        }
    }


    /**
     * Tạo đối tượng {@link BusinessException} với mã lỗi HTTP, mã lỗi hệ thống và trường dữ liệu liên quan.
     *
     * @param code mã HTTP (ví dụ: 400)
     * @param errCode mã lỗi hệ thống
     * @param field tên trường lỗi
     * @return {@link BusinessException}
     */
    private BusinessException buildBusinessException(int code, String errCode, String field) {
        List<String> params = (field != null && !field.trim().isEmpty())
                ? List.of(field)
                : Collections.emptyList(); // Trả về mảng rỗng

        return new BusinessException(code, new ErrorMessage(errCode, params));
    }

}
