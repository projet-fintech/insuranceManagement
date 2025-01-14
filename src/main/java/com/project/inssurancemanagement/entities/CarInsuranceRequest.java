package com.project.inssurancemanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CarInsuranceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int driverAge;

    private int driverExperience;

    private int previousAccidents;

    private int annualMileage;

    private int carManufacturingYear;

    private int carAge;

    private long userId;
}
