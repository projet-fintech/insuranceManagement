package com.project.inssurancemanagement.controllers;

import com.project.inssurancemanagement.entities.HealthInsurance;
import com.project.inssurancemanagement.services.HealthInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/health-insurances")
public class HealthInsuranceController {

    @Autowired
    private HealthInsuranceService healthInsuranceService;

    // Get all health insurances
    @GetMapping
    public ResponseEntity<List<HealthInsurance>> getAllHealthInsurances() {
        List<HealthInsurance> healthInsurances = healthInsuranceService.findAll();
        return new ResponseEntity<>(healthInsurances, HttpStatus.OK);
    }

    // Get a health insurance by ID
    @GetMapping("/{id}")
    public ResponseEntity<HealthInsurance> getHealthInsuranceById(@PathVariable Long id) {
        Optional<HealthInsurance> healthInsurance = healthInsuranceService.findById(id);
        return healthInsurance.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create a new health insurance
    @PostMapping
    public ResponseEntity<HealthInsurance> createHealthInsurance(@RequestBody HealthInsurance healthInsurance) {
        HealthInsurance savedHealthInsurance = healthInsuranceService.save(healthInsurance);
        return new ResponseEntity<>(savedHealthInsurance, HttpStatus.CREATED);
    }

    // Update an existing health insurance
    @PutMapping("/{id}")
    public ResponseEntity<HealthInsurance> updateHealthInsurance(@PathVariable Long id, @RequestBody HealthInsurance updatedHealthInsurance) {
        Optional<HealthInsurance> existingHealthInsurance = healthInsuranceService.findById(id);

        if (existingHealthInsurance.isPresent()) {
            HealthInsurance healthInsurance = existingHealthInsurance.get();
            // Update the fields of the existing health insurance with the new values
            healthInsurance.setAge(updatedHealthInsurance.getAge());
            healthInsurance.setSex(updatedHealthInsurance.getSex());
            healthInsurance.setBmi(updatedHealthInsurance.getBmi());
            healthInsurance.setChildren(updatedHealthInsurance.getChildren());
            healthInsurance.setSmoker(updatedHealthInsurance.getSmoker());
            healthInsurance.setPredictionResult(updatedHealthInsurance.getPredictionResult());

            HealthInsurance savedHealthInsurance = healthInsuranceService.save(healthInsurance);
            return new ResponseEntity<>(savedHealthInsurance, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a health insurance by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHealthInsurance(@PathVariable Long id) {
        healthInsuranceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}