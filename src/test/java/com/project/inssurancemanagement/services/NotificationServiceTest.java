package com.project.inssurancemanagement.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Test
    void testSendInsuranceEmail() {
        RestTemplate restTemplateMock = mock(RestTemplate.class);
        NotificationService notificationService = new NotificationService();
        notificationService.restTemplate = restTemplateMock;

        // Définir une URL de test
        notificationService.notificationServiceUrl = "http://test-url.com";

        long userId = 123L;
        String subject = "Insurance Notification";
        double predictedCost = 5000.0;
        String category = "Basic Coverage";
        double monthlyPayment = 400.0;

        Map<String, Object> expectedRequestData = new HashMap<>();
        expectedRequestData.put("userId", userId);
        expectedRequestData.put("subject", subject);
        expectedRequestData.put("predictedCost", predictedCost);
        expectedRequestData.put("category", category);
        expectedRequestData.put("monthlyPayment", monthlyPayment);

        notificationService.sendInsuranceEmail(userId, subject, predictedCost, category, monthlyPayment);

        verify(restTemplateMock, times(1)).postForObject(eq("http://test-url.com"), eq(expectedRequestData), eq(String.class));
    }
}
