package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PredictionServiceForCarInsuranceTest {

    private final PredictionServiceForCarInsurance predictionService = new PredictionServiceForCarInsurance();

    @Test
    void testClassifyPrediction() {
        assertEquals("Basic Coverage", predictionService.classifyPrediction(300));
        assertEquals("Standard Coverage", predictionService.classifyPrediction(500));
        assertEquals("Luxury Coverage", predictionService.classifyPrediction(800));
    }

    @Test
    void testCalculateMonthlyPayment() {
        assertEquals(320.0, predictionService.calculateMonthlyPayment(400, "Basic Coverage"));
        assertEquals(467.5, predictionService.calculateMonthlyPayment(550, "Standard Coverage"));
        assertEquals(1000.0, predictionService.calculateMonthlyPayment(1000, "Luxury Coverage"));
    }

    @Test
    void testPreprocessData() {
        CarInsuranceRequest request = new CarInsuranceRequest();
        request.setDriverAge(35);
        request.setDriverExperience(10);
        request.setPreviousAccidents(2);

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("Driver Age", 35);
        expectedData.put("Driver Experience", 10);
        expectedData.put("Previous Accidents", 2);

        assertEquals(expectedData, predictionService.preprocessData(request));
    }
}
