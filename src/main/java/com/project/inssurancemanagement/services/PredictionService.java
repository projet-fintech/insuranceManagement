package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsurance;

import java.util.Map;

public interface PredictionService {

    public Map<String, Object> preprocessData(HealthInsurance healthInsurance) ;

    public double getPredictionAndUpdateResult(Long healthInsuranceId) ;
}
