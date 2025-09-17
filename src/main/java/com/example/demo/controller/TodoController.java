package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.model.User;
import com.example.demo.service.TodoService;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.demo.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/todos")
@Tag(name = "Todos api", description = "APIs for managing todos")
public class TodoController {

    private final TodoService todoService;

    private final AuthService authService;

    public TodoController(TodoService todoService, AuthService authService) {
        this.todoService = todoService;
        this.authService = authService;
    }

    // ✅ POST /todos/add
    @PostMapping("/add")
    public Todo addTodo(@RequestBody Todo todo) {
        // SecurityContextHolder.getContext().getAuthentication();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return todoService.addTodo(todo, username);
    }

    // ✅ GET /todos/all
    @GetMapping("/all")
    public List<Todo> getAllTodos() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByUsername(username);
        return user.getTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByUsername(username);

        return user.getTodos().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .map(todo -> ResponseEntity.ok(todo))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String id, @RequestBody Todo updateTodo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByUsername(username);

        boolean ownsTodo = user.getTodos().stream().anyMatch(todo -> todo.getId().equals(id));

        if (!ownsTodo) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 if not your todo
        }

        Todo updated = todoService.updateTodo(id, updateTodo);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.getUserByUsername(username);
        return user.getTodos().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .map(todo -> ResponseEntity.ok(todoService.deleteTodo(id, username)))
                .orElse(ResponseEntity.notFound().build());
    }
}
