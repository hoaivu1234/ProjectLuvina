/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentService.java, 5/8/2025 hoaivd
 */

package com.luvina.la.service;

import com.luvina.la.dto.DepartmentDTO;

import java.util.Iterator;
import java.util.List;

/**
 * Service xử lý các nghiệp vụ liên quan đến phòng ban.
 *
 * Cung cấp các phương thức để truy vấn danh sách phòng ban và kiểm tra sự tồn tại của phòng ban theo ID.
 * Interface này sẽ được hiện thực bởi một lớp service implementation (ví dụ: {@code DepartmentServiceImpl}).
 *
 * Việc tách logic nghiệp vụ ra service giúp quản lý và mở rộng hệ thống dễ dàng hơn.
 *
 * @author hoaivd
 */
public interface DepartmentService {
    /**
     * Lấy danh sách tất cả các phòng ban.
     *
     * @return Danh sách {@link DepartmentDTO} đại diện cho tất cả phòng ban trong hệ thống.
     */
    List<DepartmentDTO> getAllDepartments();

    /**
     * Kiểm tra sự tồn tại của phòng ban theo ID.
     *
     * @param id ID của phòng ban cần kiểm tra.
     * @return {@code true} nếu phòng ban tồn tại, ngược lại {@code false}.
     */
    boolean existsById(Long id);
}
