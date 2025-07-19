package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
public class UserDetailServiceTest {

    // @Autowired
    @InjectMocks
    private UserDetailServiceImp userDetailServiceImp;

    // @MockBean
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Initialize mocks and other setup if necessary
        // MockitoAnnotations.initMocks(this);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        User user = User.builder()
                .username("yogesh")
                .password("passwordkumar")
                .roles(new ArrayList<>())
                .build();

        when(userRepository.findByUsername("yogesh")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailServiceImp.loadUserByUsername("yogesh");

        // Assert
        assertNotNull(userDetails);
        assertEquals("yogesh", userDetails.getUsername());
        assertEquals("passwordkumar", userDetails.getPassword());
    }
}
