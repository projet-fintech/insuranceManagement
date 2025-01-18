package com.project.inssurancemanagement.repositories;

import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarInsurancePredictionRepository extends JpaRepository<CarInsurancePrediction, Long> {
}
