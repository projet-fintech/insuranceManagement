package com.project.inssurancemanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import com.project.inssurancemanagement.entities.InsurancePrediction;
import com.project.inssurancemanagement.services.*;

@RestController
@RequestMapping("/api/insurance")
public class HealthInsuranceController {

    @Autowired
    private HealthInsuranceRequestService healthInsuranceRequestService;

    @Autowired
    private InsurancePredictionService insurancePredictionService;

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/request")
    public ResponseEntity<HealthInsuranceRequest> createRequest(@RequestBody HealthInsuranceRequest request) {
        HealthInsuranceRequest savedRequest = healthInsuranceRequestService.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    @PostMapping("/predict/{requestId}")
    public ResponseEntity<InsurancePrediction> predictAndClassify(@PathVariable Long requestId) {
        System.out.println("Predicting for request ID: " + requestId);
        InsurancePrediction savedPrediction = predictionService.getPredictionAndUpdateResult(requestId);
        System.out.println("Prediction saved: " + savedPrediction);
        return ResponseEntity.ok(savedPrediction);
    }

    @PutMapping("/approve/{predictionId}")
    public ResponseEntity<InsurancePrediction> approvePrediction(@PathVariable Long predictionId) {
        InsurancePrediction prediction = insurancePredictionService.updateStatus(predictionId, "AGREED");
        return ResponseEntity.ok(prediction);
    }

    @PutMapping("/reject/{predictionId}")
    public ResponseEntity<InsurancePrediction> rejectPrediction(@PathVariable Long predictionId) {
        InsurancePrediction prediction = insurancePredictionService.updateStatus(predictionId, "NOT_AGREED");
        return ResponseEntity.ok(prediction);
    }
}