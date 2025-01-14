package com.project.inssurancemanagement.services;


import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import com.project.inssurancemanagement.repositories.CarInsurancePredictionRepository;
import com.project.inssurancemanagement.repositories.CarInsuranceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarInsuranceRequestService {

    @Autowired
    private CarInsuranceRequestRepository requestRepository;

    public List<CarInsuranceRequest> findAll() {
        return requestRepository.findAll();
    }

    public Optional<CarInsuranceRequest> findById(Long id) {
        return requestRepository.findById(id);
    }

    public CarInsuranceRequest save(CarInsuranceRequest request) {
        return requestRepository.save(request);
    }

    public void deleteById(Long id) {
        requestRepository.deleteById(id);
    }
}