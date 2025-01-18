package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import com.project.inssurancemanagement.repositories.CarInsurancePredictionRepository;
import com.project.inssurancemanagement.repositories.CarInsuranceRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PredictionServiceForCarInsuranceTest {

    @Mock
    private CarInsuranceRequestRepository requestRepository;

    @Mock
    private CarInsurancePredictionRepository predictionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PredictionServiceForCarInsurance service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPredictionAndUpdateResult_Success() {
        Long requestId = 1L;
        CarInsuranceRequest request = new CarInsuranceRequest();
        request.setDriverAge(30);
        request.setDriverExperience(5);
        request.setPreviousAccidents(2);

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(Map.of("ensemble_prediction", 450.0));

        CarInsurancePrediction savedPrediction = new CarInsurancePrediction();
        savedPrediction.setPredictionResult(450.0);
        savedPrediction.setCategory("Standard Coverage");
        savedPrediction.setMonthlyPayment(382.5);
        when(predictionRepository.save(any(CarInsurancePrediction.class))).thenReturn(savedPrediction);

        CarInsurancePrediction result = service.getPredictionAndUpdateResult(requestId);

        assertNotNull(result);
        assertEquals(450.0, result.getPredictionResult());
        assertEquals("Standard Coverage", result.getCategory());
        assertEquals(382.5, result.getMonthlyPayment());

        verify(requestRepository).findById(requestId);
        verify(restTemplate).postForObject(anyString(), any(), eq(Map.class));
        verify(predictionRepository).save(any(CarInsurancePrediction.class));
    }
}
