package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;


import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    // Register
    @PostMapping("/register")
    public String register( @RequestBody User user) {
        return service.register(user);
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestBody User loginUser) {
        return service.login(loginUser.getUsername(), loginUser.getPassword());
    }

    // get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
}

