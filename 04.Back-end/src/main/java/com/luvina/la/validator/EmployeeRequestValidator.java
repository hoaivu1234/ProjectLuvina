/**
 * Copyright(C) 2025  Luvina Software Company
 * DTORequestValidator.java, 5/21/2025 hoaivd
 */

package com.luvina.la.validator;

import com.luvina.la.common.*;
import com.luvina.la.dto.EmployeeCertificationRequestDTO;
import com.luvina.la.dto.EmployeeRequestDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.mapper.ValidationFieldNameMapper;
import com.luvina.la.payload.MessageResponse;
import com.luvina.la.service.CertificationService;
import com.luvina.la.service.DepartmentService;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Validator component dùng để kiểm tra hợp lệ các trường dữ liệu
 * trong {@link EmployeeRequestDTO} khi thêm mới nhân viên.
 *
 * Sử dụng các service như {@link EmployeeService}, {@link DepartmentService},
 * {@link CertificationService} để kiểm tra trùng lặp và tồn tại của các dữ liệu liên quan.
 *
 * @author hoaivd
 */
@Component
public class EmployeeRequestValidator {
    /**
     * Service xử lý logic liên quan đến nhân viên.
     * Dùng để kiểm tra trùng lặp các trường như employeeLoginId, employeeEmail,...
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * Service xử lý logic liên quan đến phòng ban.
     * Dùng để kiểm tra sự tồn tại của departmentId.
     */
    @Autowired
    private DepartmentService departmentService;


    /**
     * Service xử lý logic liên quan đến chứng chỉ.
     * Dùng để kiểm tra sự tồn tại của certificationId.
     */
    @Autowired
    private CertificationService certificationService;

    /**
     * Formatter dùng để validate định dạng ngày theo chuẩn nghiêm ngặt (STRICT).
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(EmployeeValidationConstant.DATE_FORMAT_FOR_STRICT);

    /**
     * Thực hiện validate toàn bộ thông tin khi thêm mới hoặc cập nhật một nhân viên.
     *
     * @param employeeRequestDTO đối tượng chứa thông tin nhân viên cần validate.
     * @param mode Đang validate cho chế độ update hoặc add
     * @throws BusinessException nếu có bất kỳ lỗi nào trong dữ liệu đầu vào.
     */
    public void validateEmployee(EmployeeRequestDTO employeeRequestDTO, String mode) {
        if (ModeConstants.MODE_UPDATE.equals(mode)) { // Mode update
            String employeeId = employeeRequestDTO.getEmployeeId();
            validateEmployeeId(employeeId);
            validateEmployeeLoginId(employeeRequestDTO.getEmployeeLoginId(), employeeId, mode); // Validate employeeId với mode update
            // Thêm điều kiện kiểm tra employeeEmail nếu khác employeeEmail ứng với employeeId thì check trùng email
            validateEmployeeEmail(employeeRequestDTO.getEmployeeEmail(), employeeId, mode);

        } else {
            validateEmployeeLoginId(employeeRequestDTO.getEmployeeLoginId(), null, mode);
            validateEmployeeEmail(employeeRequestDTO.getEmployeeEmail(), null, mode);
        }


        validateEmployeeName(employeeRequestDTO.getEmployeeName());
        validateEmployeeNameKana(employeeRequestDTO.getEmployeeNameKana());
        validateEmployeeBirthDate(employeeRequestDTO.getEmployeeBirthDate());
        validateEmployeeTelephone(employeeRequestDTO.getEmployeeTelephone());
        validateEmployeeLoginPassword(employeeRequestDTO.getEmployeeLoginPassword(), mode);
        validateDepartmentId(employeeRequestDTO.getDepartmentId());

        // Validate chứng chỉ
        validateCertifications(employeeRequestDTO.getCertifications());
    }

    /**
     * Thực hiện kiểm tra hợp lệ danh sách chứng chỉ của nhân viên.
     * Gồm kiểm tra: tồn tại chứng chỉ, ngày bắt đầu, ngày kết thúc, điểm số.
     *
     * @param certifications Danh sách chứng chỉ cần kiểm tra.
     * @throws BusinessException nếu có chứng chỉ không hợp lệ.
     */
    private void validateCertifications(List<EmployeeCertificationRequestDTO> certifications) {
        if (certifications == null || certifications.isEmpty()) return;

        certifications.forEach(cert -> {
            validateCertificatonId(cert.getCertificationId());
            String startDate = cert.getStartDate();
            validateStartDate(startDate);
            validateEndDate(startDate, cert.getEndDate());
            validateScore(cert.getScore());
        });
    }

    /**
     * Thực hiện kiểm tra Id nhân viên có khác rỗng và có tồn tại trong cơ sở dữ liệu hay không.
     * Nếu không thì throw ra {@link BusinessException} với mã lỗi ER023.
     *
     * @param employeeId Id của nhân viên cần kiểm tra
     */
    private void validateEmployeeId(String employeeId) {
        String fieldName = FieldKey.EMPLOYEE_ID.key();
        validateEmptyFieldTyping(fieldName, employeeId);

        Long employeeIdCheck = Long.parseLong(employeeId);
        validateMustExistField(fieldName, employeeIdCheck,
                employeeService::existsById,
                ErrorCodeConstants.ER013);
    }

    /**
     * Kiểm tra hợp lệ mã đăng nhập của nhân viên.
     * Gồm kiểm tra: không rỗng, độ dài tối đa, pattern, và trùng lặp.
     *
     * @param employeeLoginId Mã đăng nhập cần kiểm tra.
     * @throws BusinessException nếu mã đăng nhập không hợp lệ hoặc đã tồn tại.
     */
    private void validateEmployeeLoginId(String employeeLoginId, String employeeId, String mode) {
        String fieldName = FieldKey.EMPLOYEE_LOGIN_ID.key();
        validateEmptyFieldTyping(fieldName, employeeLoginId);
        validateMaxLength(fieldName, employeeLoginId, EmployeeValidationConstant.LENGTH_50);
        validatePattern(fieldName, employeeLoginId, EmployeeValidationConstant.LOGIN_ID_REGEX, ErrorCodeConstants.ER019);

        // Nếu là add thì kiểm tra trùng employeeLoginId
        if (ModeConstants.MODE_ADD.equals(mode)) {
            validateDuplicateField(fieldName, employeeLoginId,
                    employeeService::existsByEmployeeLoginId,
                    ErrorCodeConstants.ER003);
            // Nếu là mode edit và tồn tại employeeLoginId thì kiểm tra currentLoginId hiện tại tương ứng với employeeId
            // Nếu currentLoginId và employeeLoginId khác nhau thì throw BusinessException
        } else if (ModeConstants.MODE_UPDATE.equals(mode) && employeeId != null) {
            String currentLoginId = employeeService.getEmployeeLoginIdById(Long.parseLong(employeeId));

            if (!employeeLoginId.equals(currentLoginId)) {
                throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, ErrorCodeConstants.ER023,
                        ValidationFieldNameMapper.getDisplayName(fieldName));
            }
        }
    }

    /**
     * Kiểm tra hợp lệ tên nhân viên.
     * Gồm kiểm tra: không rỗng và độ dài tối đa.
     *
     * @param employeeName Tên nhân viên cần kiểm tra.
     * @throws BusinessException nếu tên không hợp lệ.
     */
    private void validateEmployeeName(String employeeName) {
        String fieldName = FieldKey.EMPLOYEE_NAME.key();
        validateEmptyFieldTyping(fieldName, employeeName);
        validateMaxLength(fieldName, employeeName, EmployeeValidationConstant.LENGTH_125);
    }

    /**
     * Kiểm tra hợp lệ tên Kana của nhân viên.
     * Gồm kiểm tra: không rỗng, độ dài tối đa và đúng định dạng Kana nửa chiều rộng.
     *
     * @param employeeNameKana Tên Kana của nhân viên cần kiểm tra.
     * @throws BusinessException nếu tên Kana không hợp lệ.
     */
    private void validateEmployeeNameKana(String employeeNameKana) {
        String fieldName = FieldKey.EMPLOYEE_NAME_KANA.key();
        validateEmptyFieldTyping(fieldName, employeeNameKana);
        validateMaxLength(fieldName, employeeNameKana, EmployeeValidationConstant.LENGTH_125);
        validatePattern(fieldName, employeeNameKana, EmployeeValidationConstant.HALF_WIDTH_KANA_REGEX, ErrorCodeConstants.ER009);
    }

    /**
     * Kiểm tra hợp lệ ngày sinh của nhân viên.
     * Gồm kiểm tra: không rỗng, định dạng đúng và ngày hợp lệ (ví dụ: không phải 31/02).
     *
     * @param employeeBirthDate Ngày sinh của nhân viên ở định dạng chuỗi.
     * @throws BusinessException nếu ngày sinh không hợp lệ.
     */
    private void validateEmployeeBirthDate(String employeeBirthDate) {
        String fieldName = FieldKey.EMPLOYEE_BIRTH_DATE.key();
        validateEmptyFieldTyping(fieldName, employeeBirthDate);
        validatePattern(fieldName, employeeBirthDate, EmployeeValidationConstant.DATE_FORMAT_REGEX, ErrorCodeConstants.ER005);
        validateDateValid(fieldName, employeeBirthDate);
    }

    /**
     * Kiểm tra hợp lệ địa chỉ email của nhân viên.
     * Gồm kiểm tra: không rỗng, độ dài tối đa, định dạng đúng và không bị trùng.
     *
     * @param employeeEmail Địa chỉ email của nhân viên cần kiểm tra.
     * @throws BusinessException nếu email không hợp lệ hoặc đã tồn tại.
     */
    private void validateEmployeeEmail(String employeeEmail, String employeeId, String mode) {
        String fieldName = FieldKey.EMPLOYEE_EMAIL.key();
        validateEmptyFieldTyping(fieldName, employeeEmail);
        validateMaxLength(fieldName, employeeEmail, EmployeeValidationConstant.LENGTH_125);
        validatePattern(fieldName, employeeEmail, EmployeeValidationConstant.EMAIL_FORMAT_REGEX, ErrorCodeConstants.ER005);

        if (ModeConstants.MODE_ADD.equals(mode)) {
            validateDuplicateField(fieldName, employeeEmail,
                    employeeService::existsByEmployeeEmail,
                    ErrorCodeConstants.ER003);
        } else if (ModeConstants.MODE_UPDATE.equals(mode) && employeeId != null) {
            String currentEmail = employeeService.getEmployeeEmailById(Long.parseLong(employeeId));

            if (!employeeEmail.equals(currentEmail)) {
                validateDuplicateField(fieldName, employeeEmail,
                        employeeService::existsByEmployeeEmail,
                        ErrorCodeConstants.ER003);
            }
        }
    }

    /**
     * Kiểm tra hợp lệ số điện thoại của nhân viên.
     * Gồm kiểm tra: không rỗng, độ dài tối đa và định dạng số nửa chiều rộng.
     *
     * @param employeeTelephone Số điện thoại của nhân viên.
     * @throws BusinessException nếu số điện thoại không hợp lệ.
     */
    private void validateEmployeeTelephone(String employeeTelephone) {
        String fieldName = FieldKey.EMPLOYEE_TELEPHONE.key();
        validateEmptyFieldTyping(fieldName, employeeTelephone);
        validateMaxLength(fieldName, employeeTelephone, EmployeeValidationConstant.LENGTH_50);
        validatePattern(fieldName, employeeTelephone, EmployeeValidationConstant.ENGLISH_HALF_SIZE_REGEX, ErrorCodeConstants.ER008);
    }


    /**
     * Kiểm tra hợp lệ mật khẩu đăng nhập của nhân viên.
     * Gồm kiểm tra: không rỗng và độ dài nằm trong khoảng cho phép.
     *
     * @param employeeLoginPassword Mật khẩu đăng nhập của nhân viên.
     * @throws BusinessException nếu mật khẩu không hợp lệ.
     */
    private void validateEmployeeLoginPassword(String employeeLoginPassword, String mode) {
        String fieldName = FieldKey.EMPLOYEE_LOGIN_PASSWORD.key();

        boolean hasPassword = employeeLoginPassword != null && !employeeLoginPassword.isEmpty();

        if (ModeConstants.MODE_ADD.equals(mode)) {
            validateEmptyFieldTyping(fieldName, employeeLoginPassword);
        }

        if ((ModeConstants.MODE_ADD.equals(mode)) || (ModeConstants.MODE_UPDATE.equals(mode) && hasPassword)) {
            validateLengthRange(fieldName, employeeLoginPassword, EmployeeValidationConstant.LENGTH_50, EmployeeValidationConstant.LENGTH_8);
        }
    }


    /**
     * Kiểm tra hợp lệ mã phòng ban của nhân viên.
     * Gồm kiểm tra: không rỗng, định dạng số nguyên dương và tồn tại trong hệ thống.
     *
     * @param departmentId Mã phòng ban (dưới dạng chuỗi).
     * @throws BusinessException nếu mã phòng ban không hợp lệ hoặc không tồn tại.
     */
    private void validateDepartmentId(String departmentId) {
        String fieldName = FieldKey.DEPARTMENT_ID.key();
        validateEmptyFieldSelect(fieldName, departmentId);
        validatePattern(fieldName, departmentId, EmployeeValidationConstant.POSITIVE_INTEGER, ErrorCodeConstants.ER018);

        Long departmentIdCheck = Long.parseLong(departmentId);
        validateMustExistField(fieldName, departmentIdCheck,
                departmentService::existsById,
                ErrorCodeConstants.ER004);

    }

    /**
     * Kiểm tra hợp lệ mã chứng chỉ.
     * Gồm kiểm tra: không rỗng, định dạng số nguyên dương và tồn tại trong hệ thống.
     *
     * @param certificationId Mã chứng chỉ (dưới dạng chuỗi).
     * @throws BusinessException nếu mã chứng chỉ không hợp lệ hoặc không tồn tại.
     */
    private void validateCertificatonId(String certificationId) {
        String fieldName = FieldKey.CERTIFICATION_ID.key();
        validateEmptyFieldSelect(fieldName, certificationId);
        validatePattern(fieldName, certificationId, EmployeeValidationConstant.POSITIVE_INTEGER, ErrorCodeConstants.ER018);

        Long certificationIdCheck = Long.parseLong(certificationId);
        validateMustExistField(fieldName, certificationIdCheck,
                certificationService::existsById,
                ErrorCodeConstants.ER004);

    }

    /**
     * Kiểm tra hợp lệ ngày bắt đầu của chứng chỉ.
     * Gồm kiểm tra: không rỗng, đúng định dạng và là ngày hợp lệ.
     *
     * @param startDate Ngày bắt đầu (định dạng yyyy/MM/dd).
     * @throws BusinessException nếu ngày bắt đầu không hợp lệ.
     */
    private void validateStartDate(String startDate) {
        String fieldName = FieldKey.START_DATE.key();
        validateEmptyFieldTyping(fieldName, startDate);
        validatePattern(fieldName, startDate, EmployeeValidationConstant.DATE_FORMAT_REGEX, ErrorCodeConstants.ER005);
        validateDateValid(fieldName, startDate);
    }

    /**
     * Kiểm tra hợp lệ ngày kết thúc của chứng chỉ.
     * Gồm kiểm tra: không rỗng, đúng định dạng, là ngày hợp lệ, và sau hoặc bằng ngày bắt đầu.
     *
     * @param startDate Ngày bắt đầu (để so sánh).
     * @param endDate Ngày kết thúc (định dạng yyyy/MM/dd).
     * @throws BusinessException nếu ngày kết thúc không hợp lệ hoặc trước ngày bắt đầu.
     */
    private void validateEndDate(String startDate, String endDate) {
        String fieldName = FieldKey.END_DATE.key();
        validateEmptyFieldTyping(fieldName, endDate);
        validatePattern(fieldName, endDate, EmployeeValidationConstant.DATE_FORMAT_REGEX, ErrorCodeConstants.ER005);
        validateDateValid(fieldName, endDate);
        validateDateRange(startDate, endDate);
    }

    /**
     * Kiểm tra hợp lệ điểm số của chứng chỉ.
     * Gồm kiểm tra: không rỗng và định dạng số nguyên dương.
     *
     * @param score Điểm số (dưới dạng chuỗi).
     * @throws BusinessException nếu điểm số không hợp lệ.
     */
    private void validateScore(String score) {
        String fieldName = FieldKey.SCORE.key();
        validateEmptyFieldTyping(fieldName, score);
        validatePattern(fieldName, score, EmployeeValidationConstant.POSITIVE_INTEGER, ErrorCodeConstants.ER018);
    }

    /**
     * Kiểm tra ngày kết thúc phải sau ngày bắt đầu.
     *
     * @param startDate Ngày bắt đầu (định dạng yyyy/MM/dd).
     * @param endDate Ngày kết thúc (định dạng yyyy/MM/dd).
     * @throws BusinessException nếu ngày kết thúc không sau ngày bắt đầu.
     */
    private void validateDateRange(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, FORMATTER);
        LocalDate end = LocalDate.parse(endDate, FORMATTER);

        if (!end.isAfter(start)) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, ErrorCodeConstants.ER012, null);
        }
    }

    /**
     * Kiểm tra độ dài chuỗi nằm trong khoảng cho phép.
     *
     * @param fieldName   Tên trường dùng để hiển thị lỗi.
     * @param fieldValue  Giá trị chuỗi cần kiểm tra.
     * @param maxLength   Độ dài tối đa cho phép.
     * @param minLength   Độ dài tối thiểu cho phép.
     * @throws BusinessException nếu độ dài chuỗi không nằm trong khoảng cho phép.
     */
    private void validateLengthRange(String fieldName, String fieldValue, int maxLength, int minLength) {
        int length = fieldValue.length();
        if (length < minLength || maxLength < length) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, ErrorCodeConstants.ER007,
                    ValidationFieldNameMapper.getDisplayName(fieldName), EmployeeValidationConstant.LENGTH_8, EmployeeValidationConstant.LENGTH_50);
        }
    }

    /**
     * Kiểm tra định dạng ngày hợp lệ theo formatter chuẩn.
     *
     * @param fieldName  Tên trường cần kiểm tra.
     * @param fieldValue Giá trị ngày dưới dạng chuỗi.
     * @throws BusinessException nếu ngày không hợp lệ theo định dạng yêu cầu.
     */
    private void validateDateValid(String fieldName, String fieldValue) {
        try {
            LocalDate.parse(fieldValue.trim(), FORMATTER); // nếu ngày không hợp lệ sẽ ném exception
        } catch (DateTimeParseException e) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, ErrorCodeConstants.ER011,
                    ValidationFieldNameMapper.getDisplayName(fieldName));
        }
    }

    /**
     * Kiểm tra trường nhập liệu (text field) có bị rỗng hay không.
     *
     * @param fieldName  Tên trường dùng để hiển thị lỗi.
     * @param fieldValue Giá trị trường cần kiểm tra.
     * @throws BusinessException nếu trường rỗng hoặc null.
     */
    private void validateEmptyFieldTyping(String fieldName, String fieldValue) {
        if (fieldValue == null || fieldValue.isEmpty()) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, ErrorCodeConstants.ER001,
                    ValidationFieldNameMapper.getDisplayName(fieldName));
        }
    }

    /**
     * Kiểm tra trường lựa chọn (select field) có bị rỗng hay không.
     *
     * @param fieldName  Tên trường dùng để hiển thị lỗi.
     * @param fieldValue Giá trị trường cần kiểm tra.
     * @throws BusinessException nếu không có lựa chọn nào được chọn (rỗng hoặc null).
     */
    private void validateEmptyFieldSelect(String fieldName, String fieldValue) {
        if (fieldValue == null || fieldValue.isEmpty()) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, ErrorCodeConstants.ER002,
                    ValidationFieldNameMapper.getDisplayName(fieldName));
        }
    }

    /**
     * Kiểm tra độ dài tối đa của chuỗi đầu vào.
     *
     * @param fieldName  Tên trường để hiển thị lỗi.
     * @param fieldValue Giá trị của trường cần kiểm tra.
     * @param maxLength  Độ dài tối đa cho phép.
     * @throws BusinessException nếu độ dài vượt quá giới hạn.
     */
    private void validateMaxLength(String fieldName, String fieldValue, int maxLength) {
        int length = fieldValue.length();
        if (length > maxLength) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, ErrorCodeConstants.ER006,
                    ValidationFieldNameMapper.getDisplayName(fieldName));
        }
    }

    /**
     * Kiểm tra giá trị đầu vào có khớp với biểu thức chính quy không.
     *
     * @param fieldName  Tên trường để hiển thị lỗi.
     * @param fieldValue Giá trị cần kiểm tra.
     * @param pattern    Biểu thức chính quy để so khớp.
     * @param errorCode  Mã lỗi trả về nếu không khớp.
     * @throws BusinessException nếu không khớp với biểu thức chính quy.
     */
    private void validatePattern(String fieldName, String fieldValue, String pattern, String errorCode) {
        if (!Pattern.matches(pattern, fieldValue)) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, errorCode,
                    ValidationFieldNameMapper.getDisplayName(fieldName));
        }
    }


    /**
     * Kiểm tra trường không bị trùng lặp bằng cách sử dụng hàm kiểm tra tồn tại.
     *
     * @param fieldName     Tên trường để hiển thị lỗi.
     * @param fieldValue    Giá trị cần kiểm tra trùng lặp.
     * @param existsFunction Hàm kiểm tra xem giá trị đã tồn tại chưa.
     * @param errorCode     Mã lỗi nếu giá trị đã tồn tại.
     * @param <T>           Kiểu dữ liệu của giá trị trường.
     * @throws BusinessException nếu giá trị đã tồn tại.
     */
    private <T> void validateDuplicateField(
            String fieldName,
            T fieldValue,
            Function<T, Boolean> existsFunction,
            String errorCode
    ) {
        if (existsFunction.apply(fieldValue)) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, errorCode,
                    ValidationFieldNameMapper.getDisplayName(fieldName));
        }
    }

    /**
     * Kiểm tra bắt buộc trường phải tồn tại trong hệ thống.
     *
     * @param fieldName     Tên trường để hiển thị lỗi.
     * @param fieldValue    Giá trị cần kiểm tra tồn tại.
     * @param existsFunction Hàm kiểm tra xem giá trị có tồn tại không.
     * @param errorCode     Mã lỗi nếu giá trị không tồn tại.
     * @param <T>           Kiểu dữ liệu của giá trị trường.
     * @throws BusinessException nếu giá trị không tồn tại.
     */
    private <T> void validateMustExistField(
            String fieldName,
            T fieldValue,
            Function<T, Boolean> existsFunction,
            String errorCode
    ) {
        if (!existsFunction.apply(fieldValue)) {
            throw buildBusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, errorCode,
                    ValidationFieldNameMapper.getDisplayName(fieldName));
        }
    }


    /**
     * Tạo đối tượng {@link BusinessException} với mã lỗi HTTP, mã lỗi hệ thống và trường dữ liệu liên quan.
     *
     * @param code    mã HTTP (ví dụ: 400)
     * @param errCode mã lỗi hệ thống
     * @param fields các param cần trả về
     * @return {@link BusinessException}
     */
    private BusinessException buildBusinessException(int code, String errCode, Object... fields) {
        List<String> params = Arrays.stream(fields)
                .filter(Objects::nonNull) // Loại bỏ null
                .map(Object::toString)    // Chuyển tất cả thành String
                .filter(s -> !s.trim().isEmpty()) // Loại bỏ chuỗi rỗng
                .collect(Collectors.toList());

        return new BusinessException(code, new MessageResponse(errCode, params));
    }
}
