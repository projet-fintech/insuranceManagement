package com.project.inssurancemanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendInsuranceEmail() {
        long userId = 1L;
        String subject = "Insurance Prediction";
        double predictedCost = 10000.0;
        String category = "Soins réguliers pour des conditions modérées";
        double monthlyPayment = 900.0;

        doNothing().when(restTemplate).postForObject(anyString(), any(), eq(String.class));

        notificationService.sendInsuranceEmail(userId, subject, predictedCost, category, monthlyPayment);

        Map<String, Object> expectedRequest = new HashMap<>();
        expectedRequest.put("userId", userId);
        expectedRequest.put("subject", subject);
        expectedRequest.put("predictedCost", predictedCost);
        expectedRequest.put("category", category);
        expectedRequest.put("monthlyPayment", monthlyPayment);

        verify(restTemplate).postForObject(anyString(), eq(expectedRequest), eq(String.class));
    }
}
