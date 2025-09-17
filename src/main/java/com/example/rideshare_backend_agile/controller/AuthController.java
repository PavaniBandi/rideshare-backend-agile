package com.example.rideshare_backend_agile.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rideshare_backend_agile.entity.User;
import com.example.rideshare_backend_agile.security.Jwtutil;
import com.example.rideshare_backend_agile.service.UserService;

import lombok.Data;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private Jwtutil jwtutil;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        User user = User.builder().name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole()).build();
        userService.registerUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User Registered Successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean valid = userService.validateCredentials(request.getEmail(), request.getPassword());
        if (!valid) {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
        User user = userService.findByEmail(request.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        String token = jwtutil.generateToken(user.getEmail(), claims);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("name", user.getName());
        response.put("id", user.getId());
        return ResponseEntity.ok(response);
    }

    @Data
    public static class SignupRequest {

        private String email;
        private String name;
        private String password;
        private String role;
    }

    @Data
    public static class LoginRequest {

        private String email;
        private String password;
    }

}

//Ride Controller->Service: bookRide,getRideByID, updateStatus
//Permit access for h2
// Test login and signup
