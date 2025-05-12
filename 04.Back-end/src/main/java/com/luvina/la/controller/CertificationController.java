/**
 * Copyright(C) 2025  Luvina Software Company
 * CertificationController.java, 5/12/2025 hoaivd
 */

package com.luvina.la.controller;

import com.luvina.la.common.HttpStatusConstants;
import com.luvina.la.dto.CertificationDTO;
import com.luvina.la.payload.CertificationResponse;
import com.luvina.la.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller xử lý các yêu cầu liên quan đến trình độ tiếng nhật.
 * Cung cấp các endpoint để lấy danh sách trình độ tiếng nhật từ service và trả về thông tin cho client.
 *
 * @author hoaivd
 */
@RestController
@RequestMapping("/certifications")
public class CertificationController {
    @Autowired
    private CertificationService certificationService;

    /**
     * Lấy danh sách tất cả các trình độ tiếng nhật từ service và trả về response với mã trạng thái OK.
     *
     * @return ResponseEntity<CertificationResponse<List<CertificationDTO>>> đối tượng chứa mã trạng thái và danh sách các trình độ tiếng nhật
     */
    @GetMapping("")
    ResponseEntity<CertificationResponse<List<CertificationDTO>>> getAllCertificatons() {
        // Lấy danh sách các trình độ tiếng nhật từ dịch vụ
        List<CertificationDTO> certifications = certificationService.getAllCertification();

        // Trả về phản hồi với mã trạng thái OK và dữ liệu trình độ tiếng nhật
        return ResponseEntity.ok(new CertificationResponse(HttpStatusConstants.OK, certifications));
    }
}
