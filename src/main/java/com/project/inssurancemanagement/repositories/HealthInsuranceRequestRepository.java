package com.project.inssurancemanagement.repositories;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthInsuranceRequestRepository extends JpaRepository<HealthInsuranceRequest, Long> {
}
