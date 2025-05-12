/**
 * Copyright(C) 2025  Luvina Software Company
 * EmployeeMapper.java, 4/29/2025 hoaivd
 */

package com.luvina.la.mapper;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Date;
import java.util.List;

/**
 * Interface để chuyển đổi giữa các đối tượng DTO và Entity cho Employee.
 * Sử dụng MapStruct để tự động ánh xạ các trường giữa DTO và Entity.
 * Cung cấp các phương thức để ánh xạ dữ liệu giữa Entity và DTO cho Employee, cũng như xử lý các trường con như certification.
 *
 * @author hoaivd
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    /**
     * Chuyển đổi đối tượng EmployeeDTO thành Employee Entity.
     *
     * @param dto Đối tượng EmployeeDTO cần chuyển đổi.
     * @return Đối tượng Employee Entity sau khi chuyển đổi.
     */
    Employee toEntity(EmployeeDTO dto);

    /**
     * Chuyển đổi đối tượng Employee Entity thành EmployeeDTO, với các ánh xạ đặc biệt cho các trường con.
     * Sử dụng các phương thức bổ sung để lấy thông tin từ danh sách EmployeeCertification và ánh xạ vào các trường như departmentName, certificationName, endDate, và score.
     *
     * @param entity Đối tượng Employee Entity cần chuyển đổi.
     * @return Đối tượng EmployeeDTO sau khi chuyển đổi.
     */
    @Mapping(source = "department.departmentName", target = "departmentName")  // Ánh xạ departmentName từ Employee's department.
    @Mapping(source = "employeeCertifications", target = "certificationName", qualifiedByName = "mapFirstCertificationName")  // Ánh xạ certificationName từ EmployeeCertification đầu tiên.
    @Mapping(source = "employeeCertifications", target = "endDate", qualifiedByName = "mapFirstEndDate")  // Ánh xạ endDate từ EmployeeCertification đầu tiên.
    @Mapping(source = "employeeCertifications", target = "score", qualifiedByName = "mapFirstScore")  // Ánh xạ score từ EmployeeCertification đầu tiên.
    EmployeeDTO toDTO(Employee entity);

    /**
     * Chuyển đổi danh sách Employee Entities thành danh sách EmployeeDTOs.
     *
     * @param employees Danh sách các đối tượng Employee Entity.
     * @return Danh sách các đối tượng EmployeeDTO sau khi chuyển đổi.
     */
    List<EmployeeDTO> toList(List<Employee> employees);

    /**
     * Phương thức ánh xạ để lấy tên chứng chỉ từ EmployeeCertification đầu tiên trong danh sách.
     * Nếu danh sách EmployeeCertification trống hoặc chứng chỉ là null, trả về null.
     *
     * @param certs Danh sách EmployeeCertification.
     * @return Tên chứng chỉ của chứng chỉ đầu tiên, hoặc null nếu không có.
     */
    @Named("mapFirstCertificationName")
    default String mapFirstCertificationName(List<EmployeeCertification> certs) {
        if (certs == null || certs.isEmpty() || certs.get(0).getCertification() == null) {
            return null;
        }
        return certs.get(0).getCertification().getCertificationName();
    }

    /**
     * Phương thức ánh xạ để lấy ngày kết thúc của chứng chỉ từ EmployeeCertification đầu tiên trong danh sách.
     *
     * @param certs Danh sách EmployeeCertification.
     * @return Ngày kết thúc của chứng chỉ đầu tiên, hoặc null nếu không có.
     */
    @Named("mapFirstEndDate")
    default Date mapFirstEndDate(List<EmployeeCertification> certs) {
        return (certs == null || certs.isEmpty()) ? null : certs.get(0).getEndDate();
    }

    /**
     * Phương thức ánh xạ để lấy điểm số từ EmployeeCertification đầu tiên trong danh sách.
     * Nếu điểm số là null, trả về giá trị mặc định là 0.
     *
     * @param certs Danh sách EmployeeCertification.
     * @return Điểm số của chứng chỉ đầu tiên, hoặc 0 nếu không có.
     */
    @Named("mapFirstScore")
    default int mapFirstScore(List<EmployeeCertification> certs) {
        if (certs == null || certs.isEmpty() || certs.get(0).getScore() == null) {
            return 0;
        }
        return certs.get(0).getScore().intValue();
    }
}
