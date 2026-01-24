package com.example.lab10.dto;

public class TokenRefreshRequest {
    private String refreshToken;

    // Gettery i Settery
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}