package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    // ✅ Add this to fetch refresh token by username
    Optional<RefreshToken> findByUsername(String username);

    // Optional: delete refresh token by username
    void deleteByUsername(String username);
}
