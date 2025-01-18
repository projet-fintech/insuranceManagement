package com.project.inssurancemanagement.repositories;
import com.project.inssurancemanagement.entities.InsurancePrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsurancePredictionRepository extends JpaRepository<InsurancePrediction, Long> {
}