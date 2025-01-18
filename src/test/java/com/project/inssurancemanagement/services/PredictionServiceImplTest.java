package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import com.project.inssurancemanagement.entities.InsurancePrediction;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PredictionServiceImplTest {

    private final PredictionServiceImpl predictionService = new PredictionServiceImpl();

    @Test
    void testClassifyPrediction() {
        assertEquals("Soins de base et besoins ponctuels", predictionService.classifyPrediction(3000));
        assertEquals("Soins réguliers pour des conditions modérées", predictionService.classifyPrediction(10000));
        assertEquals("Soins pour maladies chroniques ou hospitalisation prolongée", predictionService.classifyPrediction(30000));
        assertEquals("Soins complexes ou de long terme", predictionService.classifyPrediction(50000));
    }

    @Test
    void testCalculateMonthlyPayment() {
        assertEquals(300.0, predictionService.calculateMonthlyPayment(5000, "Soins de base et besoins ponctuels"));
        assertEquals(900.0, predictionService.calculateMonthlyPayment(15000, "Soins réguliers pour des conditions modérées"));
        assertEquals(3500.0, predictionService.calculateMonthlyPayment(45000, "Soins pour maladies chroniques ou hospitalisation prolongée"));
        assertEquals(5000.0, predictionService.calculateMonthlyPayment(50000, "Soins complexes ou de long terme"));
    }

    @Test
    void testPreprocessData() {
        HealthInsuranceRequest request = new HealthInsuranceRequest();
        request.setAge(30);
        request.setSex(1);
        request.setBmi(22.5);
        request.setChildren(1);
        request.setSmoker(0);
        request.setUserId(123L);

        Map<String, Object> expectedData = new HashMap<>();
        expectedData.put("age", 30);
        expectedData.put("sex", "M");
        expectedData.put("bmi", 22.5);
        expectedData.put("children", 1);
        expectedData.put("smoker", "no");
        expectedData.put("userId", 123L);

        assertEquals(expectedData, predictionService.preprocessData(request));
    }
}
