package com.project.inssurancemanagement.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    @Value("${notification.api.url}")
    private String notificationServiceUrl;
    @Autowired
    private RestTemplate restTemplate;

    public void sendInsuranceEmail(long userId, String subject, double predictedCost, String category, double monthlyPayment) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("userId", userId);
        requestData.put("subject", subject);
        requestData.put("predictedCost", predictedCost);
        requestData.put("category", category);
        requestData.put("monthlyPayment", monthlyPayment);

        restTemplate.postForObject(notificationServiceUrl, requestData, String.class);
    }
}