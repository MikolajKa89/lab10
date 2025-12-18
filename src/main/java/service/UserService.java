package com.example.lab10.service;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Wstrzykujemy repozytorium przez konstruktor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metoda do tworzenia użytkownika
    public User registerUser(String username, String email, String password) {
        User user = new User(username, email, password);
        // Tutaj w przyszłości dodasz kodowanie hasła!
        return userRepository.save(user);
    }

    // Prosta metoda logowania (tylko przykład)
    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }
}