package com.project.inssurancemanagement.repositories;

import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarInsurancePredictionRepository extends JpaRepository<CarInsurancePrediction, Long> {
}
