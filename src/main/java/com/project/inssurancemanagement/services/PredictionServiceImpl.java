package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import com.project.inssurancemanagement.entities.InsurancePrediction;
import com.project.inssurancemanagement.repositories.HealthInsuranceRequestRepository;
import com.project.inssurancemanagement.repositories.InsurancePredictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PredictionServiceImpl implements PredictionService {
    @Value("${health.prediction.api.url}")
    private String healthPredictionUrl;
    @Autowired
    private HealthInsuranceRequestRepository healthInsuranceRequestRepository;

    @Autowired
    private InsurancePredictionRepository insurancePredictionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    @Override
    public InsurancePrediction getPredictionAndUpdateResult(Long requestId) {
        // Fetch the health insurance request
        Optional<HealthInsuranceRequest> optionalRequest = healthInsuranceRequestRepository.findById(requestId);

        if (optionalRequest.isEmpty()) {
            throw new RuntimeException("HealthInsuranceRequest not found with id " + requestId);
        }

        HealthInsuranceRequest request = optionalRequest.get();
        System.out.println("Fetched request: " + request);

        // Prepare the data for the Flask API
        Map<String, Object> requestData = preprocessData(request);
        System.out.println("Request data for Flask API: " + requestData);

        // Send the POST request
        Map<String, Object> response = restTemplate.postForObject(healthPredictionUrl, requestData, Map.class);

        if (response == null || !response.containsKey("prediction")) {
            throw new RuntimeException("Failed to get prediction from Flask API");
        }

        // Extract the predicted cost
        double predictedCost = ((Number) response.get("prediction")).doubleValue();
        System.out.println("Predicted cost: " + predictedCost);

        // Classify the prediction
        String category = classifyPrediction(predictedCost);
        System.out.println("Category: " + category);

        double monthlyPayment = calculateMonthlyPayment(predictedCost, category);
        System.out.println("Monthly payment: " + monthlyPayment);

        // Save the prediction result
        InsurancePrediction prediction = new InsurancePrediction();
        prediction.setPredictionResult(predictedCost);
        prediction.setCategory(category);
        prediction.setMonthlyPayment(monthlyPayment);
        prediction.setStatus("PENDING"); // Default status
        prediction.setUserId(request.getUserId());
        InsurancePrediction savedPrediction = insurancePredictionRepository.save(prediction);
        System.out.println("Saved prediction: " + savedPrediction);

        return savedPrediction;
    }

    public Map<String, Object> preprocessData(HealthInsuranceRequest request) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("age", request.getAge());
        requestData.put("sex", request.getSex());
        requestData.put("bmi", request.getBmi());
        requestData.put("children", request.getChildren());
        requestData.put("smoker", request.getSmoker());
        requestData.put("userId", request.getUserId());
        return requestData;
    }

    public String classifyPrediction(double predictedCost) {
        if (predictedCost <= 5000) {
            return "Soins de base et besoins ponctuels";
        } else if (predictedCost <= 15000) {
            return "Soins réguliers pour des conditions modérées";
        } else if (predictedCost <= 45000) {
            return "Soins pour maladies chroniques ou hospitalisation prolongée";
        } else {
            return "Soins complexes ou de long terme";
        }
    }

    public double calculateMonthlyPayment(double predictedCost, String category) {
        switch (category) {
            case "Soins de base et besoins ponctuels":
                return 300.00; // 10% reduction
            case "Soins réguliers pour des conditions modérées":
                return 900; // 15% reduction
            case "Soins pour maladies chroniques ou hospitalisation prolongée":
                return 3500; // 20% reduction
            case "Soins complexes ou de long terme":
                return 5000; // 25% reduction
            default:
                return predictedCost;
        }
    }
}