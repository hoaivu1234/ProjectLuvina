package com.luvina.la.repository.Impl;

import com.luvina.la.common.EmployeeRole;
import com.luvina.la.common.SortConstants;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.repository.EmployeeRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRepositoryImpl.java, 5/8/2025 hoaivd
 */
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<EmployeeDTO> getListEmployees(
            EmployeeRole role,
            String name,
            Long departmentId,
            List<Sort.Order> orders,
            int offset,
            int limit
    ) {
        // Build main query
        String queryString = buildEmployeeQueryString(name, departmentId, orders);
        TypedQuery<EmployeeDTO> query = createEmployeeQuery(queryString, role, name, departmentId);
        applyPagination(query, offset, limit);

        // Execute query
        List<EmployeeDTO> resultList = query.getResultList();

        // Get total count
        Long total = countEmployees(role, name, departmentId);

        return new PageImpl<>(resultList, PageRequest.of(offset / limit, limit), total);
    }

    private String buildEmployeeQueryString(String name, Long departmentId, List<Sort.Order> orders) {
        StringBuilder sb = new StringBuilder();
        sb.append("""       
                SELECT new com.luvina.la.dto.EmployeeDTO(
                    e.employeeId,
                    e.employeeName,
                    e.employeeEmail,
                    e.employeeNameKana,
                    e.employeeBirthDate,
                    e.employeeTelephone,
                    d.departmentName,
                    c.certificationName,
                    ec.endDate,
                    ec.score,
                    e.employeeLoginId,
                    e.employeeLoginPassword
                )
                FROM Employee e
                JOIN e.department d
                LEFT JOIN e.employeeCertifications ec
                LEFT JOIN ec.certification c
                WHERE e.employeeRole = :role
                AND (
                    ec.employeeCertificationId IS NULL
                    OR ec.employeeCertificationId = (
                        SELECT ec2.employeeCertificationId
                        FROM EmployeeCertification ec2
                        JOIN ec2.certification c2
                        WHERE ec2.employee.employeeId = e.employeeId
                        AND c2.certificationName = (
                            SELECT MIN(c3.certificationName)
                            FROM EmployeeCertification ec3
                            JOIN ec3.certification c3
                            WHERE ec3.employee.employeeId = e.employeeId
                        )
                    )
                )
                """);

        addNameCondition(sb, name);
        addDepartmentCondition(sb, departmentId);
        addOrderByClause(sb, orders);

        return sb.toString();
    }

    private void addNameCondition(StringBuilder sb, String name) {
        if (name != null && !name.isBlank()) {
            sb.append(" AND e.employeeName LIKE :name ");
        }
    }

    private void addDepartmentCondition(StringBuilder sb, Long departmentId) {
        if (departmentId != null) {
            sb.append(" AND d.departmentId = :departmentId ");
        }
    }

    private void addOrderByClause(StringBuilder sb, List<Sort.Order> orders) {
        if (!orders.isEmpty()) {
            sb.append(" ORDER BY ");
            sb.append(orders.stream()
                    .map(this::mapOrderToProperty)
                    .collect(Collectors.joining(", ")));
        }
    }

    private String mapOrderToProperty(Sort.Order order) {
        return switch (order.getProperty()) {
            case SortConstants.EMPLOYEE_NAME_FIELD -> SortConstants.EMPLOYEE_NAME_FIELD;
            case SortConstants.CERTIFICATION_NAME_FIELD -> SortConstants.CERTIFICATION_NAME_FIELD;
            case SortConstants.END_DATE_FIELD -> SortConstants.END_DATE_FIELD;
            default -> SortConstants.EMPLOYEE_NAME_FIELD;
        } + " " + order.getDirection();
    }

    private TypedQuery<EmployeeDTO> createEmployeeQuery(
            String queryString,
            EmployeeRole role,
            String name,
            Long departmentId
    ) {
        TypedQuery<EmployeeDTO> query = entityManager.createQuery(queryString, EmployeeDTO.class);
        query.setParameter("role", role);

        if (name != null && !name.isBlank()) {
            query.setParameter("name", "%" + name + "%");
        }

        if (departmentId != null) {
            query.setParameter("departmentId", departmentId);
        }

        return query;
    }

    private void applyPagination(TypedQuery<?> query, int offset, int limit) {
        query.setFirstResult(offset);
        query.setMaxResults(limit);
    }

    private Long countEmployees(EmployeeRole role, String name, Long departmentId) {
        String countQueryString = buildCountQueryString(name, departmentId);
        TypedQuery<Long> countQuery = createCountQuery(countQueryString, role, name, departmentId);
        return countQuery.getSingleResult();
    }

    private String buildCountQueryString(String name, Long departmentId) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                    SELECT COUNT(DISTINCT e)
                    FROM Employee e
                    JOIN e.department d
                    WHERE e.employeeRole = :role
                """);

        addNameCondition(sb, name);
        addDepartmentCondition(sb, departmentId);

        return sb.toString();
    }

    private TypedQuery<Long> createCountQuery(
            String queryString,
            EmployeeRole role,
            String name,
            Long departmentId
    ) {
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        query.setParameter("role", role);

        if (name != null && !name.isBlank()) {
            query.setParameter("name", "%" + name + "%");
        }

        if (departmentId != null) {
            query.setParameter("departmentId", departmentId);
        }

        return query;
    }
}

