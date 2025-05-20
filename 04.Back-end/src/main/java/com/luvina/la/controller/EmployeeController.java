/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeController.java, 5/2/2025 hoaivd
 */

package com.luvina.la.controller;

import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.common.PaginationConstants;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.dto.EmployeeRequestDTO;
import com.luvina.la.dto.EmployeeResponseDTO;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.service.EmployeeService;
import com.luvina.la.validator.InputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.query.EscapeCharacter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller xử lý các yêu cầu liên quan đến nhân viên.
 * Cung cấp các endpoint để lấy danh sách phòng ban từ dịch vụ và trả về thông tin cho client.
 *
 * @author hoaivd
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private InputValidator inputValidator;

    /**
     * Lấy danh sách nhân viên dựa trên các tham số lọc và phân trang.
     * Phương thức này sẽ nhận các tham số từ client, tiến hành xác thực và xử lý các tham số đầu vào,
     * sau đó trả về danh sách nhân viên thỏa mãn các điều kiện lọc.
     *
     * @param employeeName Tên nhân viên để lọc (có thể rỗng).
     * @param departmentId ID của phòng ban để lọc (có thể rỗng).
     * @param ordEmployeeName Thứ tự sắp xếp theo tên nhân viên (có thể rỗng).
     * @param ordCertificationName Thứ tự sắp xếp theo tên chứng chỉ (có thể rỗng).
     * @param ordEndDate Thứ tự sắp xếp theo ngày kết thúc (có thể rỗng).
     * @param sortPriority Giá trị ưu tiên sắp xếp (có thể rỗng).
     * @param offset Vị trí bắt đầu lấy dữ liệu (có thể rỗng).
     * @param limit Số lượng bản ghi trả về (có thể rỗng).
     *
     * @return ResponseEntity<EmployeeResponse<List<EmployeeDTO>>> đối tượng chứa mã trạng thái và danh sách nhân viên thỏa mãn điều kiện lọc.
     * @throws DataAccessException Nếu có lỗi xảy ra trong quá trình truy xuất dữ liệu.
     */
    @GetMapping("")
    public ResponseEntity<EmployeeResponse<List<EmployeeDTO>>> getListEmployees(
            @RequestParam(name = "employee_name", required = false, defaultValue = "") String employeeName,
            @RequestParam(name = "department_id", required = false, defaultValue = "") String departmentId,
            @RequestParam(name = "ord_employee_name", required = false, defaultValue = "") String ordEmployeeName,
            @RequestParam(name = "ord_certification_name", required = false, defaultValue = "") String ordCertificationName,
            @RequestParam(name = "ord_end_date", required = false, defaultValue = "") String ordEndDate,
            @RequestParam(name = "sort_priority", required = false, defaultValue = "") String sortPriority,
            @RequestParam(name = "offset", required = false, defaultValue = "") String offset,
            @RequestParam(name = "limit", required = false, defaultValue = "") String limit) throws DataAccessException {

        // validate input params
        // Escape ký tự đặc biệt trong employeeName
        employeeName = EscapeCharacter.DEFAULT.escape(employeeName);

        // Kiểm tra ID phòng ban có hợp lệ không
        Long departmentIdNumber = inputValidator.validateDepartmentId(departmentId);

        // Kiểm tra thứ tự sắp xếp của các tham số liên quan đến sắp xếp
        ordEmployeeName = inputValidator.validateSortOrder(ordEmployeeName);
        ordCertificationName = inputValidator.validateSortOrder(ordCertificationName);
        ordEndDate = inputValidator.validateSortOrder(ordEndDate);

        // Xử lý tham số phân trang
        int offsetNumber;
        int limitNumber;

        // Kiểm tra giá trị phân trang, nếu không có thì sử dụng giá trị mặc định
        offsetNumber = offset.isEmpty() ? PaginationConstants.DEFAULT_OFFSET_VALUE : inputValidator.validatePositiveNumber(offset, PaginationConstants.OFFSET_LABEL);
        limitNumber = limit.isEmpty() ? PaginationConstants.DEFAULT_LIMIT_VALUE : inputValidator.validatePositiveNumber(limit, PaginationConstants.LIMIT_LABEL);

        // Lấy tổng số bản ghi nhân viên thỏa mãn các điều kiện lọc
        int count = employeeService.getCountEmployee(employeeName, departmentIdNumber);

        // Khởi tạo response
        EmployeeResponse<List<EmployeeDTO>> response = new EmployeeResponse<>(count, HttpStatusConstants.OK, new ArrayList<>());

        // Nếu có nhân viên thỏa mãn, lấy danh sách nhân viên
        if (count > 0) {
            response = employeeService.getListEmployees(employeeName, departmentIdNumber, ordEmployeeName, ordCertificationName,
                    ordEndDate, sortPriority, offsetNumber, limitNumber);
        }

        // Trả về response với mã trạng thái OK và danh sách nhân viên
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * API thêm mới một nhân viên.
     * Nhận vào một đối tượng {@link EmployeeRequestDTO} chứa thông tin của nhân viên như:
     * mã đăng nhập, mật khẩu, email, mã phòng ban,... và thực hiện lưu vào cơ sở dữ liệu
     * @param employeeRequest Đối tượng chứa thông tin nhân viên cần thêm mới.
     * @return {@link ResponseEntity} chứa {@link EmployeeResponse} với mã trạng thái HTTP 200 (OK) nếu thành công.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeResponse> addEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequest) {
        EmployeeResponse<Long> response = employeeService.addEmployee(employeeRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * API lấy thông tin chi tiết của một nhân viên theo ID.
     *
     * @param id ID của nhân viên cần lấy thông tin.
     * @return Đối tượng {@link EmployeeResponseDTO} chứa thông tin chi tiết của nhân viên.
     */
    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    /**
     * Xóa một nhân viên dựa trên ID.
     *
     * @param id ID của nhân viên cần xóa.
     * @return ResponseEntity chứa đối tượng {@link EmployeeResponse} với thông tin phản hồi từ service.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<EmployeeResponse> deleteEmployeeById(@PathVariable Long id) {
        EmployeeResponse<Long> response = employeeService.deleteEmployeeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
