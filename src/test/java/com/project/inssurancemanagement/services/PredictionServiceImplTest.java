package com.project.inssurancemanagement.services;

import com.project.inssurancemanagement.entities.HealthInsuranceRequest;
import com.project.inssurancemanagement.entities.InsurancePrediction;
import com.project.inssurancemanagement.repositories.HealthInsuranceRequestRepository;
import com.project.inssurancemanagement.repositories.InsurancePredictionRepository;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPredictionAndUpdateResult_Success() {
        // Mock input data
        Long requestId = 1L;
        HealthInsuranceRequest request = new HealthInsuranceRequest();
        request.setAge(30);
        request.setSex(1);
        request.setBmi(25.0);
        request.setChildren(2);
        request.setSmoker(0);
        request.setUserId(100L);

        when(healthInsuranceRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(Map.of("prediction", 10000.0));

        InsurancePrediction savedPrediction = new InsurancePrediction();
        savedPrediction.setPredictionResult(10000.0);
        savedPrediction.setCategory("Soins réguliers pour des conditions modérées");
        savedPrediction.setMonthlyPayment(900);
        when(insurancePredictionRepository.save(any(InsurancePrediction.class))).thenReturn(savedPrediction);

        // Execute the method
        InsurancePrediction result = predictionService.getPredictionAndUpdateResult(requestId);

        // Assertions
        assertNotNull(result);
        assertEquals(10000.0, result.getPredictionResult());
        assertEquals("Soins réguliers pour des conditions modérées", result.getCategory());
        assertEquals(900, result.getMonthlyPayment());

        verify(healthInsuranceRequestRepository).findById(requestId);
        verify(restTemplate).postForObject(anyString(), any(), eq(Map.class));
        verify(insurancePredictionRepository).save(any(InsurancePrediction.class));
    }

    @Test
    void testGetPredictionAndUpdateResult_RequestNotFound() {
        Long requestId = 1L;

        when(healthInsuranceRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                predictionService.getPredictionAndUpdateResult(requestId)
        );

        assertEquals("HealthInsuranceRequest not found with id " + requestId, exception.getMessage());
        verify(healthInsuranceRequestRepository).findById(requestId);
        verifyNoInteractions(restTemplate, insurancePredictionRepository);
    }
}
