package com.luvina.la.repository;

import com.luvina.la.entity.EmployeeCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeCertificationRepository extends JpaRepository<EmployeeCertification, Long> {
//    Optional<EmployeeCertification> findBy
}
