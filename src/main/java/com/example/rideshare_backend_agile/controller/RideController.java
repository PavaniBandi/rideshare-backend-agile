package com.example.rideshare_backend_agile.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rideshare_backend_agile.entity.Ride;
import com.example.rideshare_backend_agile.entity.User;
import com.example.rideshare_backend_agile.service.RideService;
import com.example.rideshare_backend_agile.service.UserService;

@RestController
@RequestMapping("/rides")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class RideController {

    @Autowired
    private RideService rideService;

    @Autowired
    private UserService userService;

    @PostMapping("/book")
    public ResponseEntity<?> bookRide(@RequestBody Ride ride, @AuthenticationPrincipal UserDetails userDetails) {
        User rider = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        ride.setRiderId(rider.getId());
        ride.setStatus(Ride.Status.REQUESTED);
        Ride saved = rideService.bookRide(ride);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/status/{rideId}")
    public ResponseEntity<?> updateStatus(@PathVariable int rideId, @RequestBody Map<String, String> req) {
        Ride.Status status = Ride.Status.valueOf(req.get("status"));
        String driverId = req.get("driverId");

        Ride updated = rideService.updateStatus(rideId, status, driverId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/status/{rideId}")
    public ResponseEntity<?> getRide(@PathVariable int rideId) {
        Ride ride = rideService.getRideById(rideId);
        if (ride == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/history")
    public ResponseEntity<?> rideHistory(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Ride> rides = rideService.getRidesForUser(user);
        return ResponseEntity.ok(rides);
    }

    @GetMapping("/requested")
    public ResponseEntity<?> getActiveRides() {
        return ResponseEntity.ok(rideService.getActiveRides());
    }

}
