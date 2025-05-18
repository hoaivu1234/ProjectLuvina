/**
 * Copyright(C) 2025  Luvina Software Company
 * UniqueEmployeeLoginIdValidator.java, 5/18/2025 hoaivd
 */

package com.luvina.la.validator;

import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator kiểm tra tính duy nhất của tên đăng nhập trong hệ thống nhân viên.
 *
 * Áp dụng cho các trường được đánh dấu với annotation {@link UniqueEmployeeLoginId}.
 * Validator này sử dụng {@link EmployeeService} để kiểm tra loginId đã tồn tại hay chưa.
 */
public class UniqueEmployeeLoginIdValidator implements ConstraintValidator<UniqueEmployeeLoginId, String> {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Phương thức kiểm tra xem loginId có hợp lệ (tức là chưa tồn tại trong hệ thống) hay không.
     *
     * @param employeeLoginId   Chuỗi loginId cần xác thực.
     * @param context Bối cảnh xác thực, cho phép cấu hình lỗi nếu cần.
     * @return {@code true} nếu loginId là null/rỗng (để validator khác như @NotEmpty xử lý),
     *         hoặc loginId chưa tồn tại trong hệ thống;
     *         {@code false} nếu loginId đã tồn tại.
     */
    @Override
    public boolean isValid(String employeeLoginId, ConstraintValidatorContext context) {
        if (employeeLoginId == null || employeeLoginId.isBlank()) {
            return true; // Để @NotEmpty xử lý
        }
        return !employeeService.existsByEmployeeLoginId(employeeLoginId);
    }
}

