package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsurance;

import java.util.List;
import java.util.Optional;

public interface HealthInsuranceService {

    public void deleteById(Long id) ;

    public HealthInsurance save(HealthInsurance healthInsurance) ;

    public Optional<HealthInsurance> findById(Long id) ;

    public List<HealthInsurance> findAll() ;

    public HealthInsurance updateHealthInsurance(Long id, HealthInsurance updatedHealthInsurance) ;
}
