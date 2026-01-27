package com.example.lab10.service;

import com.example.lab10.model.User;
import com.example.lab10.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldHashPasswordBeforeSavingUser() {
        // 1. ARRANGE (Przygotowanie danych)
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("plainPassword123");

        // Udajemy, że enkoder zamienia "plainPassword123" na "HASHED_XYZ"
        when(passwordEncoder.encode("plainPassword123")).thenReturn("HASHED_XYZ");
        // Udajemy, że repozytorium po prostu zwraca ten sam obiekt
        when(userRepository.save(any(User.class))).thenReturn(user);

        // 2. ACT (Wykonanie akcji)
        userService.registerUser(user);

        // 3. ASSERT (Sprawdzenie)
        // Sprawdzamy, czy hasło w obiekcie zostało zmienione na zahashowane
        assertEquals("HASHED_XYZ", user.getPassword());

        // Sprawdzamy, czy enkoder został faktycznie użyty
        verify(passwordEncoder).encode("plainPassword123");
        // Sprawdzamy, czy metoda save została wywołana
        verify(userRepository).save(user);
    }
}