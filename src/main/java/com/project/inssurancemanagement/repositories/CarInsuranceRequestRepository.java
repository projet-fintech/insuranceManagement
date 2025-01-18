package com.project.inssurancemanagement.repositories;

import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarInsuranceRequestRepository extends JpaRepository<CarInsuranceRequest, Long> {
}
