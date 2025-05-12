/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentServiceImpl.java, 4/29/2025 hoaivd
 */
package com.luvina.la.service.Impl;

import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.entity.Department;
import com.luvina.la.mapper.DepartmentMapper;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Lớp triển khai dịch vụ quản lý các phòng ban (Department) trong hệ thống.
 * Lớp này cung cấp các phương thức xử lý nghiệp vụ liên quan đến phòng ban,
 * bao gồm việc lấy danh sách các phòng ban từ cơ sở dữ liệu và chuyển đổi dữ liệu
 * từ thực thể (Entity) sang DTO để trả về cho người dùng.
 *
 * @author hoaivd
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    /**
     * Repository để tương tác với bảng phòng ban trong cơ sở dữ liệu.
     * Được tiêm tự động bởi Spring để thực hiện các thao tác CRUD.
     */
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Mapper để chuyển đổi giữa các đối tượng Entity và DTO.
     * Dùng để chuyển đổi danh sách phòng ban từ thực thể sang DTO cho người dùng.
     */
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * Lấy tất cả các phòng ban từ cơ sở dữ liệu và chuyển đổi thành danh sách các DepartmentDTO.
     *
     * @return Danh sách các DepartmentDTO, mỗi đối tượng chứa thông tin của một phòng ban
     */
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        // Lấy tất cả các phòng ban từ cơ sở dữ liệu, sắp xếp theo departmentId tăng dần.
        List<Department> departmentList = departmentRepository.findAllByOrderByDepartmentIdAsc();

        // Chuyển đổi danh sách phòng ban từ Entity sang DTO và trả về kết quả
        return departmentMapper.toList(departmentList);
    }
}

