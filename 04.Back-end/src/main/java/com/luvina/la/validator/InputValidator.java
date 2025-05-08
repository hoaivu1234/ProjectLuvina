package com.luvina.la.validator;

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
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[%,-./;]");

    public Long validateDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.isEmpty()) {
            return null;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(departmentId).find()) {
            throw buildBusinessException(400, "ERR005", "departmentId");
        }

        try {
            return Long.parseLong(departmentId.trim());
        } catch (NumberFormatException e) {
            throw buildBusinessException(400, "ERR005", "departmentId");
        }
    }

    public String validateEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(employeeName.trim()).find()) {
            throw buildBusinessException(400, "ERR005", "employeeName");
        }

        return employeeName.trim();
    }

    public String validateSortOrder(String sortOrder) {
        if (sortOrder == null || sortOrder.trim().isEmpty()) {
            return "ASC"; // Giá trị mặc định khi không có hoặc rỗng
        } else if (!"ASC".equals(sortOrder) && !"DESC".equals(sortOrder)) {
            throw buildBusinessException(400, "ERR021", "");
        }

        return sortOrder;
    }

    public int validatePositiveNumber(String value, String type) {
        try {
            int number = Integer.parseInt(value);
            if (number <= 0) {
                throw buildBusinessException(400, "ERR018", type);
            }
            return number;
        } catch (NumberFormatException ex) {
            throw buildBusinessException(400, "ERR018", type);
        }
    }

    private BusinessException buildBusinessException(int code, String errCode, String field) {
        return new BusinessException(code, new ErrorMessage(errCode, List.of(field)));
    }

}


