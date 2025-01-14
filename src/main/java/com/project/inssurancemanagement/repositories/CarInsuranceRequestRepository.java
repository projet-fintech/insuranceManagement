package com.project.inssurancemanagement.repositories;

import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarInsuranceRequestRepository extends JpaRepository<CarInsuranceRequest, Long> {
}
