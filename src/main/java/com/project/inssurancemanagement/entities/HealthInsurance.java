package com.project.inssurancemanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HealthInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;

    private int sex;         // 1 for male, 0 for female

    private double bmi;

    private int children;    // Number of children/dependents

    private int smoker;      // 1 for smoker, 0 for non-smoker

    private double predictionResult; // Predicted insurance cost

    private long userId ;
}
