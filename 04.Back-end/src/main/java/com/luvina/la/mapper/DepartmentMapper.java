/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentMapper.java, 4/29/2025 hoaivd
 */

package com.luvina.la.mapper;

import com.luvina.la.dto.DepartmentDTO;
import com.luvina.la.entity.Department;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Interface để chuyển đổi giữa các đối tượng DTO và Entity cho Department.
 * Sử dụng MapStruct để tự động ánh xạ các trường giữa DTO và Entity.
 * Đây là một lớp Mapper giúp thực hiện việc chuyển đổi dữ liệu giữa các tầng (DTO <-> Entity).
 *
 * @author hoaivd
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    /**
     * Chuyển đổi đối tượng DepartmentDTO thành Department Entity.
     *
     * @param dto Đối tượng DepartmentDTO cần chuyển đổi.
     * @return Đối tượng Department Entity sau khi chuyển đổi.
     */
    Department toEntity(DepartmentDTO dto);

    /**
     * Chuyển đổi đối tượng Department Entity thành DepartmentDTO.
     *
     * @param entity Đối tượng Department Entity cần chuyển đổi.
     * @return Đối tượng DepartmentDTO sau khi chuyển đổi.
     */
    DepartmentDTO toDTO(Department entity);

    /**
     * Chuyển đổi danh sách Department Entities thành danh sách DepartmentDTOs.
     *
     * @param list Danh sách các đối tượng Department Entity.
     * @return Danh sách các đối tượng DepartmentDTO sau khi chuyển đổi.
     */
    List<DepartmentDTO> toList(List<Department> list);
}

