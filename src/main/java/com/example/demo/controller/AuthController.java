package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserDetailServiceImp;
import com.example.demo.utils.jwtUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImp userDetailServiceImp;

    @Autowired
    private jwtUtils jwtUtils;

    public AuthController(AuthService service) {
        this.service = service;
    }

    // Register
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return service.register(user);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginUser) {
        // return service.login(loginUser.getUsername(), loginUser.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
            UserDetails userDetails = userDetailServiceImp.loadUserByUsername(loginUser.getUsername());  
            
            String token = jwtUtils.generateToken(userDetails.getUsername());
            System.out.println("JWT Token: " + token);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

    }

}
