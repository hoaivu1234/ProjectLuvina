package com.luvina.la.repository;

import com.luvina.la.entity.Certification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends CrudRepository<Certification, Long> {
}
