package com.example.lab10.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @NotBlank(message = "Nazwa uzytkownika jest wymagana")
    @Size(min = 3, message = "Nazwa uzytkownika musi miec co najmniej 3 znaki")
    private String username;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Niepoprawny format email")
    private String email;

    @NotBlank(message = "Haslo jest wymagane")
    @Size(min = 6, message = "Haslo musi miec co najmniej 6 znakow")
    @com.example.lab10.validation.NoSpaces
    private String password;

    // Gettery i Settery
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}