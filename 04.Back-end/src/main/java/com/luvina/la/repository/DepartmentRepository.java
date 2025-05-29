/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentRepository.java, 5/8/2025 hoaivd
 */

package com.luvina.la.repository;

import com.luvina.la.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository tương tác với cơ sở dữ liệu để truy vấn thông tin về phòng ban.
 *
 * Giao tiếp với bảng dữ liệu {@link Department} và cung cấp các phương thức truy xuất dữ liệu,
 * bao gồm các phương thức mặc định của {@link JpaRepository} và các phương thức tùy chỉnh.
 *
 * Hiện tại, repository cung cấp phương thức để lấy danh sách tất cả phòng ban theo thứ tự tăng dần của mã phòng ban.
 *
 * @author hoaivd
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Truy vấn tất cả bản ghi phòng ban từ cơ sở dữ liệu và sắp xếp theo thứ tự tăng dần của mã phòng ban.
     *
     * @return Danh sách các đối tượng {@link Department} đã được sắp xếp theo {@code departmentId} tăng dần.
     */
    List<Department> findAllByOrderByDepartmentIdAsc();
}
