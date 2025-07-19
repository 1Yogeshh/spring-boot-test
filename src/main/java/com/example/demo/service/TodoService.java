package com.example.demo.service;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.repository.TodoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    private final AuthService authService;

    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, AuthService authService, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Transactional
    public Todo addTodo(Todo todo, String username) {

        try {
            User user = authService.getUserByUsername(username);
            // todo.setUser(user);
            Todo saved = todoRepository.save(todo);
            user.getTodos().add(saved);
            userRepository.save(user);
            return saved;

        } catch (Exception e) {
            System.err.println("Error while adding todo: " + e.getMessage());
            throw new RuntimeException("Failed to add todo: " + e.getMessage());
        }
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodoById(String id) {
        return todoRepository.findById(id).orElse(null);
    }

    public Todo updateTodo(String id, Todo updatetodo) {
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    if (updatetodo.getTitle() != null) {
                        existingTodo.setTitle(updatetodo.getTitle());
                    }
                    // existingTodo.setTitle(updatetodo.getTitle());
                    if (updatetodo.getDescription() != null) {
                        existingTodo.setDescription(updatetodo.getDescription());
                    }

                    return todoRepository.save(existingTodo);
                })
                .orElse(null);
    }

    public Todo deleteTodo(String id, String username) {
        if (todoRepository.existsById(id)) {
            User user = authService.getUserByUsername(username);
            user.getTodos().removeIf(todo -> todo.getId().equals(id));
            userRepository.save(user);
            todoRepository.deleteById(id);
        }
        return null;
    }
}
