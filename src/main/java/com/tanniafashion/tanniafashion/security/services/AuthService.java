package com.tanniafashion.tanniafashion.security.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanniafashion.tanniafashion.models.User;
import com.tanniafashion.tanniafashion.repositories.UserRepository;
import com.tanniafashion.tanniafashion.security.JwtUtil;
import com.tanniafashion.tanniafashion.security.models.AuthRequest;
import com.tanniafashion.tanniafashion.security.models.AuthResponse;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public AuthService(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        try {
            System.out.println("Intentando autenticar usuario: " + request.getUsername());

            if (authenticationManager == null) {
                System.out.println("AuthenticationManager is null!");
                throw new RuntimeException("AuthenticationManager not initialized");
            } else {
                System.out.println("AuthenticationManager is initialized!");
            }

            User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found: " + request.getUsername()));

            if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                System.out.println("Password missmatch");
                throw new RuntimeException("Invalid credentials: Password missmatch");
            } else {
                System.out.println("Password match!");
            }

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            System.out.println("Authenticated: " + authentication.isAuthenticated());
            System.out.println("Authorities: " + authentication.getAuthorities());

            if(!authentication.isAuthenticated()) {
                System.out.println("Authentication failed for user: " + request.getUsername());
                throw new RuntimeException("Invalid credentials");
            }

            System.out.println("Authentication successful for user: " + request.getUsername());

            List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

            String token = jwtUtil.generateToken(request.getUsername(), roles);
            
            return new AuthResponse(user.getId(), token, user.getUsername(), user.getEmail());

        } catch (AuthenticationException exception) {
            System.out.println("Login failed for user: " + request.getUsername() + ". Error: " + exception.getMessage());
            throw new RuntimeException("Invalid credentials");
        }
    }
}
