/**
 * Copyright(C) 2025  Luvina Software Company
 * ValidDateValidator.java, 5/17/2025 hoaivd
 */

package com.luvina.la.validator;

import com.luvina.la.common.EmployeeValidationConstant;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Validator dùng để kiểm tra tính hợp lệ của chuỗi ngày tháng theo định dạng nghiêm ngặt.
 *
 * Áp dụng cho các trường kiểu String có annotation {@link ValidDate}.
 * Kiểm tra định dạng ngày phải đúng chuẩn (ví dụ: không chấp nhận 2024/02/30).
 */
public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {
    /**
     * Định dạng ngày theo pattern định nghĩa trong hằng số {@link EmployeeValidationConstant#DATE_FORMAT_FOR_STRICT},
     * sử dụng `uuuu` thay cho `yyyy` để tương thích tốt hơn với {@link ResolverStyle#STRICT}.
     *
     * {@code ResolverStyle.STRICT} giúp đảm bảo kiểm tra nghiêm ngặt: ví dụ,
     * ngày 2024/02/30 sẽ bị coi là không hợp lệ (vì tháng 2 năm 2024 chỉ có 29 ngày).
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(EmployeeValidationConstant.DATE_FORMAT_FOR_STRICT) // dùng uuuu thay cho yyyy để tương thích vởi mode STRICT
            .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Kiểm tra xem chuỗi ngày có hợp lệ theo định dạng yêu cầu hay không.
     *
     * @param value   Chuỗi ngày cần kiểm tra.
     * @param context Bối cảnh ràng buộc để cấu hình lỗi (không dùng trong validator này).
     * @return {@code true} nếu giá trị hợp lệ hoặc rỗng (để các validator như @NotEmpty xử lý),
     *         {@code false} nếu sai định dạng hoặc không phải là ngày hợp lệ.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true; // để @NotEmpty xử lý

        try {
            LocalDate.parse(value.trim(), FORMATTER); // nếu ngày không hợp lệ sẽ ném exception
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}


