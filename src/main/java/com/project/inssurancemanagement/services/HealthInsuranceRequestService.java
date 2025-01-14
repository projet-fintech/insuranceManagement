package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import com.project.inssurancemanagement.repositories.HealthInsuranceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthInsuranceRequestService {

    @Autowired
    private HealthInsuranceRequestRepository healthInsuranceRequestRepository;

    public List<HealthInsuranceRequest> findAll() {
        return healthInsuranceRequestRepository.findAll();
    }

    public Optional<HealthInsuranceRequest> findById(Long id) {
        return healthInsuranceRequestRepository.findById(id);
    }

    public HealthInsuranceRequest save(HealthInsuranceRequest healthInsuranceRequest) {
        return healthInsuranceRequestRepository.save(healthInsuranceRequest);
    }

    public void deleteById(Long id) {
        healthInsuranceRequestRepository.deleteById(id);
    }


}
