/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeRepository.java, 5/8/2025 hoaivd
 */

package com.luvina.la.repository;

import com.luvina.la.entity.Employee;
import com.luvina.la.common.EmployeeRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * Repository tương tác với cơ sở dữ liệu để truy vấn và thao tác dữ liệu liên quan đến nhân viên.
 *
 * Giao tiếp với bảng dữ liệu {@link Employee}, kế thừa từ {@link JpaRepository} để sử dụng các thao tác CRUD mặc định
 * và mở rộng thêm các phương thức tùy chỉnh từ {@link EmployeeRepositoryCustom}.
 *
 * Cung cấp các phương thức kiểm tra tồn tại, truy vấn chi tiết theo login ID, ID nhân viên, email,
 * và thống kê số lượng nhân viên theo vai trò và bộ lọc.
 *
 * @author hoaivd
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {
    /**
     * Tìm nhân viên theo mã đăng nhập.
     *
     * @param employeeLoginId Mã đăng nhập của nhân viên.
     * @return {@link Optional} chứa {@link Employee} nếu tìm thấy, ngược lại là {@link Optional#empty()}.
     */
    Optional<Employee> findByEmployeeLoginId(String employeeLoginId);

    /**
     * Tìm nhân viên theo ID.
     *
     * @param employeeId ID của nhân viên.
     * @return {@link Optional} chứa {@link Employee} nếu tìm thấy, ngược lại là {@link Optional#empty()}.
     */
    Optional<Employee> findByEmployeeId(Long employeeId);

    /**
     * Kiểm tra sự tồn tại của nhân viên theo mã đăng nhập.
     *
     * @param employeeLoginId Mã đăng nhập cần kiểm tra.
     * @return {@code true} nếu tồn tại nhân viên với mã đăng nhập, ngược lại {@code false}.
     */
    boolean existsByEmployeeLoginId(String employeeLoginId);

    /**
     * Kiểm tra sự tồn tại của nhân viên theo địa chỉ email.
     *
     * @param employeeEmail Địa chỉ email cần kiểm tra.
     * @return {@code true} nếu tồn tại nhân viên với email, ngược lại {@code false}.
     */
    boolean existsByEmployeeEmail(String employeeEmail);

    /**
     * Thống kê số lượng nhân viên theo vai trò và bộ lọc tùy chọn theo tên và phòng ban.
     *
     * @param role Vai trò của nhân viên cần thống kê.
     * @param name Tên nhân viên (tùy chọn, có thể null).
     * @param departmentId ID phòng ban (tùy chọn, có thể null).
     * @return Số lượng nhân viên thỏa mãn điều kiện lọc.
     */
    @Query("""
            SELECT COUNT(e) 
            FROM Employee e
            INNER JOIN e.department d
            WHERE e.employeeRole = :role
              AND (:name IS NULL OR e.employeeName LIKE %:name%)
              AND (:departmentId IS NULL OR d.departmentId = :departmentId)
            """)
    int getCountEmployee(
            @Param("role") EmployeeRole role,
            @Param("name") @Nullable String name,
            @Param("departmentId") @Nullable Long departmentId);

}
