/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRepositoryImpl.java, 5/8/2025 hoaivd
 */

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
 * Lớp này thực thi giao diện EmployeeRepositoryCustom, cung cấp các phương thức để truy vấn và lấy danh sách nhân viên
 * từ cơ sở dữ liệu.
 *
 * @author hoaivd
 */
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Phương thức này lấy danh sách nhân viên dựa trên các tham số như tên nhân viên, phòng ban, và các tham số phân trang, sắp xếp.
     *
     * @param role Vai trò của nhân viên (Ví dụ: USER, ADMIN)
     * @param name Tên của nhân viên cần tìm kiếm
     * @param departmentId ID của phòng ban mà nhân viên thuộc về
     * @param orders Danh sách các đối tượng Sort.Order để sắp xếp kết quả
     * @param offset Vị trí bắt đầu của trang trong phân trang
     * @param limit Số lượng bản ghi trong mỗi trang
     * @return Page<EmployeeDTO> Một đối tượng Page chứa danh sách nhân viên theo phân trang và các thông tin liên quan
     * @throws [Exception] Nếu có lỗi trong quá trình truy vấn hoặc phân trang
     */
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

    /**
     * Tạo xâu và trả về một xâu chứa nội dung truy vấn vào cơ sở dữ liệu
     * @param name Tên nhân viên
     * @param departmentId Id phòng ban
     * @param orders Danh sách các Sort.Order
     * @return Xâu chứa nội dung truy vấn vào cơ sở dữ liệu
     */
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


    /**
     * Thêm điều kiện vào câu truy vấn cho trường `employeeName`.
     * Nếu tên nhân viên không null và không rỗng, điều kiện `employeeName LIKE` sẽ được thêm vào câu truy vấn.
     *
     * @param sb StringBuilder để xây dựng câu truy vấn
     * @param name Tên nhân viên cần tìm kiếm
     */
    private void addNameCondition(StringBuilder sb, String name) {
        if (name != null && !name.isBlank()) {
            sb.append(" AND e.employeeName LIKE :name ");
        }
    }

    /**
     * Thêm điều kiện vào câu truy vấn cho trường `departmentId`.
     * Nếu `departmentId` không null, điều kiện `departmentId` sẽ được thêm vào câu truy vấn.
     *
     * @param sb StringBuilder để xây dựng câu truy vấn
     * @param departmentId ID phòng ban cần lọc
     */
    private void addDepartmentCondition(StringBuilder sb, Long departmentId) {
        if (departmentId != null) {
            sb.append(" AND d.departmentId = :departmentId ");
        }
    }


    /**
     * Thêm phần `ORDER BY` vào câu truy vấn nếu có yêu cầu sắp xếp.
     * Phần `ORDER BY` sẽ được xây dựng dựa trên danh sách các đối tượng `Sort.Order`.
     *
     * @param sb StringBuilder để xây dựng câu truy vấn
     * @param orders Danh sách các đối tượng `Sort.Order` để sắp xếp kết quả
     */
    private void addOrderByClause(StringBuilder sb, List<Sort.Order> orders) {
        if (!orders.isEmpty()) {
            sb.append(" ORDER BY ");
            sb.append(orders.stream()
                    .map(this::mapOrderToProperty)
                    .collect(Collectors.joining(", ")));
        }
    }

    /**
     * Áp dụng quy tắc ánh xạ trường của `Sort.Order` thành các thuộc tính trong cơ sở dữ liệu.
     *
     * @param order Đối tượng `Sort.Order` chứa thông tin về trường và hướng sắp xếp
     * @return Chuỗi mô tả trường và hướng sắp xếp tương ứng trong câu truy vấn SQL
     */
    private String mapOrderToProperty(Sort.Order order) {
        return switch (order.getProperty()) {
            case SortConstants.EMPLOYEE_NAME_FIELD -> SortConstants.EMPLOYEE_NAME_FIELD;
            case SortConstants.CERTIFICATION_NAME_FIELD -> SortConstants.CERTIFICATION_NAME_FIELD;
            case SortConstants.END_DATE_FIELD -> SortConstants.END_DATE_FIELD;
            default -> SortConstants.EMPLOYEE_NAME_FIELD;
        } + " " + order.getDirection();
    }

    /**
     * Tạo một truy vấn TypedQuery cho đối tượng `EmployeeDTO` dựa trên câu truy vấn và các tham số được cung cấp.
     *
     * @param queryString Câu truy vấn SQL đã được xây dựng để tìm kiếm nhân viên
     * @param role Vai trò của nhân viên (ví dụ: USER, ADMIN)
     * @param name Tên nhân viên cần tìm kiếm
     * @param departmentId ID của phòng ban cần lọc
     * @return Một đối tượng `TypedQuery<EmployeeDTO>` đã được cấu hình với các tham số và câu truy vấn
     */
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

    /**
     * Áp dụng phân trang cho truy vấn bằng cách thiết lập chỉ số bắt đầu và giới hạn kết quả trả về.
     *
     * @param query Truy vấn `TypedQuery` cần áp dụng phân trang
     * @param offset Vị trí bắt đầu của trang (chỉ số bắt đầu từ 0)
     * @param limit Số lượng bản ghi tối đa được trả về trong một trang
     */
    private void applyPagination(TypedQuery<?> query, int offset, int limit) {
        query.setFirstResult(offset);
        query.setMaxResults(limit);
    }

    /**
     * Đếm số lượng nhân viên phù hợp với các điều kiện lọc.
     *
     * @param role Vai trò của nhân viên (ví dụ: USER, ADMIN)
     * @param name Tên nhân viên cần tìm kiếm
     * @param departmentId ID của phòng ban cần lọc
     * @return Số lượng nhân viên thỏa mãn các điều kiện lọc
     */
    private Long countEmployees(EmployeeRole role, String name, Long departmentId) {
        String countQueryString = buildCountQueryString(name, departmentId);
        TypedQuery<Long> countQuery = createCountQuery(countQueryString, role, name, departmentId);
        return countQuery.getSingleResult();
    }

    /**
     * Xây dựng câu truy vấn SQL để đếm số lượng nhân viên theo các điều kiện lọc.
     *
     * @param name Tên nhân viên cần tìm kiếm (có thể là null)
     * @param departmentId ID của phòng ban cần lọc (có thể là null)
     * @return Câu truy vấn SQL được xây dựng dưới dạng chuỗi String
     */
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

    /**
     * Tạo một truy vấn `TypedQuery<Long>` cho câu truy vấn đếm số lượng nhân viên.
     *
     * @param queryString Câu truy vấn SQL đã được xây dựng để đếm số lượng nhân viên
     * @param role Vai trò của nhân viên (ví dụ: USER, ADMIN)
     * @param name Tên nhân viên cần tìm kiếm (có thể là null)
     * @param departmentId ID của phòng ban cần lọc (có thể là null)
     * @return Một đối tượng `TypedQuery<Long>` đã được cấu hình với các tham số và câu truy vấn
     */
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

