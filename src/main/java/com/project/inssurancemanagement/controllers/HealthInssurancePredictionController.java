package com.project.inssurancemanagement.controllers;

import com.project.inssurancemanagement.services.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health_insurance_prediction")
public class HealthInssurancePredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/{id}")
    public ResponseEntity<String> predictHealthInsurance(@PathVariable Long id) {
        try {
            double prediction = predictionService.getPredictionAndUpdateResult(id);
            return ResponseEntity.ok("The predicted insurance cost is USD " + prediction);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
