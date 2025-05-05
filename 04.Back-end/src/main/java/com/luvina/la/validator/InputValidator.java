package com.luvina.la.validator;

import com.luvina.la.exception.BusinessException;
import com.luvina.la.payload.ErrorMessage;
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
            throw new BusinessException(400, new ErrorMessage("ERR005", List.of("departmentId")));
        }

        try {
            return Long.parseLong(departmentId.trim());
        } catch (NumberFormatException e) {
            throw new BusinessException(400, new ErrorMessage("ERR005", List.of("departmentId")));
        }
    }

    public String validateEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(employeeName.trim()).find()) {
            throw new BusinessException(400, new ErrorMessage("ERR005", List.of("employeeName")));
        }

        return employeeName.trim();
    }
}


