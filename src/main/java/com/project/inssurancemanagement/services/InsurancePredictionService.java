package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.InsurancePrediction;
import com.project.inssurancemanagement.repositories.InsurancePredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsurancePredictionService {

    @Autowired
    private InsurancePredictionRepository insurancePredictionRepository;

    public List<InsurancePrediction> findAll() {
        return insurancePredictionRepository.findAll();
    }

    public Optional<InsurancePrediction> findById(Long id) {
        return insurancePredictionRepository.findById(id);
    }

    public InsurancePrediction save(InsurancePrediction insurancePrediction) {
        return insurancePredictionRepository.save(insurancePrediction);
    }

    public void deleteById(Long id) {
        insurancePredictionRepository.deleteById(id);
    }

    public InsurancePrediction updateStatus(Long id, String status) {
        return insurancePredictionRepository.findById(id)
                .map(prediction -> {
                    prediction.setStatus(status);
                    return insurancePredictionRepository.save(prediction);
                })
                .orElseThrow(() -> new RuntimeException("InsurancePrediction not found with id " + id));
    }
}
