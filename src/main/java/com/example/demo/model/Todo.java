package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "todos") // MongoDB collection name
@Data
public class Todo {
    @Id
    private String id;
    private String title;
    private String description;

    // @DBRef
    // private User user; 
}
