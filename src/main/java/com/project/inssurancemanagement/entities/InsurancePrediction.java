package com.project.inssurancemanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InsurancePrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double predictionResult; // Predicted insurance cost
    private String category;         // Category based on cost range
    private double monthlyPayment;   // Monthly payment based on category
    private String status;           // AGREED, NOT_AGREED
    private long userId;
}