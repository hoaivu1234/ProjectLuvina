/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationService.java, 5/8/2025 hoaivd
 */

package com.luvina.la.service;

import com.luvina.la.dto.CertificationDTO;

import java.util.List;

/**
 * Service xử lý các nghiệp vụ liên quan đến chứng chỉ nhân viên.
 *
 * Cung cấp các phương thức để truy vấn danh sách chứng chỉ và kiểm tra sự tồn tại của chứng chỉ theo ID.
 * Interface này sẽ được hiện thực bởi một lớp service implementation tương ứng (ví dụ: {@code CertificationServiceImpl}).
 *
 * Các phương thức trong interface này giúp tách biệt logic nghiệp vụ khỏi tầng controller và repository.
 *
 * @author hoaivd
 */
public interface CertificationService {
    /**
     * Lấy danh sách tất cả chứng chỉ nhân viên.
     *
     * @return Danh sách {@link CertificationDTO} đại diện cho tất cả chứng chỉ hiện có trong hệ thống.
     */
    List<CertificationDTO> getAllCertification();

    /**
     * Kiểm tra sự tồn tại của chứng chỉ theo ID.
     *
     * @param id ID của chứng chỉ cần kiểm tra.
     * @return {@code true} nếu chứng chỉ tồn tại, ngược lại {@code false}.
     */
    boolean existsById(Long id);
}
