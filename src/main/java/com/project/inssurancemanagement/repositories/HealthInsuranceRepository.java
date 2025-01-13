package com.project.inssurancemanagement.repositories;

import com.project.inssurancemanagement.entities.HealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Long> {
}
