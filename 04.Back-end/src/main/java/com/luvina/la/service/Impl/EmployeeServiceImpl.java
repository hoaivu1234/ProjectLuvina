package com.luvina.la.service.Impl;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.mapper.EmployeeMapper;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeServiceImpl.java, 4/29/2025 hoaivd
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse<List<EmployeeDTO>> getListEmployees(
            String employeeName,
            Long departmentId,
            Sort.Direction employeeNameDirection,
            Sort.Direction certificationNameDirection,
            Sort.Direction endDateDirection,
            String sortPriority,
            int offset,
            int limit){

        // Tạo sort order
        List<Sort.Order> orders = buildSortOrders(
                employeeNameDirection,
                certificationNameDirection,
                endDateDirection,
                sortPriority);

        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by(orders));

        Page<Employee> page = employeeRepository.getListEmployees(
                EmployeeRole.USER,
                employeeName,
                departmentId,
                pageable);

        return new EmployeeResponse<>(
                (int) page.getTotalElements(),
                HttpStatus.OK.value(),
                employeeMapper.toList(page.getContent()));
    }

    private List<Sort.Order> buildSortOrders(
            Sort.Direction employeeNameDirection,
            Sort.Direction certificationNameDirection,
            Sort.Direction endDateDirection,
            String sortPriority) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sortPriority != null) {
            switch (sortPriority.toLowerCase()) {
                case "ord_employee_name":
                    orders.add(new Sort.Order(employeeNameDirection, "employeeName"));
                    orders.add(new Sort.Order(Sort.Direction.ASC, "c.certificationLevel"));
                    orders.add(new Sort.Order(Sort.Direction.ASC, "ec.endDate"));
                    break;
                case "ord_certification_name":
                    orders.add(new Sort.Order(certificationNameDirection, "c.certificationLevel"));
                    orders.add(new Sort.Order(Sort.Direction.ASC, "employeeName"));
                    orders.add(new Sort.Order(Sort.Direction.ASC, "ec.endDate"));
                    break;
                case "ord_end_date":
                    orders.add(new Sort.Order(endDateDirection, "ec.endDate"));
                    orders.add(new Sort.Order(Sort.Direction.ASC, "employeeName"));
                    orders.add(new Sort.Order(Sort.Direction.ASC, "c.certificationLevel"));
                    break;
                default:
                    addDefaultSortOrders(orders);
            }
        } else {
            addDefaultSortOrders(orders);
        }

        return orders;
    }

    private void addDefaultSortOrders(List<Sort.Order> orders) {
        orders.add(new Sort.Order(Sort.Direction.ASC, "employeeName"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "c.certificationLevel"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "ec.endDate"));
    }

    @Override
    public int getCountEmployee(String employeeName, Long departmentId) {
        return employeeRepository.getCountEmployee(EmployeeRole.USER, employeeName, departmentId);
    }
}
