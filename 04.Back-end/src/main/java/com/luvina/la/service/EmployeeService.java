/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeService.java, 5/8/2025 hoaivd
 */

package com.luvina.la.service;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.dto.EmployeeRequestDTO;
import com.luvina.la.dto.EmployeeResponseDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.payload.EmployeeResponse;

import java.util.List;
import java.util.Optional;

/**
 * Service xử lý các nghiệp vụ liên quan đến nhân viên.
 *
 * Cung cấp các phương thức để thao tác với dữ liệu nhân viên như: thêm mới, cập nhật, xóa, tìm kiếm, kiểm tra tồn tại, và truy vấn chi tiết.
 * Interface này sẽ được hiện thực bởi lớp triển khai cụ thể (ví dụ: {@code EmployeeServiceImpl}).
 *
 * Việc tách logic nghiệp vụ vào service giúp hệ thống dễ dàng kiểm thử, bảo trì và mở rộng.
 *
 * @author hoaivd
 */
public interface EmployeeService {

    /**
     * Lấy danh sách nhân viên với bộ lọc theo tên, phòng ban và sắp xếp theo nhiều tiêu chí.
     *
     * @param employeeName Tên nhân viên cần tìm (có thể null).
     * @param departmentId ID phòng ban cần lọc (có thể null).
     * @param ordEmployeeName Thứ tự sắp xếp theo tên nhân viên (ASC hoặc DESC).
     * @param ordCertificationName Thứ tự sắp xếp theo tên chứng chỉ (ASC hoặc DESC).
     * @param ordEndDate Thứ tự sắp xếp theo ngày hết hạn chứng chỉ (ASC hoặc DESC).
     * @param sortPriority Ưu tiên sắp xếp (ví dụ: tên, chứng chỉ, ngày hết hạn,...).
     * @param offset Vị trí bắt đầu lấy dữ liệu (dùng cho phân trang).
     * @param limit Số lượng bản ghi cần lấy.
     * @return Đối tượng {@link EmployeeResponse} chứa danh sách {@link EmployeeDTO}.
     */
    EmployeeResponse<List<EmployeeDTO>> getListEmployees(
            String employeeName,
            Long departmentId,
            String ordEmployeeName,
            String ordCertificationName,
            String ordEndDate,
            String sortPriority,
            int offset,
            int limit);

    /**
     * Lấy số lượng nhân viên theo bộ lọc tên và phòng ban.
     *
     * @param employeeName Tên nhân viên cần tìm (có thể null).
     * @param departmentId ID phòng ban cần lọc (có thể null).
     * @return Tổng số nhân viên thỏa mãn điều kiện.
     */
    int getCountEmployee(String employeeName, Long departmentId);

    /**
     * Thêm mới một nhân viên vào hệ thống.
     *
     * @param requestDTO Thông tin nhân viên cần thêm.
     * @return Đối tượng {@link EmployeeResponse} chứa ID của nhân viên vừa được thêm.
     */
    EmployeeResponse<Long> addEmployee(EmployeeRequestDTO requestDTO);

    /**
     * Cập nhật thông tin của một nhân viên.
     *
     * @param updateDTO Thông tin nhân viên cần cập nhật.
     * @return Đối tượng {@link EmployeeResponse} chứa ID của nhân viên sau khi cập nhật.
     */
    EmployeeResponse<Long> updateEmployee(EmployeeRequestDTO updateDTO);

    /**
     * Kiểm tra sự tồn tại của nhân viên theo mã đăng nhập.
     *
     * @param employeeLoginId Mã đăng nhập của nhân viên.
     * @return {@code true} nếu tồn tại, ngược lại {@code false}.
     */
    boolean existsByEmployeeLoginId(String employeeLoginId);

    /**
     * Kiểm tra sự tồn tại của nhân viên theo email.
     *
     * @param employeeEmail Email của nhân viên.
     * @return {@code true} nếu tồn tại, ngược lại {@code false}.
     */
    boolean existsByEmployeeEmail(String employeeEmail);

    /**
     * Lấy thông tin chi tiết của một nhân viên.
     *
     * @param employee Đối tượng {@link Employee} cần lấy thông tin.
     * @return Đối tượng {@link EmployeeResponseDTO} chứa thông tin chi tiết của nhân viên.
     */
    EmployeeResponseDTO getEmployeeById(Employee employee);

    /**
     * Xóa một nhân viên khỏi hệ thống.
     *
     * @param employee Đối tượng {@link Employee} cần xóa.
     * @return Đối tượng {@link EmployeeResponse} chứa ID của nhân viên đã bị xóa.
     */
    EmployeeResponse<Long> deleteEmployeeById(Employee employee);


    /**
     * Kiểm tra sự tồn tại của nhân viên theo ID.
     *
     * @param id ID của nhân viên.
     * @return {@code true} nếu tồn tại, ngược lại {@code false}.
     */
    boolean existsById(Long id);

    /**
     * Lấy mã đăng nhập của nhân viên theo ID.
     *
     * @param id ID của nhân viên.
     * @return Mã đăng nhập của nhân viên.
     */
    String getEmployeeLoginIdById(Long id);

    /**
     * Lấy email của nhân viên theo ID.
     *
     * @param id ID của nhân viên.
     * @return Email của nhân viên.
     */
    String getEmployeeEmailById(Long id);

    /**
     * Tìm nhân viên theo ID.
     *
     * @param employeeId ID của nhân viên.
     * @return Đối tượng {@link Optional} chứa nhân viên nếu tìm thấy.
     */
    Optional<Employee> findByEmployeeId(Long employeeId);
}
