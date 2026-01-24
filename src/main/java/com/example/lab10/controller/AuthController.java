package com.example.lab10.controller;

import com.example.lab10.dto.LoginRequest;
import com.example.lab10.dto.TokenRefreshRequest;
import com.example.lab10.dto.TokenRefreshResponse;
import com.example.lab10.model.RefreshToken;
import com.example.lab10.service.CustomUserDetailsService;
import com.example.lab10.service.JwtService;
import com.example.lab10.service.RefreshTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

// Importy do logowania
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          CustomUserDetailsService userService,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public TokenRefreshResponse login(@RequestBody LoginRequest request) {
        // Logujemy próbę (INFO)
        logger.info("Próba logowania użytkownika: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Logujemy sukces (INFO)
            logger.info("Udane logowanie użytkownika: {}", request.getUsername());

            var userDetails = userService.loadUserByUsername(request.getUsername());
            String accessToken = jwtService.generateToken(userDetails);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());

            return new TokenRefreshResponse(accessToken, refreshToken.getToken());

        } catch (Exception e) {
            // Logujemy porażkę (WARN) - np. złe hasło
            logger.warn("Nieudane logowanie dla użytkownika: {}. Błąd: {}", request.getUsername(), e.getMessage());
            throw e; // Rzucamy błąd dalej, żeby użytkownik dostał 403/401
        }
    }

    @PostMapping("/refresh")
    public TokenRefreshResponse refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(userService.loadUserByUsername(user.getUsername()));
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());
                    return new TokenRefreshResponse(accessToken, newRefreshToken.getToken());
                })
                .orElseThrow(() -> {
                    logger.warn("Próba odświeżenia tokena nieudanym tokenem: {}", requestRefreshToken);
                    return new RuntimeException("Refresh token is not in database!");
                });
    }
}