/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeServiceImpl.java, 4/29/2025 hoaivd
 */

package com.luvina.la.service.Impl;

import com.luvina.la.common.*;
import com.luvina.la.dto.EmployeeCertificationRequestDTO;
import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.dto.EmployeeRequestDTO;
import com.luvina.la.dto.EmployeeResponseDTO;
import com.luvina.la.entity.Certification;
import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.payload.EmployeeResponse;
import com.luvina.la.payload.MessageResponse;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.repository.DepartmentRepository;
import com.luvina.la.repository.EmployeeCertificationRepository;
import com.luvina.la.repository.EmployeeRepository;
import com.luvina.la.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * Repository để tương tác với bảng phòng ban trong cơ sở dữ liệu.
     */
    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Repository để tương tác với bảng chứng chỉ trong cơ sở dữ liệu.
     */
    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private EmployeeCertificationRepository employeeCertificationRepository;

    /**
     * Interface này dùng để encode mật khẩu trước khi lưu vào cơ sở dữ liệu.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findByEmployeeId(id);
        if (employee.isEmpty()) {
            throw new BusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR,
                    new MessageResponse(ErrorCodeConstants.ER013, new ArrayList<>()));
        }
        return null;
    }

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

    /**
     * Thêm mới một nhân viên vào hệ thống.
     *
     * @param requestDTO Đối tượng chứa thông tin nhân viên cần thêm, bao gồm: tên, email, số điện thoại, tài khoản đăng nhập, mật khẩu, phòng ban, chứng chỉ,...
     * @return Đối tượng {@link EmployeeResponse} chứa mã trạng thái, ID nhân viên vừa được thêm và thông báo thành công.
     *
     * @throws BusinessException nếu không tìm thấy phòng ban hoặc chứng chỉ tương ứng với ID được truyền vào.
     */

    @Transactional
    @Override
    public EmployeeResponse<Long> addEmployee(EmployeeRequestDTO requestDTO) {
        Employee employee = new Employee();

        // Gán các thuộc tính đơn giản
        employee.setEmployeeName(requestDTO.getEmployeeName());
        employee.setEmployeeEmail(requestDTO.getEmployeeEmail());
        employee.setEmployeeTelephone(requestDTO.getEmployeeTelephone());
        employee.setEmployeeNameKana(requestDTO.getEmployeeNameKana());
        employee.setEmployeeLoginId(requestDTO.getEmployeeLoginId());

        String encodedPassword = passwordEncoder.encode(requestDTO.getEmployeeLoginPassword());
        employee.setEmployeeLoginPassword(encodedPassword);

        employee.setEmployeeRole(EmployeeRole.USER);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EmployeeValidationConstant.DATE_FORMAT);
        LocalDate birthDate = LocalDate.parse(requestDTO.getEmployeeBirthDate(), formatter);
        employee.setEmployeeBirthDate(Date.valueOf(birthDate));

        // Tìm department có departmentId bằng departmentId truyền vào
        Long deptId = Long.parseLong(requestDTO.getDepartmentId());
        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new BusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR,
                        new MessageResponse(ErrorCodeConstants.ER015, new ArrayList<>())));
        employee.setDepartment(department);

        // Gán danh sách chứng chỉ nếu có
        if (requestDTO.getCertifications() != null && !requestDTO.getCertifications().isEmpty()) {
            List<EmployeeCertification> certList = requestDTO.getCertifications().stream().map(certDTO -> {
                // Khởi tạo chứng chỉ
                EmployeeCertification cert = new EmployeeCertification();

                LocalDate startDate = LocalDate.parse(certDTO.getStartDate(), formatter);
                cert.setStartDate(Date.valueOf(startDate));

                LocalDate endDate = LocalDate.parse(certDTO.getEndDate(), formatter);
                cert.setEndDate(Date.valueOf(endDate));

                Long certificationId = Long.parseLong(certDTO.getCertificationId());
                Certification certification = certificationRepository.findById(certificationId)
                        .orElseThrow(() -> new BusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR,
                                new MessageResponse(ErrorCodeConstants.ER015, new ArrayList<>())));
                cert.setCertification(certification);

                BigDecimal score = new BigDecimal(certDTO.getScore());
                cert.setScore(score);

                cert.setEmployee(employee); // Liên kết ngược

                return cert;
            }).collect(Collectors.toList());

            employee.setEmployeeCertifications(certList);
        }

        // Lưu vào DB
        employeeRepository.save(employee);
        return new EmployeeResponse<>(HttpStatusConstants.OK, employee.getEmployeeId(), new MessageResponse(MsgCodeConstants.MSG001, new ArrayList<>()));
    }

    /**
     * Kiểm tra xem tên đăng nhập của nhân viên đã tồn tại trong hệ thống hay chưa.
     *
     * @param employeeLoginId Tên đăng nhập cần kiểm tra.
     * @return true nếu tên đăng nhập đã tồn tại, false nếu chưa tồn tại.
     */
    @Override
    public boolean existsByEmployeeLoginId(String employeeLoginId) {
        return employeeRepository.existsByEmployeeLoginId(employeeLoginId);
    }

    /**
     * Kiểm tra xem email của nhân viên đã tồn tại trong hệ thống hay chưa.
     *
     * @param employeeEmail Địa chỉ email cần kiểm tra.
     * @return true nếu email đã tồn tại, false nếu chưa tồn tại.
     */
    @Override
    public boolean existsByEmployeeEmail(String employeeEmail) {
        return employeeRepository.existsByEmployeeEmail(employeeEmail);
    }

}
