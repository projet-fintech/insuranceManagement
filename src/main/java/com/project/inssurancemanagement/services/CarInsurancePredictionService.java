package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import com.project.inssurancemanagement.repositories.CarInsurancePredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarInsurancePredictionService {

    @Autowired
    private CarInsurancePredictionRepository predictionRepository;

    public List<CarInsurancePrediction> findAll() {
        return predictionRepository.findAll();
    }

    public Optional<CarInsurancePrediction> findById(Long id) {
        return predictionRepository.findById(id);
    }

    public CarInsurancePrediction save(CarInsurancePrediction prediction) {
        return predictionRepository.save(prediction);
    }

    public void deleteById(Long id) {
        predictionRepository.deleteById(id);
    }

    public CarInsurancePrediction updateStatus(Long id, String status) {
        return predictionRepository.findById(id)
                .map(prediction -> {
                    prediction.setStatus(status);
                    return predictionRepository.save(prediction);
                })
                .orElseThrow(() -> new RuntimeException("CarInsurancePrediction not found with id " + id));
    }
}
