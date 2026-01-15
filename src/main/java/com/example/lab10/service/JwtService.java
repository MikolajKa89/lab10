package com.example.lab10.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // TO JEST SEKRETNY KLUCZ - normalnie trzyma się go w pliku konfiguracyjnym.
    // Musi być długi i skomplikowany (min. 256 bitów dla HS256).
    private static final String SECRET_KEY = "ToJestBardzoTajnyKluczKtoryMusiBycDlugiInaczejNieZadziala123456";

    // 1. Wyciąganie nazwy użytkownika z tokena (sprawdzanie biletu)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 2. Generowanie tokena (drukowanie biletu)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // W środku zapisujemy login
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data wydania
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Ważny przez 24h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Podpisz naszym kluczem
                .compact();
    }

    // 3. Sprawdzanie czy token jest ważny
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Metody pomocnicze (bebechy biblioteki)
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
                // Prosty trick, żeby zamienić nasz tekst na klucz w Base64 (dla uproszczenia Labu)
                java.util.Base64.getEncoder().encodeToString(SECRET_KEY.getBytes())
        );
        return Keys.hmacShaKeyFor(keyBytes);
    }
}