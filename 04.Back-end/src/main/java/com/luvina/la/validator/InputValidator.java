package com.luvina.la.validator;

import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.common.SortConstants;
import com.luvina.la.common.ValidationConstants;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.payload.ErrorMessage;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Copyright(C) 2025  Luvina Software Company
 * InputValidator.java, 5/5/2025 hoaivd
 */
@Component
public class InputValidator {
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(ValidationConstants.SPECIAL_CHAR_REGEX);

    public Long validateDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.isEmpty()) {
            return null;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(departmentId).find()) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER005,
                    ValidationConstants.FIELD_DEPARTMENT_ID
            );
        }

        try {
            return Long.parseLong(departmentId.trim());
        } catch (NumberFormatException e) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER005,
                    ValidationConstants.FIELD_DEPARTMENT_ID
            );
        }
    }

    public String validateEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(employeeName.trim()).find()) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER005,
                    ValidationConstants.FIELD_EMPLOYEE_NAME
            );
        }

        return employeeName.trim();
    }

    public String validateSortOrder(String sortOrder) {
        if (sortOrder == null || sortOrder.trim().isEmpty()) {
            return SortConstants.ASC; // Giá trị mặc định khi không có hoặc rỗng
        } else if (!SortConstants.ASC.equals(sortOrder) && !SortConstants.DESC.equals(sortOrder)) {
            throw buildBusinessException(HttpStatusConstants.BAD_REQUEST, ErrorCodeConstants.ER021, "");
        }

        return sortOrder;
    }

    public int validatePositiveNumber(String value, String type) {
        try {
            int number = Integer.parseInt(value);
            if (number <= 0) {
                throw buildBusinessException(HttpStatusConstants.BAD_REQUEST, ErrorCodeConstants.ER018, type);
            }
            return number;
        } catch (NumberFormatException ex) {
            throw buildBusinessException(HttpStatusConstants.BAD_REQUEST, ErrorCodeConstants.ER018, type);
        }
    }

    private BusinessException buildBusinessException(int code, String errCode, String field) {
        return new BusinessException(code, new ErrorMessage(errCode, List.of(field)));
    }

}


