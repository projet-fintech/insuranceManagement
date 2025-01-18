package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import com.project.inssurancemanagement.entities.InsurancePrediction;
import com.project.inssurancemanagement.repositories.HealthInsuranceRequestRepository;
import com.project.inssurancemanagement.repositories.InsurancePredictionRepository;
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
class PredictionServiceImplTest {

    @Mock
    private HealthInsuranceRequestRepository healthInsuranceRequestRepository;

    @Mock
    private InsurancePredictionRepository insurancePredictionRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PredictionServiceImpl predictionService;

    private HealthInsuranceRequest request;
    private Map<String, Object> apiResponse;

    @BeforeEach
    void setUp() {
        request = new HealthInsuranceRequest();
        request.setId(1L);
        request.setAge(30);
        request.setSex(1);
        request.setBmi(25.5);
        request.setChildren(2);
        request.setSmoker(0);
        request.setUserId(123L);

        apiResponse = new HashMap<>();
        apiResponse.put("prediction", 10000.0);
    }

    @Test
    void getPredictionAndUpdateResult_Success() {
        // Mock repository response
        when(healthInsuranceRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(restTemplate.postForObject(anyString(), any(Map.class), eq(Map.class))).thenReturn(apiResponse);
        when(insurancePredictionRepository.save(any(InsurancePrediction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute the method
        InsurancePrediction result = predictionService.getPredictionAndUpdateResult(1L);

        // Verify the result
        assertNotNull(result);
        assertEquals(10000.0, result.getPredictionResult());
        assertEquals("Soins réguliers pour des conditions modérées", result.getCategory());
        assertEquals(900.0, result.getMonthlyPayment());
        assertEquals("PENDING", result.getStatus());
        assertEquals(123L, result.getUserId());

        // Verify interactions
        verify(healthInsuranceRequestRepository, times(1)).findById(1L);
        verify(restTemplate, times(1)).postForObject(anyString(), any(Map.class), eq(Map.class));
        verify(insurancePredictionRepository, times(1)).save(any(InsurancePrediction.class));
    }

    @Test
    void getPredictionAndUpdateResult_RequestNotFound() {
        when(healthInsuranceRequestRepository.findById(1L)).thenReturn(Optional.empty());

        // Verify exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            predictionService.getPredictionAndUpdateResult(1L);
        });

        assertEquals("HealthInsuranceRequest not found with id 1", exception.getMessage());
    }

    @Test
    void getPredictionAndUpdateResult_ApiFailure() {
        when(healthInsuranceRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(restTemplate.postForObject(anyString(), any(Map.class), eq(Map.class))).thenReturn(null);

        // Verify exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            predictionService.getPredictionAndUpdateResult(1L);
        });

        assertEquals("Failed to get prediction from Flask API", exception.getMessage());
    }
}