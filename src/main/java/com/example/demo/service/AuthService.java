package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.dto.UserDTO;
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

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(UserDTO userDTO) {
        log.debug("Registering user: {}", userDTO.getUsername());
        try {
            if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
                return "Username is required!";
            }

            if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
                return "Password is required!";
            }

            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                return "Username already exists!";
            }
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return "Registration successful!";
        } catch (Exception e) {
            // TODO: handle exception
            log.info("hello world" + e.getMessage());
            log.error("Registration failed: {}", userDTO.getUsername());
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
