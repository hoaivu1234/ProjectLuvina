package com.luvina.la.repository;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface EmployeeRepositoryCustom {
    Page<EmployeeDTO> getListEmployees(
            EmployeeRole role,
            String name,
            Long departmentId,
            List<Sort.Order> orders,
            int offset,
            int limit
    );
}
