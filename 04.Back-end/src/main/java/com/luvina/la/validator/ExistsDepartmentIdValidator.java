/**
 * Copyright(C) 2025  Luvina Software Company
 * ExistsDepartmentIdValidator.java, 5/18/2025 hoaivd
 */

package com.luvina.la.validator;

import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.service.DepartmentService;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator kiểm tra xem ID của phòng ban (departmentId) có tồn tại trong hệ thống hay không.
 *
 * Áp dụng cho các trường được đánh dấu với annotation {@link ExistsDepartmentId}.
 * Validator này sử dụng {@link DepartmentService} để xác thực tính tồn tại của ID.
 */
public class ExistsDepartmentIdValidator implements ConstraintValidator<ExistsDepartmentId, String> {
    // Service xử lý phòng ban để kiểm tra ID
    @Autowired
    private DepartmentService departmentService;

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
            return departmentService.existsById(id);
        } catch (NumberFormatException e) {
            return false; // Không phải số thì cũng trả false
        }
    }
}

