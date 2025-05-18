/**
 * Copyright(C) 2025  Luvina Software Company
 * UniqueEmployeeEmailValidator.java, 5/18/2025 hoaivd
 */

package com.luvina.la.validator;

import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator kiểm tra tính duy nhất của địa chỉ email trong hệ thống nhân viên.
 *
 * Áp dụng cho các trường được đánh dấu với annotation {@link UniqueEmployeeEmail}.
 * Validator này sử dụng {@link EmployeeService} để kiểm tra email đã tồn tại hay chưa.
 */
public class UniqueEmployeeEmailValidator implements ConstraintValidator<UniqueEmployeeEmail, String> {
    // Service để truy cập các phương thức kiểm tra dữ liệu nhân viên
    @Autowired
    private EmployeeService employeeService;

    /**
     * Phương thức kiểm tra xem email có hợp lệ (tức là chưa tồn tại trong hệ thống) hay không.
     *
     * @param email   Chuỗi email cần xác thực.
     * @param context Bối cảnh xác thực, cho phép cấu hình lỗi nếu cần.
     * @return {@code true} nếu email là null/rỗng (để validator khác như @NotEmpty xử lý),
     *         hoặc email chưa tồn tại trong hệ thống;
     *         {@code false} nếu email đã tồn tại.
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true; // Để @NotEmpty xử lý
        }
        return !employeeService.existsByEmployeeEmail(email);
    }
}

