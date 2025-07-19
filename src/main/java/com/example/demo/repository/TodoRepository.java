package com.example.demo.repository;

import com.example.demo.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TodoRepository extends MongoRepository<Todo, String> {
    // no extra code needed for now
}
