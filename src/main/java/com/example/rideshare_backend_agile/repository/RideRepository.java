package com.example.rideshare_backend_agile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rideshare_backend_agile.entity.Ride;

public interface RideRepository extends JpaRepository<Ride, Integer> {
}
