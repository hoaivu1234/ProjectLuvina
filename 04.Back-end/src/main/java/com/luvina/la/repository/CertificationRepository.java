/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationRepository.java, 5/8/2025 hoaivd
 */

package com.luvina.la.repository;

import com.luvina.la.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository tương tác với cơ sở dữ liệu để truy vấn thông tin về chứng chỉ (trình độ tiếng nhật).
 *
 * Giao tiếp với bảng dữ liệu {@link Certification} và cung cấp các phương thức truy xuất dữ liệu,
 * bao gồm cả phương thức mặc định của {@link JpaRepository} và các phương thức tùy chỉnh nếu cần.
 *
 * Hiện tại, repository cung cấp phương thức để lấy toàn bộ danh sách chứng chỉ.
 *
 * @author hoaivd
 */
@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    /**
     * Truy vấn tất cả bản ghi chứng chỉ từ cơ sở dữ liệu.
     *
     * @return Danh sách các đối tượng {@link Certification}
     */
    List<Certification> findAll();
}
