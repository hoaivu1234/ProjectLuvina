package com.luvina.la.validator;

/**
 * Copyright(C) 2025  Luvina Software Company
 * ExistsEmployeeIdValidator.java, 5/20/2025 hoaivd
 */

import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.service.DepartmentService;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator kiểm tra xem ID của nhân viên (employeeId) có tồn tại trong hệ thống hay không.
 *
 * Áp dụng cho các trường được đánh dấu với annotation {@link ExistsEmployeeId}.
 * Validator này sử dụng {@link EmployeeService} để xác thực tính tồn tại của ID.
 */
public class ExistsEmployeeIdValidator implements ConstraintValidator<ExistsEmployeeId, String> {
    // Service xử lý nhân viên để kiểm tra ID
    @Autowired
    private EmployeeService employeeService;

    /**
     * Phương thức kiểm tra giá trị đầu vào có hợp lệ không.
     *
     * @param value   Giá trị chuỗi cần kiểm tra (dự kiến là ID dưới dạng chuỗi).
     * @param context Bối cảnh xác thực để cấu hình lỗi nếu cần.
     * @return {@code true} nếu:
     *         - Giá trị null hoặc rỗng (để validator khác như @NotEmpty xử lý),
     *         - hoặc là số và tồn tại trong hệ thống;
     *         {@code false} nếu không thể parse sang số hoặc không tồn tại trong DB.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Để @NotEmpty xử lý
        }

        try {
            Long id = Long.parseLong(value);
            return employeeService.existsById(id);
        } catch (NumberFormatException e) {
            return false; // Không phải số thì cũng trả false
        }
    }
}


