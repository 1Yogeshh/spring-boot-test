package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    
    @Autowired
    private Dog dog;

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, " + dog.fun();
    }
}
