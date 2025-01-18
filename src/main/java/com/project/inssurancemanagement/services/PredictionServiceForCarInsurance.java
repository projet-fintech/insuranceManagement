package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import com.project.inssurancemanagement.repositories.CarInsurancePredictionRepository;
import com.project.inssurancemanagement.repositories.CarInsuranceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PredictionServiceForCarInsurance {

    @Value("${car.prediction.api.url}")
    private String PredictionApiUrl;
    @Autowired
    private CarInsuranceRequestRepository requestRepository;

    @Autowired
    private CarInsurancePredictionRepository predictionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public CarInsurancePrediction getPredictionAndUpdateResult(Long requestId) {
        // Fetch the car insurance request
        Optional<CarInsuranceRequest> optionalRequest = requestRepository.findById(requestId);

        if (optionalRequest.isEmpty()) {
            throw new RuntimeException("CarInsuranceRequest not found with id " + requestId);
        }

        CarInsuranceRequest request = optionalRequest.get();
        System.out.println("Fetched request: " + request);

        // Prepare the data for the Flask API
        Map<String, Object> requestData = preprocessData(request);
        System.out.println("Request data for Flask API: " + requestData);

        // Send the POST request
        Map<String, Object> response = restTemplate.postForObject(PredictionApiUrl, requestData, Map.class);

        if (response == null || !response.containsKey("ensemble_prediction")) {
            throw new RuntimeException("Failed to get prediction from Flask API");
        }

        // Extract the predicted cost
        double predictedCost = ((Number) response.get("ensemble_prediction")).doubleValue();
        System.out.println("Predicted cost: " + predictedCost);

        // Classify the prediction
        String category = classifyPrediction(predictedCost);
        System.out.println("Category: " + category);

        double monthlyPayment = calculateMonthlyPayment(predictedCost, category);
        System.out.println("Monthly payment: " + monthlyPayment);

        // Save the prediction result
        CarInsurancePrediction prediction = new CarInsurancePrediction();
        prediction.setPredictionResult(predictedCost);
        prediction.setCategory(category);
        prediction.setMonthlyPayment(monthlyPayment);
        prediction.setStatus("PENDING"); // Default status
        prediction.setUserId(request.getUserId());
        CarInsurancePrediction savedPrediction = predictionRepository.save(prediction);
        System.out.println("Saved prediction: " + savedPrediction);

        return savedPrediction;
    }

    public Map<String, Object> preprocessData(CarInsuranceRequest request) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("Driver Age", request.getDriverAge());
        requestData.put("Driver Experience", request.getDriverExperience());
        requestData.put("Previous Accidents", request.getPreviousAccidents());
        return requestData;
    }

    private String classifyPrediction(double predictedCost) {
        if (predictedCost <= 400) {
            return "Basic Coverage";
        } else if (predictedCost <= 550) {
            return "Standard Coverage";
        } else {
            return "Luxury Coverage";
        }
    }

    private double calculateMonthlyPayment(double predictedCost, String category) {
        switch (category) {
            case "Basic Coverage":
                return predictedCost * 0.8; // 20% reduction
            case "Standard Coverage":
                return predictedCost * 0.85; // 15% reduction
            case "Premium Coverage":
                return predictedCost * 0.9; // 10% reduction
            case "Luxury Coverage":
                return predictedCost; // No reduction
            default:
                return predictedCost;
        }
    }
}
