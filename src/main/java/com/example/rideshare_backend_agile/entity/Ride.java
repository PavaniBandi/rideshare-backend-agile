package com.example.rideshare_backend_agile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String pickupLocation;
    private String dropLocation;
    private Double fare;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String paymentMode;

    private int riderId;
    private int driverId;

    public enum Status {
        REQUESTED, ACCEPTED, PICKED, COMPLETED
    }

}
