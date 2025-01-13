package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsurance;
import com.project.inssurancemanagement.repositories.HealthInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PredictionServiceImpl implements PredictionService {

    private static final String PREDICTION_API_URL = "http://127.0.0.1:5000/predict";

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Autowired
    private RestTemplate restTemplate;

    public double getPredictionAndUpdateResult(Long healthInsuranceId) {
        // Fetch the health insurance record
        Optional<HealthInsurance> optionalHealthInsurance = healthInsuranceRepository.findById(healthInsuranceId);

        if (optionalHealthInsurance.isEmpty()) {
            throw new RuntimeException("HealthInsurance record with ID " + healthInsuranceId + " not found");
        }

        HealthInsurance healthInsurance = optionalHealthInsurance.get();

        // Prepare the data for the Flask API
        Map<String, Object> requestData = preprocessData(healthInsurance);

        // Send the POST request
        Map<String, Object> response = restTemplate.postForObject(PREDICTION_API_URL, requestData, Map.class);

        if (response == null || !response.containsKey("prediction")) {
            throw new RuntimeException("Failed to get prediction from Flask API");
        }

        // Extract the predicted cost
        double predictedCost = ((Number) response.get("prediction")).doubleValue();

        // Update the health insurance record with the predicted cost
        healthInsurance.setPredictionResult(predictedCost);
        healthInsuranceRepository.save(healthInsurance);

        return predictedCost;
    }

    public Map<String, Object> preprocessData(HealthInsurance healthInsurance) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("age", healthInsurance.getAge());
        requestData.put("sex", healthInsurance.getSex());
        requestData.put("bmi", healthInsurance.getBmi());
        requestData.put("children", healthInsurance.getChildren());
        requestData.put("smoker", healthInsurance.getSmoker());
        requestData.put("predictionResult", healthInsurance.getPredictionResult());
        requestData.put("userId", healthInsurance.getUserId());
        return requestData;
    }
}