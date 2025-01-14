package com.project.inssurancemanagement.controllers;

import com.project.inssurancemanagement.services.PredictionServiceForCarInsurance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import com.project.inssurancemanagement.services.CarInsurancePredictionService;
import com.project.inssurancemanagement.services.CarInsuranceRequestService;

@RestController
@RequestMapping("/api/car-insurance")
public class CarInsuranceController {

    @Autowired
    private CarInsuranceRequestService requestService;

    @Autowired
    private CarInsurancePredictionService predictionService;

    @Autowired
    private PredictionServiceForCarInsurance predictionLogicService;

    @PostMapping("/request")
    public ResponseEntity<CarInsuranceRequest> createRequest(@RequestBody CarInsuranceRequest request) {
        CarInsuranceRequest savedRequest = requestService.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    @PostMapping("/predict/{requestId}")
    public ResponseEntity<CarInsurancePrediction> predictAndClassify(@PathVariable Long requestId) {
        System.out.println("Predicting for request ID: " + requestId);
        CarInsurancePrediction savedPrediction = predictionLogicService.getPredictionAndUpdateResult(requestId);
        System.out.println("Prediction saved: " + savedPrediction);
        return ResponseEntity.ok(savedPrediction);
    }

    @PutMapping("/approve/{predictionId}")
    public ResponseEntity<CarInsurancePrediction> approvePrediction(@PathVariable Long predictionId) {
        CarInsurancePrediction prediction = predictionService.updateStatus(predictionId, "AGREED");
        return ResponseEntity.ok(prediction);
    }

    @PutMapping("/reject/{predictionId}")
    public ResponseEntity<CarInsurancePrediction> rejectPrediction(@PathVariable Long predictionId) {
        CarInsurancePrediction prediction = predictionService.updateStatus(predictionId, "NOT_AGREED");
        return ResponseEntity.ok(prediction);
    }
}
