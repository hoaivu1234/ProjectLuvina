/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationServiceImpl.java, 4/29/2025 hoaivd
 */

package com.luvina.la.service.Impl;

import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.entity.Certification;
import com.luvina.la.mapper.CertificationMapper;
import com.luvina.la.repository.CertificationRepository;
import com.luvina.la.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class triển khai dịch vụ quản lý các trình độ tiếng nhật trong hệ thống.
 * Class này cung cấp các phương thức xử lý nghiệp vụ liên quan đến trình độ tiếng nhật,
 * bao gồm việc lấy danh sách các trình độ tiếng nhật từ cơ sở dữ liệu và chuyển đổi dữ liệu
 * từ thực thể (Entity) sang DTO để trả về cho người dùng.
 *
 * @author hoaivd
 */
@Service
public class CertificationServiceImpl implements CertificationService {
    // Repository để tương tác với bảng trình độ tiếng nhật trong cơ sở dữ liệu
    @Autowired
    private CertificationRepository certificationRepository;

    // Mapper để chuyển đổi giữa các đối tượng Entity và DTO.
    @Autowired
    private CertificationMapper certificationMapper;

    /**
     * Lấy danh sách tất cả chứng chỉ trong hệ thống.
     *
     * @return Danh sách các chứng chỉ dưới dạng {@link CertificationDTO}.
     */
    @Override
    public List<CertificationDTO> getAllCertification() {
        List<Certification> certificationList = certificationRepository.findAll();

        // Chuyển đổi danh sách trình độ tiếng nhật từ Entity sang DTO và trả về kết quả
        return certificationMapper.toList(certificationList);
    }

    /**
     * Kiểm tra xem chứng chỉ với ID được truyền vào có tồn tại trong hệ thống hay không.
     *
     * @param id ID của chứng chỉ cần kiểm tra.
     * @return true nếu tồn tại, false nếu không tồn tại.
     */
    @Override
    public boolean existsById(Long id) {
        return certificationRepository.existsById(id);
    }
}
