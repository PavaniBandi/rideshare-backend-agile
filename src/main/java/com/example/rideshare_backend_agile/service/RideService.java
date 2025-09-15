package com.example.rideshare_backend_agile.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rideshare_backend_agile.entity.Ride;
import com.example.rideshare_backend_agile.entity.User;
import com.example.rideshare_backend_agile.repository.RideRepository;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    public Ride bookRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public Ride updateStatus(int rideId, Ride.Status status, String driverId) {
        Ride ride = rideRepository.findById(rideId).orElseThrow();
        ride.setStatus(status);

        // If status is ACCEPTED and driverId is provided, assign the driver
        if (status == Ride.Status.ACCEPTED && driverId != null) {
            ride.setDriverId(Integer.valueOf(driverId));
        }

        return rideRepository.save(ride);
    }

    public Ride getRideById(int rideId) {
        return rideRepository.findById(rideId).orElse(null);
    }

    public List<Ride> getActiveRides() {
        return rideRepository.findAll().stream().filter(r -> r.getStatus() != Ride.Status.COMPLETED).toList();
    }

    public List<Ride> getRidesForUser(User user) {
        return rideRepository.findAll().stream()
                .filter(r -> (r.getRiderId() == user.getId()) || (r.getDriverId() == (user.getId())))
                .toList();
    }

}
