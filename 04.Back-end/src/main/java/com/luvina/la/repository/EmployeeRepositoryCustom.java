/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRepositoryCustom.java, 5/8/2025 hoaivd
 */

package com.luvina.la.repository;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Interface mở rộng cho {@link EmployeeRepository} để định nghĩa các phương thức truy vấn tùy chỉnh
 * không được hỗ trợ trực tiếp bởi JpaRepository.
 *
 * Cung cấp phương thức để truy vấn danh sách nhân viên có phân trang, lọc và sắp xếp linh hoạt
 * theo vai trò, tên, phòng ban và thứ tự sắp xếp tùy chọn.
 *
 * Interface này cần được hiện thực trong một lớp implementation tương ứng (ví dụ: {@code EmployeeRepositoryImpl}).
 *
 * @author hoaivd
 */
public interface EmployeeRepositoryCustom {
    /**
     * Lấy danh sách nhân viên với phân trang, lọc theo vai trò, tên, phòng ban và sắp xếp theo thứ tự truyền vào.
     *
     * @param role Vai trò của nhân viên cần lọc.
     * @param name Tên nhân viên cần lọc (có thể null).
     * @param departmentId ID phòng ban cần lọc (có thể null).
     * @param orders Danh sách thứ tự sắp xếp, sử dụng {@link Sort.Order}.
     * @param offset Vị trí bắt đầu (sử dụng cho phân trang).
     * @param limit Số lượng bản ghi tối đa cần lấy.
     * @return Đối tượng {@link Page} chứa danh sách {@link EmployeeDTO} và thông tin phân trang.
     */
    Page<EmployeeDTO> getListEmployees(
            EmployeeRole role,
            String name,
            Long departmentId,
            List<Sort.Order> orders,
            int offset,
            int limit
    );
}
