package com.project.inssurancemanagement.services;


import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import com.project.inssurancemanagement.entities.InsurancePrediction;

import java.util.Map;

public interface PredictionService {

    public Map<String, Object> preprocessData(HealthInsuranceRequest healthInsurance) ;

    public InsurancePrediction getPredictionAndUpdateResult(Long healthInsuranceId) ;
}
