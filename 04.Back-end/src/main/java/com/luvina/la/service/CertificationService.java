package com.luvina.la.service;

import com.luvina.la.dto.CertificationDTO;

import java.util.List;

public interface CertificationService {
    List<CertificationDTO> getAllCertification();

    boolean existsById(Long id);
}
