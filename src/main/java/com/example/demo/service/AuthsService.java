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

    // Generate both tokens and save refresh token
    public Map<String, String> generateTokens(String username) {
        String accessToken = jwtUtils.generateAccessToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setUsername(username);
        tokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        refreshTokenRepository.save(tokenEntity);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
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
