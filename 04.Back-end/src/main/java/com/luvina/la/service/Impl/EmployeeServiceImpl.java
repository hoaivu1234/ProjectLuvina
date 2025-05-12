/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeServiceImpl.java, 4/29/2025 hoaivd
 */

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
 * Lớp triển khai dịch vụ quản lý nhân viên (Employee) trong hệ thống.
 * <p>
 * Lớp này cung cấp các phương thức xử lý nghiệp vụ liên quan đến nhân viên,
 * bao gồm việc lấy danh sách nhân viên từ cơ sở dữ liệu với các tiêu chí lọc và phân trang.
 *
 * @author hoaivd
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    /**
     * Repository để tương tác với bảng nhân viên trong cơ sở dữ liệu.
     */
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Mapper để chuyển đổi giữa các đối tượng Entity và DTO.
     * Dùng để chuyển đổi danh sách nhân viên từ thực thể sang DTO cho người dùng.
     */
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * Lấy danh sách nhân viên từ cơ sở dữ liệu dựa trên các tiêu chí lọc và sắp xếp.
     * Cung cấp phân trang để tối ưu hóa việc lấy dữ liệu.
     *
     * @param employeeName Tên nhân viên để lọc.
     * @param departmentId ID của phòng ban để lọc.
     * @param ordEmployeeName Cách sắp xếp tên nhân viên.
     * @param ordCertificationName Cách sắp xếp tên chứng chỉ.
     * @param ordEndDate Cách sắp xếp ngày kết thúc chứng chỉ.
     * @param sortPriority Tiêu chí sắp xếp ưu tiên.
     * @param offset Vị trí bắt đầu của trang dữ liệu.
     * @param limit Số lượng bản ghi trong mỗi trang.
     * @return Một đối tượng `EmployeeResponse` chứa danh sách nhân viên DTO cùng với tổng số bản ghi và mã trạng thái.
     */
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

    /**
     * Phương thức buildSortOrders để tạo danh sách các Sort.Order dựa trên các tham số sắp xếp
     * @param ordEmployeeName Cách sắp xếp tên nhân viên.
     * @param ordCertificationName Cách sắp xếp tên chứng chỉ.
     * @param ordEndDate Cách sắp xếp ngày kết thúc chứng chỉ.
     * @param sortPriority Tiêu chí sắp xếp ưu tiên.
     * @return Danh sách các Sort.Order dựa trên các tham số sắp xếp
     */
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

    /**
     * Phương thức parseDirection để chuyển đổi chuỗi thành Sort.Direction
     *
     * @param direction Chuổi cần chuyển
     * @return Trả về một Sort.Direction từ chuỗi đầu vào
     */
    private Sort.Direction parseDirection(String direction) {
        return SortConstants.ASC.equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
    }

    /**
     * Phương thức addDefaultSortOrders để thêm các Sort.Order mặc định
     *
     * @param orders Danh sách các Sort.Order
     */
    private void addDefaultSortOrders(List<Sort.Order> orders) {
        orders.add(Sort.Order.asc(SortConstants.EMPLOYEE_NAME_FIELD));
        orders.add(Sort.Order.asc(SortConstants.CERTIFICATION_NAME_FIELD));
        orders.add(Sort.Order.asc(SortConstants.END_DATE_FIELD));
    }

    /**
     * Đếm số lượng nhân viên theo các tiêu chí lọc
     * @param employeeName Tên nhân viên
     * @param departmentId Id phòng ban
     * @return Số lượng nhân viên thỏa mãn.
     */
    @Override
    public int getCountEmployee(String employeeName, Long departmentId) {
        return employeeRepository.getCountEmployee(EmployeeRole.USER, employeeName, departmentId);
    }
}
