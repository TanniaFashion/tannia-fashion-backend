package com.tanniafashion.tanniafashion.security.models;

public class AuthResponse {
    private Long id;
    private String token;
    private String username;
    private String email;

    public AuthResponse(Long id, String token, String username, String email) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }
    
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
