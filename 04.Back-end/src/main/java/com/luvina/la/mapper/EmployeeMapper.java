package com.luvina.la.mapper;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import com.luvina.la.entity.EmployeeCertification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;

/**
 * use:
 *  EmployeeMapper.MAPPER.toEntity(dto);
 *  EmployeeMapper.MAPPER.toList(list);
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeDTO dto);

    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "employeeCertifications", target = "certificationName", qualifiedByName = "mapFirstCertificationName")
    @Mapping(source = "employeeCertifications", target = "endDate", qualifiedByName = "mapFirstEndDate")
    @Mapping(source = "employeeCertifications", target = "score", qualifiedByName = "mapFirstScore")
    EmployeeDTO toDTO(Employee entity);

    List<EmployeeDTO> toList(List<Employee> employees);

    @Named("mapFirstCertificationName")
    default String mapFirstCertificationName(List<EmployeeCertification> certs) {
        if (certs == null || certs.isEmpty() || certs.get(0).getCertification() == null) {
            return null;
        }
        return certs.get(0).getCertification().getCertificationName();
    }

    @Named("mapFirstEndDate")
    default Date mapFirstEndDate(List<EmployeeCertification> certs) {
        return (certs == null || certs.isEmpty()) ? null : certs.get(0).getEndDate();
    }

    @Named("mapFirstScore")
    default int mapFirstScore(List<EmployeeCertification> certs) {
        if (certs == null || certs.isEmpty() || certs.get(0).getScore() == null) {
            return 0;
        }
        return certs.get(0).getScore().intValue();
    }
}
