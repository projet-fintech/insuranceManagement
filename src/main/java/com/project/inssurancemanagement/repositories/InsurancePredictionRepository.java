package com.project.inssurancemanagement.repositories;
import com.project.inssurancemanagement.entities.InsurancePrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancePredictionRepository extends JpaRepository<InsurancePrediction, Long> {
}