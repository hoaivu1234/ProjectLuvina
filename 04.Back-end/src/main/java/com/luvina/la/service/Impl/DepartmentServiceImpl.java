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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Copyright(C) 2025  Luvina Software Company
 * DepartmentServiceImpl.java, 4/29/2025 hoaivd
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departmentList = departmentRepository.findAllByOrderByDepartmentNameAsc();
        return departmentMapper.toList(departmentList);
    }
}
