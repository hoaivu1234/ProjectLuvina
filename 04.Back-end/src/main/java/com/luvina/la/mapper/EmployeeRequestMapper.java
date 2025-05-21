package com.luvina.la.mapper;

import com.luvina.la.common.EmployeeValidationConstant;
import com.luvina.la.dto.EmployeeRequestDTO;
import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Mapper interface sử dụng MapStruct để chuyển đổi dữ liệu từ {@link EmployeeRequestDTO} sang entity {@link Employee}.
 *
 * Interface này định nghĩa các ánh xạ tùy chỉnh giữa DTO và Entity, bao gồm xử lý định dạng ngày,
 * bỏ qua các trường cần xử lý thủ công như password, và ánh xạ ID phòng ban thành đối tượng {@link Department}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeRequestMapper {
    /**
     * Chuyển đổi đối tượng {@link EmployeeRequestDTO} thành entity {@link Employee}.
     *
     * @param dto Đối tượng DTO chứa dữ liệu đầu vào từ client
     * @return Đối tượng entity {@link Employee} tương ứng đã ánh xạ
     *
     * <ul>
     *   <li>{@code employeeBirthDate} được chuyển từ chuỗi sang {@link java.sql.Date} thông qua hàm {@link #parseDate}</li>
     *   <li>{@code employeeLoginPassword} được bỏ qua vì sẽ được mã hóa thủ công bằng `passwordEncoder`</li>
     *   <li>{@code department} được ánh xạ từ {@code departmentId} bằng hàm {@link #toDepartment}</li>
     * </ul>
     */
    @Mapping(target = "employeeBirthDate", expression = "java(parseDate(dto.getEmployeeBirthDate()))")
    @Mapping(target = "employeeLoginPassword", ignore = true) // Bỏ qua ánh xạ trường này vì sẽ mã hóa mật khẩu thủ công.
    @Mapping(target = "department", expression = "java(toDepartment(dto.getDepartmentId()))")
    Employee toEntity(EmployeeRequestDTO dto);

    /**
     * Hàm tiện ích để chuyển chuỗi ngày (dạng "yyyy-MM-dd") sang {@link java.sql.Date}.
     *
     * @param dateStr Chuỗi ngày sinh từ DTO
     * @return Đối tượng {@link java.sql.Date} tương ứng
     */
    default Date parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EmployeeValidationConstant.DATE_FORMAT);
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        return Date.valueOf(localDate);
    }

    /**
     * Hàm tiện ích để tạo đối tượng {@link Department} chỉ từ ID dạng chuỗi.
     *
     * Hàm này dùng khi không cần fetch department từ database, chỉ cần ánh xạ foreign key: departmentId.
     *
     * @param departmentId ID của phòng ban dưới dạng chuỗi
     * @return Đối tượng {@link Department} với ID được ánh xạ
     */
    default Department toDepartment(String departmentId) {
        return new Department(Long.parseLong(departmentId));
    }
}

