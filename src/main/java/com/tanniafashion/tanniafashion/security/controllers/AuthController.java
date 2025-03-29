package com.tanniafashion.tanniafashion.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanniafashion.tanniafashion.models.User;
import com.tanniafashion.tanniafashion.security.models.AuthRequest;
import com.tanniafashion.tanniafashion.security.models.AuthResponse;
import com.tanniafashion.tanniafashion.security.services.AuthService;   
import com.tanniafashion.tanniafashion.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Authentication Operations")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already taken");
        }

        try {
            userService.saveUser(user);
            return new ResponseEntity<>("Register successfully", HttpStatus.CREATED);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during registration: " + exception.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        System.out.println("Login request received for username: " + request.getUsername());
        try {
            AuthResponse response = authService.authenticate(request);
            System.out.println("Login successful for user: " + request.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            System.out.println("Login failed for user: " + request.getUsername() + ". Error: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalidad credentials");
        }
    }
}
