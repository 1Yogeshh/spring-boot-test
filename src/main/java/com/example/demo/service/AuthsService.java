package com.example.demo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.utils.JwtUtil;

@Service
public class AuthsService {

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RedisService redisService;

    public Map<String, String> generateTokens(String username) {

    // 1️⃣ Check Redis first
    String existingToken = redisService.get("refreshToken:" + username);

    String refreshToken;
    if (existingToken != null) {
        refreshToken = existingToken; // reuse existing valid refresh token
    } else {
        refreshToken = jwtUtils.generateRefreshToken(username);

        // Save in DB
        RefreshToken tokenEntity = refreshTokenRepository.findByUsername(username)
            .orElse(new RefreshToken());
        tokenEntity.setUsername(username);
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        refreshTokenRepository.save(tokenEntity);

        // Save in Redis for 30 days
        redisService.set("refreshToken:" + username, refreshToken, 30L * 24 * 60 * 60);
    }

    // Generate access token
    String accessToken = jwtUtils.generateAccessToken(username);

    return Map.of(
        "accessToken", accessToken,
        "refreshToken", refreshToken
    );
}


    // Refresh access token using refresh token
    public String refreshAccessToken(String refreshToken) throws Exception {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshToken);

        if (tokenOpt.isEmpty()) {
            throw new Exception("Invalid Refresh Token");
        }

        RefreshToken tokenEntity = tokenOpt.get();

        if (tokenEntity.getExpiryDate().before(new Date())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new Exception("Refresh Token Expired");
        }

        return jwtUtils.generateAccessToken(tokenEntity.getUsername());
    }
}
