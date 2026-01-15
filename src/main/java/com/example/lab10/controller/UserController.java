package com.example.lab10.controller;

import com.example.lab10.dto.CreateUserRequest;
import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        // 1. Tworzymy prawdziwego użytkownika
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        // 2. Kodujemy hasło (To bardzo ważne!)
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Zapisujemy do bazy
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("Użytkownik zarejestrowany pomyślnie!");
    }
}