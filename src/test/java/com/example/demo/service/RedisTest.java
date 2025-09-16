package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testRedis() {
        redisTemplate.opsForValue().set("l", "zhangsan");
        
        Object name = redisTemplate.opsForValue().get("google");
        // System.out.println(redisTemplate.opsForValue().get("lastname"));

        System.out.println(name);

        // Object name = redisTemplate.opsForValue().get("name");
        // System.out.println(name);

    }

 


}
