package com.luvina.la.mapper;

import com.luvina.la.dto.EmployeeCertificationResponseDTO;
import com.luvina.la.dto.EmployeeResponseDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper chuyển từ Entity {@link Employee} sang DTO {@link EmployeeResponseDTO}
 */
@Mapper(componentModel = "spring")
public interface EmployeeResponseMapper {

    /**
     * Chuyển đổi {@link Employee} sang {@link EmployeeResponseDTO}
     *
     * @param employee đối tượng entity
     * @return DTO đã được ánh xạ
     */
    @Mappings({
            @Mapping(source = "department.departmentId", target = "departmentId"),
            @Mapping(source = "department.departmentName", target = "departmentName")
    })
    EmployeeResponseDTO toDto(Employee employee);

    /**
     * Ánh xạ từng EmployeeCertification sang DTO (đây là method quan trọng)
     */
    @Mappings({
            @Mapping(source = "certification.certificationId", target = "certificationId"),
            @Mapping(source = "certification.certificationName", target = "certificationName")
    })
    EmployeeCertificationResponseDTO toCertificationDto(EmployeeCertification cert);

    /**
     * MapStruct sẽ dùng toCertificationDto() để tự map danh sách
     */
    List<EmployeeCertificationResponseDTO> toCertificationDtos(List<EmployeeCertification> certs);
}

