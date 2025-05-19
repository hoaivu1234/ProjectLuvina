/**
 * Copyright(C) 2025  Luvina Software Company
 * DateRangeValidator.java, 5/17/2025 hoaivd
 */

package com.luvina.la.validator;


import com.luvina.la.common.EmployeeValidationConstant;
import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.dto.EmployeeCertificationRequestDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validator dùng để kiểm tra tính hợp lệ của khoảng ngày trong đối tượng {@link EmployeeCertificationRequestDTO}.
 *
 * Cụ thể, kiểm tra rằng ngày kết thúc (certificationEndDate) phải sau ngày bắt đầu (certificationStartDate).
 * Nếu không hợp lệ, trả về mã lỗi {@link ErrorCodeConstants#ER012} cho trường certificationEndDate.
 *
 * Validator này hoạt động kết hợp với annotation tùy chỉnh {@link ValidDateRange}.
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, EmployeeCertificationRequestDTO> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(EmployeeValidationConstant.DATE_FORMAT_FOR_STRICT);

    /**
     * Kiểm tra tính hợp lệ của khoảng ngày.
     *
     * @param dto Đối tượng chứa thông tin chứng chỉ cần kiểm tra.
     * @param context Ngữ cảnh ràng buộc dùng để cấu hình lỗi nếu không hợp lệ.
     * @return true nếu hợp lệ hoặc dữ liệu trống (sẽ do validator khác xử lý),
     *         false nếu ngày kết thúc không sau ngày bắt đầu.
     */
    @Override
    public boolean isValid(EmployeeCertificationRequestDTO dto, ConstraintValidatorContext context) {
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            return true; // Để NotEmpty và Pattern xử lý
        }

        try {
            LocalDate start = LocalDate.parse(dto.getStartDate(), FORMATTER);
            LocalDate end = LocalDate.parse(dto.getEndDate(), FORMATTER);

            if (!end.isAfter(start)) {
                context.disableDefaultConstraintViolation();
                context
                        .buildConstraintViolationWithTemplate(ErrorCodeConstants.ER012)
                        .addPropertyNode("certificationEndDate") // Chỉ định lỗi nằm ở trường nào
                        .addConstraintViolation();
                return false;
            }
        } catch (DateTimeParseException ex) {
            return true; // Để validator khác (Pattern, ValidDate) xử lý
        }

        return true;
    }
}

