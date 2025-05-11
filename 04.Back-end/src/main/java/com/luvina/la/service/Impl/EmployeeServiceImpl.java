package com.luvina.la.service.Impl;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.common.SortConstants;
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
            String ordEmployeeName,
            String ordCertificationName,
            String ordEndDate,
            String sortPriority,
            int offset,
            int limit){

        // Tạo sort order
        List<Sort.Order> orders = buildSortOrders(
                ordEmployeeName,
                ordCertificationName,
                ordEndDate,
                sortPriority);

        Page<EmployeeDTO> page = employeeRepository.getListEmployees(
                EmployeeRole.USER,
                employeeName,
                departmentId,
                orders,
                offset,
                limit);

        return new EmployeeResponse<>(
                (int) page.getTotalElements(),
                HttpStatus.OK.value(),
                page.getContent());
    }

    private List<Sort.Order> buildSortOrders(
            String ordEmployeeName,
            String ordCertificationName,
            String ordEndDate,
            String sortPriority) {

        List<Sort.Order> orders = new ArrayList<>();

        // Đảo ngược hướng sắp xếp của certificationName
        ordCertificationName = SortConstants.ASC.equalsIgnoreCase(ordCertificationName)
                ? SortConstants.DESC
                : SortConstants.ASC;

        Sort.Direction dirEmployeeName = parseDirection(ordEmployeeName);
        Sort.Direction dirCertificationName = parseDirection(ordCertificationName);
        Sort.Direction dirEndDate = parseDirection(ordEndDate);

        if (sortPriority != null) {
            switch (sortPriority.toLowerCase()) {
                case SortConstants.SORT_PRIORITY_EMPLOYEE_NAME:
                    orders.add(new Sort.Order(dirEmployeeName, SortConstants.EMPLOYEE_NAME_FIELD));
                    orders.add(new Sort.Order(Sort.Direction.ASC, SortConstants.CERTIFICATION_NAME_FIELD));
                    orders.add(new Sort.Order(Sort.Direction.ASC, SortConstants.END_DATE_FIELD));
                    break;
                case SortConstants.SORT_PRIORITY_CERTIFICATION_NAME:
                    orders.add(new Sort.Order(dirCertificationName, SortConstants.CERTIFICATION_NAME_FIELD));
                    orders.add(new Sort.Order(Sort.Direction.ASC, SortConstants.EMPLOYEE_NAME_FIELD));
                    orders.add(new Sort.Order(Sort.Direction.ASC, SortConstants.END_DATE_FIELD));
                    break;
                case SortConstants.SORT_PRIORITY_END_DATE:
                    orders.add(new Sort.Order(dirEndDate, SortConstants.END_DATE_FIELD));
                    orders.add(new Sort.Order(Sort.Direction.ASC, SortConstants.EMPLOYEE_NAME_FIELD));
                    orders.add(new Sort.Order(Sort.Direction.ASC, SortConstants.CERTIFICATION_NAME_FIELD));
                    break;
                default:
                    addDefaultSortOrders(orders);
            }
        } else {
            addDefaultSortOrders(orders);
        }

        return orders;
    }

    private Sort.Direction parseDirection(String direction) {
        return SortConstants.ASC.equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }

    private void addDefaultSortOrders(List<Sort.Order> orders) {
        orders.add(Sort.Order.asc(SortConstants.EMPLOYEE_NAME_FIELD));
        orders.add(Sort.Order.asc(SortConstants.CERTIFICATION_NAME_FIELD));
        orders.add(Sort.Order.asc(SortConstants.END_DATE_FIELD));
    }

    @Override
    public int getCountEmployee(String employeeName, Long departmentId) {
        return employeeRepository.getCountEmployee(EmployeeRole.USER, employeeName, departmentId);
    }
}
