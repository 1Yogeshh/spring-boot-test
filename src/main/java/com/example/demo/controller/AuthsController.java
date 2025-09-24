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

import lombok.extern.slf4j.Slf4j;

import com.example.demo.model.User;

import com.example.demo.service.AuthsService;

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

    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
            UserDetails userDetails = userDetailServiceImp.loadUserByUsername(loginUser.getUsername());
            Map<String, String> tokens = authService.generateTokens(userDetails.getUsername());
            return tokens.get("accessToken");
    }

    // Refresh endpoint
    @PostMapping("/refresh")
    public Map<String, String> refreshToken(@RequestParam String refreshToken) throws Exception {
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return Map.of("accessToken", newAccessToken);
    }
}
