package com.luvina.la.repository;

import com.luvina.la.entity.EmployeeCertification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCertificationRepository extends CrudRepository<EmployeeCertification, Long> {
}
