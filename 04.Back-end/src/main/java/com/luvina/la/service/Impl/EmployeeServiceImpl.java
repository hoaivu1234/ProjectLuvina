/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeServiceImpl.java, 4/29/2025 hoaivd
 */

package com.luvina.la.service.Impl;

import com.luvina.la.common.*;
import com.luvina.la.dto.*;
import com.luvina.la.entity.Certification;
import com.luvina.la.entity.Department;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import com.luvina.la.exception.BusinessException;
import com.luvina.la.mapper.EmployeeRequestMapper;
import com.luvina.la.mapper.EmployeeResponseMapper;
import com.luvina.la.mapper.ValidationFieldNameMapper;
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
    @Autowired
    private EmployeeRequestMapper employeeRequestMapper;

    @Autowired
    private EmployeeResponseMapper employeeResponseMapper;
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

    /**
     * Repository để tương tác với bảng chứng chỉ nhân viên trong cơ sở dữ liệu.
     */
    @Autowired
    private EmployeeCertificationRepository employeeCertificationRepository;

    /**
     * Interface này dùng để encode mật khẩu trước khi lưu vào cơ sở dữ liệu.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Trả về thông tin chi tiết của một nhân viên theo ID, bao gồm cả thông tin chứng chỉ (certifications).
     * <p>
     * Nếu nhân viên không tồn tại, phương thức sẽ ném ra {@link BusinessException} với mã lỗi ER013.
     *
     * @param id ID của nhân viên cần lấy thông tin.
     * @return Đối tượng {@link EmployeeResponseDTO} chứa thông tin nhân viên và danh sách chứng chỉ (nếu có).
     * @throws BusinessException nếu không tìm thấy nhân viên tương ứng với ID truyền vào.
     */
    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = getEmployee(id, ErrorCodeConstants.ER013, false);

        List<EmployeeCertification> certifications = employeeCertificationRepository.findByEmployee(employee);

        EmployeeResponseDTO response = employeeResponseMapper.toDto(employee);
        response.setCode(HttpStatusConstants.OK);

        if (certifications != null && !certifications.isEmpty()) {
            response.setCertifications(employeeResponseMapper.toCertificationDtos(certifications));
        }

        return response;
    }

    /**
     * Xóa nhân viên theo ID.
     *
     * Đánh dấu {@code @Transactional} để đảm bảo tính toàn vẹn dữ liệu.
     * Nếu nhân viên tồn tại, nếu không, ném ra ngoại lệ BusinessException.
     * Nếu nhân viên tồn tại thì kiểm tra nhân viên có phải là ADMIN không. Nếu đúng thì throw BusinessException
     * Nếu không phải thì tiến hành xóa khỏi cơ sở dữ liệu.
     *
     * @param id ID của nhân viên cần xóa.
     * @return {@link EmployeeResponse} chứa ID đã xóa, mã trạng thái HTTP và thông báo thành công.
     * @throws BusinessException nếu không tìm thấy nhân viên với ID tương ứng.
     */
    @Transactional
    @Override
    public EmployeeResponse<Long> deleteEmployeeById(Long id) {
        Employee employee = getEmployee(id, ErrorCodeConstants.ER014, true);

        if (EmployeeRole.ADMIN.equals(employee.getEmployeeRole())) {
            MessageResponse message = new MessageResponse(ErrorCodeConstants.ER020, new ArrayList<>());
            throw new BusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, id, message);
        }

        employeeRepository.deleteById(employee.getEmployeeId());
        return new EmployeeResponse<>(HttpStatusConstants.OK, id, new MessageResponse(MsgCodeConstants.MSG003, new ArrayList<>()));
    }

    /**
     * Kiểm tra sự tồn tại của nhân viên dựa trên ID.
     *
     * @param id ID của nhân viên cần kiểm tra.
     * @return true nếu nhân viên tồn tại, false nếu không tồn tại.
     */
    @Override
    public boolean existsById(Long id) {
        return employeeRepository.existsById(id);
    }

    /**
     * Lấy mã đăng nhập (employeeLoginId) của nhân viên theo ID.
     *
     * @param id ID của nhân viên.
     * @return Mã đăng nhập nếu tìm thấy, ngược lại trả về null.
     */
    @Override
    public String getEmployeeLoginIdById(Long id) {
        return employeeRepository.findById(id)
                .map(Employee::getEmployeeLoginId)
                .orElse(null);
    }

    /**
     * Lấy địa chỉ email của nhân viên theo ID.
     *
     * @param id ID của nhân viên.
     * @return Email nếu tìm thấy, ngược lại trả về null.
     */
    @Override
    public String getEmployeeEmailById(Long id) {
        return employeeRepository.findById(id)
                .map(Employee::getEmployeeEmail)
                .orElse(null);
    }


    /**
     * Lấy danh sách nhân viên từ cơ sở dữ liệu dựa trên các tiêu chí lọc và sắp xếp.
     * Cung cấp phân trang để tối ưu hóa việc lấy dữ liệu.
     *
     * @param employeeName         Tên nhân viên để lọc.
     * @param departmentId         ID của phòng ban để lọc.
     * @param ordEmployeeName      Cách sắp xếp tên nhân viên.
     * @param ordCertificationName Cách sắp xếp tên chứng chỉ.
     * @param ordEndDate           Cách sắp xếp ngày kết thúc chứng chỉ.
     * @param sortPriority         Tiêu chí sắp xếp ưu tiên.
     * @param offset               Vị trí bắt đầu của trang dữ liệu.
     * @param limit                Số lượng bản ghi trong mỗi trang.
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
            int limit) {

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
     *
     * @param ordEmployeeName      Cách sắp xếp tên nhân viên.
     * @param ordCertificationName Cách sắp xếp tên chứng chỉ.
     * @param ordEndDate           Cách sắp xếp ngày kết thúc chứng chỉ.
     * @param sortPriority         Tiêu chí sắp xếp ưu tiên.
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
     *
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
     * @throws BusinessException nếu không tìm thấy phòng ban hoặc chứng chỉ tương ứng với ID được truyền vào.
     */
    @Transactional
    @Override
    public EmployeeResponse<Long> addEmployee(EmployeeRequestDTO requestDTO) {
        Employee employee = employeeRequestMapper.toEntity(requestDTO);

        String encodedPassword = passwordEncoder.encode(requestDTO.getEmployeeLoginPassword());
        employee.setEmployeeLoginPassword(encodedPassword);

        employee.setEmployeeRole(EmployeeRole.USER);

        if (requestDTO.getCertifications() != null && !requestDTO.getCertifications().isEmpty()) {
            List<EmployeeCertification> certList = requestDTO.getCertifications().stream()
                    .map(certDTO -> {
                        EmployeeCertification cert = new EmployeeCertification();
                        cert.setStartDate(employeeRequestMapper.parseDate(certDTO.getStartDate()));
                        cert.setEndDate(employeeRequestMapper.parseDate(certDTO.getEndDate()));
                        cert.setScore(new BigDecimal(certDTO.getScore()));
                        cert.setCertification(new Certification(Long.parseLong(certDTO.getCertificationId())));
                        cert.setEmployee(employee);
                        return cert;
                    })
                    .collect(Collectors.toList());

            employee.setEmployeeCertifications(certList);
        }

        // Lưu vào DB
        employeeRepository.save(employee);
        return new EmployeeResponse<>(HttpStatusConstants.OK, employee.getEmployeeId(), new MessageResponse(MsgCodeConstants.MSG001, new ArrayList<>()));
    }

    /**
     * Cập nhật thông tin nhân viên trong hệ thống.
     * Thực hiện các bước sau:
     * - Chuyển đổi DTO thành entity.
     * - Mã hóa mật khẩu nếu có cung cấp mật khẩu mới.
     * - Thiết lập vai trò mặc định là USER.
     * - Xóa các chứng chỉ cũ của nhân viên.
     * - Chuyển đổi và thêm danh sách chứng chỉ mới nếu có.
     * - Lưu thông tin nhân viên đã cập nhật vào cơ sở dữ liệu.
     *
     * @param updateDTO Đối tượng {@link EmployeeRequestDTO} chứa thông tin nhân viên cần cập nhật.
     * @return Đối tượng {@link EmployeeResponse} chứa mã trạng thái, ID nhân viên, và thông điệp kết quả.
     */
    @Transactional
    @Override
    public EmployeeResponse<Long> updateEmployee(EmployeeRequestDTO updateDTO) {
        Employee employee = employeeRequestMapper.toEntity(updateDTO);

        String rawPassword = employee.getEmployeeLoginPassword();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            employee.setEmployeeLoginPassword(encodedPassword);
        }

        employee.setEmployeeRole(EmployeeRole.USER);

        // Xóa certifications trong trường hợp update
        Long employeeId = Long.parseLong(updateDTO.getEmployeeId());
        employeeCertificationRepository.deleteByEmployee_EmployeeId(employeeId);

        if (updateDTO.getCertifications() != null && !updateDTO.getCertifications().isEmpty()) {
            List<EmployeeCertification> certList = updateDTO.getCertifications().stream()
                    .map(certDTO -> {
                        EmployeeCertification cert = new EmployeeCertification();
                        cert.setStartDate(employeeRequestMapper.parseDate(certDTO.getStartDate()));
                        cert.setEndDate(employeeRequestMapper.parseDate(certDTO.getEndDate()));
                        cert.setScore(new BigDecimal(certDTO.getScore()));
                        cert.setCertification(new Certification(Long.parseLong(certDTO.getCertificationId())));
                        cert.setEmployee(employee);
                        return cert;
                    })
                    .collect(Collectors.toList());

            employee.setEmployeeCertifications(certList);
        }
        // Lưu vào DB
        employeeRepository.save(employee);
        return new EmployeeResponse<>(HttpStatusConstants.OK, employeeId, new MessageResponse(MsgCodeConstants.MSG002, new ArrayList<>()));
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

    /**
     * Lấy thông tin nhân viên theo ID. Nếu không tồn tại, ném ra {@link BusinessException}.
     *
     * @param id ID của nhân viên cần tìm.
     * @param errorCode Mã lỗi sẽ sử dụng nếu không tìm thấy nhân viên.
     * @param includeIdInException Cờ xác định có nên đưa ID vào ngoại lệ hay không.
     * @return Đối tượng {@link Employee} tương ứng với ID.
     * @throws BusinessException nếu không tìm thấy nhân viên theo ID.
     */
    private Employee getEmployee(Long id, String errorCode, boolean includeIdInException) {
        List<String> params = new ArrayList<>();
        params.add(ValidationFieldNameMapper.getDisplayName(FieldKey.ID.key()));

        return employeeRepository.findByEmployeeId(id)
                .orElseThrow(() -> {
                    MessageResponse message = new MessageResponse(errorCode, params);
                    if (includeIdInException) {
                        return new BusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, id, message);
                    } else {
                        return new BusinessException(HttpStatusConstants.INTERNAL_SERVER_ERROR, message);
                    }
                });
    }

}
