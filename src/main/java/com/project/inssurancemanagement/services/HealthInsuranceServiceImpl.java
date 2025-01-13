package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsurance;
import com.project.inssurancemanagement.repositories.HealthInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthInsuranceServiceImpl implements HealthInsuranceService {

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Override
    public List<HealthInsurance> findAll() {
        return healthInsuranceRepository.findAll();
    }

    @Override
    public Optional<HealthInsurance> findById(Long id) {
        return healthInsuranceRepository.findById(id);
    }

    @Override
    public HealthInsurance save(HealthInsurance healthInsurance) {
        return healthInsuranceRepository.save(healthInsurance);
    }

    @Override
    public void deleteById(Long id) {
        healthInsuranceRepository.deleteById(id);
    }

    @Override
    public HealthInsurance updateHealthInsurance(Long id, HealthInsurance updatedHealthInsurance) {
        return healthInsuranceRepository.findById(id)
                .map(healthInsurance -> {
                    healthInsurance.setAge(updatedHealthInsurance.getAge());
                    healthInsurance.setSex(updatedHealthInsurance.getSex());
                    healthInsurance.setBmi(updatedHealthInsurance.getBmi());
                    healthInsurance.setChildren(updatedHealthInsurance.getChildren());
                    healthInsurance.setSmoker(updatedHealthInsurance.getSmoker());
                    healthInsurance.setPredictionResult(updatedHealthInsurance.getPredictionResult());
                    return healthInsuranceRepository.save(healthInsurance);
                })
                .orElseThrow(() -> new RuntimeException("HealthInsurance not found with id " + id));
    }
}
