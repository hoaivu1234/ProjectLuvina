package com.luvina.la.mapper;

import com.luvina.la.dto.EmployeeDTO;
import com.luvina.la.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * use:
 *  EmployeeMapper.MAPPER.toEntity(dto);
 *  EmployeeMapper.MAPPER.toList(list);
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee toEntity(EmployeeDTO entity);
    @Mapping(source = "department.departmentName", target = "departmentName")
    @Mapping(source = "employeeCertifications[0].certification.name", target = "certificationName")
    EmployeeDTO toDTO(Employee entity);
    List<EmployeeDTO> toList(List<Employee> list);

}
