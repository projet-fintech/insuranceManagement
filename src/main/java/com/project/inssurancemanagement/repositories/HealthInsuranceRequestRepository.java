package com.project.inssurancemanagement.repositories;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthInsuranceRequestRepository extends JpaRepository<HealthInsuranceRequest, Long> {
}
