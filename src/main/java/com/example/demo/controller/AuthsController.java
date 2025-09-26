package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserDetailServiceImp;
import com.example.demo.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

import com.example.demo.model.User;

import com.example.demo.service.AuthsService;
import com.example.demo.service.RedisService;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthsController {

    @Autowired
    private AuthsService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    // login endpoint with jwt auth contoler
    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
        UserDetails userDetails = userDetailServiceImp.loadUserByUsername(loginUser.getUsername());
        Map<String, String> tokens = authService.generateTokens(userDetails.getUsername());
        System.out.println(tokens + "username: " + userDetails.getUsername());
        return tokens.get("accessToken");
    }

    // Refresh endpoint
    @PostMapping("/refresh")
    public Map<String, String> refreshToken(@RequestParam String refreshToken) throws Exception {
        String username = jwtUtil.extractUsername(refreshToken);

        // Get stored token from Redis
        String storedToken = redisService.get("refreshToken:" + username);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new RuntimeException("Refresh token invalid or expired");
        }

        String newAccessToken = jwtUtil.generateAccessToken(username);
        return Map.of("accessToken", newAccessToken);
    }
}
