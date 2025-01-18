package com.project.inssurancemanagement.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendInsuranceEmail_Success() {
        // Mock API call
        doNothing().when(restTemplate).postForObject(anyString(), any(Map.class), eq(String.class));

        // Execute the method
        notificationService.sendInsuranceEmail(123L, "Test Subject", 1000.0, "Test Category", 100.0);

        // Verify interaction
        verify(restTemplate, times(1)).postForObject(anyString(), any(Map.class), eq(String.class));
    }
}
