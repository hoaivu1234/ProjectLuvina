/**
 * Copyright(C) 2025  Luvina Software Company
 * InputValidator.java, 5/5/2025 hoaivd
 */

package com.luvina.la.validator;

import com.luvina.la.common.ErrorCodeConstants;
import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.common.SortConstants;
import com.luvina.la.common.InputValidationConstants;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.payload.MessageResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Component dùng để validate các tham số đầu vào của API.
 * Nếu dữ liệu không hợp lệ, sẽ ném {@link BusinessException} với mã lỗi và thông điệp tương ứng.
 * @author hoaivd
 */

@Component
public class InputValidator {
    // Biểu thức chính quy dùng để kiểm tra ký tự đặc biệt không cho phép
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile(InputValidationConstants.SPECIAL_CHAR_REGEX);


    /**
     * Validate giá trị của departmentId từ request param.
     * Chấp nhận giá trị null hoặc rỗng (tức là không filter theo phòng ban).
     * Nếu có giá trị:
     * - Không được chứa ký tự đặc biệt
     * - Phải là số nguyên hợp lệ (Long)
     *
     * @param departmentId chuỗi departmentId từ request param
     * @return giá trị Long hợp lệ hoặc null nếu không có
     */
    public Long validateDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.isEmpty()) {
            return null;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(departmentId).find()) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER005,
                    InputValidationConstants.FIELD_DEPARTMENT_ID
            );
        }

        try {
            return Long.parseLong(departmentId.trim());
        } catch (NumberFormatException e) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER005,
                    InputValidationConstants.FIELD_DEPARTMENT_ID
            );
        }
    }

    /**
     * Validate tên nhân viên.
     * Cho phép null hoặc rỗng.
     * Nếu có giá trị, không được chứa ký tự đặc biệt.
     *
     * @param employeeName tên nhân viên từ request param
     * @return tên nhân viên đã trim nếu hợp lệ, hoặc null nếu không có
     */
    public String validateEmployeeName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }

        if (SPECIAL_CHAR_PATTERN.matcher(employeeName.trim()).find()) {
            throw buildBusinessException(
                    HttpStatusConstants.BAD_REQUEST,
                    ErrorCodeConstants.ER005,
                    InputValidationConstants.FIELD_EMPLOYEE_NAME
            );
        }

        return employeeName.trim();
    }

    /**
     * Validate thứ tự sắp xếp (ASC, DESC).
     * Nếu không có giá trị, mặc định trả về ASC.
     * Nếu không hợp lệ, ném lỗi ER021.
     *
     * @param sortOrder giá trị sắp xếp (ví dụ: ASC hoặc DESC)
     * @return giá trị hợp lệ (ASC hoặc DESC)
     */
    public String validateSortOrder(String sortOrder) {
        if (sortOrder == null || sortOrder.trim().isEmpty()) {
            return SortConstants.ASC; // Giá trị mặc định khi không có hoặc rỗng
        } else if (!SortConstants.ASC.equals(sortOrder) && !SortConstants.DESC.equals(sortOrder)) {
            throw buildBusinessException(HttpStatusConstants.BAD_REQUEST, ErrorCodeConstants.ER021, "");
        }

        return sortOrder;
    }

    /**
     * Validate các tham số phân trang (offset, limit).
     * Phải là số nguyên dương (> 0), nếu không sẽ ném lỗi ER018.
     *
     * @param value chuỗi đầu vào
     * @param type tên trường (OFFSET hoặc LIMIT) để đưa vào message lỗi
     * @return số nguyên dương hợp lệ
     */
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

    /**
     * Tạo đối tượng {@link BusinessException} với mã lỗi HTTP, mã lỗi hệ thống và trường dữ liệu liên quan.
     *
     * @param code mã HTTP (ví dụ: 400)
     * @param errCode mã lỗi hệ thống
     * @param field tên trường lỗi
     * @return {@link BusinessException}
     */
    private BusinessException buildBusinessException(int code, String errCode, String field) {
        List<String> params = (field != null && !field.trim().isEmpty())
                ? List.of(field)
                : Collections.emptyList(); // Trả về mảng rỗng

        return new BusinessException(code, new MessageResponse(errCode, params));
    }
}


