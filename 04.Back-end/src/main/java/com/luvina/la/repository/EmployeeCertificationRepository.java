/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeCertificationRepository.java, 5/8/2025 hoaivd
 */

package com.luvina.la.repository;

import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository tương tác với cơ sở dữ liệu để truy vấn và thao tác dữ liệu liên quan đến chứng chỉ của nhân viên.
 *
 * Giao tiếp với bảng dữ liệu {@link EmployeeCertification} và cung cấp các phương thức truy xuất, xóa dữ liệu
 * dựa trên thông tin của nhân viên.
 *
 * Kế thừa từ {@link JpaRepository} để sử dụng các thao tác CRUD mặc định, đồng thời định nghĩa thêm các phương thức tùy chỉnh.
 *
 * @author hoaivd
 */
@Repository
public interface EmployeeCertificationRepository extends JpaRepository<EmployeeCertification, Long> {
    /**
     * Truy vấn danh sách chứng chỉ của một nhân viên cụ thể.
     *
     * @param employee Đối tượng {@link Employee} đại diện cho nhân viên cần truy vấn.
     * @return Danh sách các {@link EmployeeCertification} tương ứng với nhân viên đã cho.
     */
    List<EmployeeCertification> findByEmployee(Employee employee);

    /**
     * Xóa tất cả chứng chỉ của một nhân viên dựa trên ID của nhân viên.
     *
     * @param employeeId ID của nhân viên cần xóa chứng chỉ.
     */
    void deleteByEmployeeEmployeeId(Long employeeId);
}
