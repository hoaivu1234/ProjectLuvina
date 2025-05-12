/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationMapper.java, 5/12/2025 hoaivd
 */

package com.luvina.la.mapper;

import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.entity.Certification;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Interface để chuyển đổi giữa các đối tượng DTO và Entity cho Certification.
 * Sử dụng MapStruct để tự động ánh xạ các trường giữa DTO và Entity.
 * Đây là một lớp Mapper giúp thực hiện việc chuyển đổi dữ liệu giữa các tầng (DTO <-> Entity).
 *
 * @author hoaivd
 */
@Mapper(componentModel = "spring")
public interface CertificationMapper {

    /**
     * Chuyển đổi đối tượng CertificationDTO thành Certification Entity.
     *
     * @param dto Đối tượng CertificationDTO cần chuyển đổi.
     * @return Đối tượng Certification Entity sau khi chuyển đổi.
     */
    Certification toEntity(CertificationDTO dto);

    /**
     * Chuyển đổi đối tượng Certification Entity thành CertificationDTO.
     *
     * @param entity Đối tượng Certification Entity cần chuyển đổi.
     * @return Đối tượng CertificationDTO sau khi chuyển đổi.
     */
    CertificationDTO toDTO(Certification entity);

    /**
     * Chuyển đổi danh sách Certification Entities thành danh sách CertificationDTOs.
     *
     * @param list Danh sách các đối tượng Certification Entity.
     * @return Danh sách các đối tượng CertificationDTO sau khi chuyển đổi.
     */
    List<CertificationDTO> toList(List<Certification> list);
}
