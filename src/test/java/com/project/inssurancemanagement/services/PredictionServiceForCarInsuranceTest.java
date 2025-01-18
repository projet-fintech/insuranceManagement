package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.CarInsurancePrediction;
import com.project.inssurancemanagement.entities.CarInsuranceRequest;
import com.project.inssurancemanagement.repositories.CarInsurancePredictionRepository;
import com.project.inssurancemanagement.repositories.CarInsuranceRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PredictionServiceForCarInsuranceTest {

    @Mock
    private CarInsuranceRequestRepository requestRepository;

    @Mock
    private CarInsurancePredictionRepository predictionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PredictionServiceForCarInsurance predictionService;

    private CarInsuranceRequest request;
    private Map<String, Object> apiResponse;

    @BeforeEach
    void setUp() {
        request = new CarInsuranceRequest();
        request.setId(1L);
        request.setDriverAge(30);
        request.setDriverExperience(5);
        request.setPreviousAccidents(2);
        request.setUserId(123L);

        apiResponse = new HashMap<>();
        apiResponse.put("ensemble_prediction", 500.0);
    }

    @Test
    void getPredictionAndUpdateResult_Success() {
        // Mock repository response
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(restTemplate.postForObject(anyString(), any(Map.class), eq(Map.class))).thenReturn(apiResponse);
        when(predictionRepository.save(any(CarInsurancePrediction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the method
        CarInsurancePrediction result = predictionService.getPredictionAndUpdateResult(1L);

        // Verify the result
        assertNotNull(result);
        assertEquals(500.0, result.getPredictionResult());
        assertEquals("Standard Coverage", result.getCategory());
        assertEquals(425.0, result.getMonthlyPayment()); // 500 * 0.85
        assertEquals("PENDING", result.getStatus());
        assertEquals(123L, result.getUserId());

        // Verify interactions
        verify(requestRepository, times(1)).findById(1L);
        verify(restTemplate, times(1)).postForObject(anyString(), any(Map.class), eq(Map.class));
        verify(predictionRepository, times(1)).save(any(CarInsurancePrediction.class));
    }

    @Test
    void getPredictionAndUpdateResult_RequestNotFound() {
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            predictionService.getPredictionAndUpdateResult(1L);
        });

        assertEquals("CarInsuranceRequest not found with id 1", exception.getMessage());
    }

    @Test
    void getPredictionAndUpdateResult_ApiFailure() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(restTemplate.postForObject(anyString(), any(Map.class), eq(Map.class))).thenReturn(null);

        // Verify exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            predictionService.getPredictionAndUpdateResult(1L);
        });

        assertEquals("Failed to get prediction from Flask API", exception.getMessage());
    }
}