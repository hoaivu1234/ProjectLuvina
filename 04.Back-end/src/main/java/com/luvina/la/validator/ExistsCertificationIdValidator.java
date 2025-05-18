/**
 * Copyright(C) 2025  Luvina Software Company
 * ExistsCertificationIdValidator.java, 5/18/2025 hoaivd
 */

package com.luvina.la.validator;


import com.luvina.la.service.CertificationService;
import com.luvina.la.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator kiểm tra xem ID của chứng chỉ (certificationId) có tồn tại trong hệ thống hay không.
 *
 * Áp dụng cho các trường có annotation {@link ExistsCertificationId}.
 * Validator này sử dụng dịch vụ {@link CertificationService} để xác thực tính tồn tại của ID.
 */
public class ExistsCertificationIdValidator implements ConstraintValidator<ExistsCertificationId, String> {
    // Service để kiểm tra ID chứng chỉ có tồn tại trong database hay không
    @Autowired
    private CertificationService certificationService;

    /**
     * Hàm kiểm tra tính hợp lệ của giá trị đầu vào.
     *
     * @param value   Giá trị chuỗi cần kiểm tra (dự kiến là ID dưới dạng chuỗi).
     * @param context Bối cảnh xác thực để cấu hình lỗi nếu cần.
     * @return {@code true} nếu:
     *         - Giá trị null hoặc rỗng (để các annotation khác như @NotEmpty xử lý),
     *         - hoặc là số và tồn tại trong hệ thống;
     *         {@code false} nếu không thể chuyển đổi sang số hoặc không tồn tại.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // Để @NotEmpty xử lý
        }

        try {
            Long id = Long.parseLong(value);
            return certificationService.existsById(id);
        } catch (NumberFormatException e) {
            return false; // Không phải số thì cũng trả false
        }
    }
}
