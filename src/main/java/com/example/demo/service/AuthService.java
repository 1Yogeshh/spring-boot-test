package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    // private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(User user) {
        log.debug("Registering user: {}", user.getUsername());
        try {
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return "Username is required!";
            }

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return "Password is required!";
            }

            // if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            //     return "Username already exists!";
            // }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return "Registration successful!";
        } catch (Exception e) {
            // TODO: handle exception
            log.info("hello world" + e.getMessage());
            log.error("Registration failed: {}", user.getUsername());
            log.debug("Debugging registration process");
            log.trace(null, e);
            return "Registration failed due to an error!";
        }

    }

    public String login(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                return "Login successful!";
            }
        }
        return "Invalid username or password!";

    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
