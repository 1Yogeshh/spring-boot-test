package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserServiceFunctionality() {
        User user = userRepository.findByUsername("yogesh").orElse(null);
        assertNotNull(user, "User should not be null");
        assertTrue(!user.getUsername().isEmpty(), "Username should not be empty");
    }

    @Test
    public void testUserTodo(){
        User user = userRepository.findByUsername("yogesh").orElse(null);
        assertTrue(!user.getTodos().isEmpty(), "User should have todos");
    }

    @Disabled
    @Test
    public void add(){
        assertEquals(4, 2+2);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 2, 3",
        "2, 3, 5",
        "3, 5, 8",
        "4, 6, 10",
        "5, 7, 12"
    })
    public void test( int a , int b , int expect){
        assertEquals(expect, a + b);
    }
}
