package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.City;
import com.example.demo.model.WeatherInfo;
import com.example.demo.service.RedisService;
import com.example.demo.service.UserDetailServiceImp;
import com.example.demo.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RedisService redisService;

    @PostMapping
    public ResponseEntity<?> getWeather(@RequestBody City city) {
        // Cache hit check
        String cachedResponse = redisService.get(city.getName());
        if (cachedResponse != null) {
            System.out.println("Cache hit for city: " + city.getName());
            return ResponseEntity.ok(cachedResponse);
        }

        try {
            String json = weatherService.getWeather(city.getName());
            WeatherInfo info = weatherService.parseWeather(json);

            String response = String.format(
                    "Hello, %s! Here's the weather for %s:\nTemperature: %s°C\nFeels Like: %s°C\nHumidity: %s%%\nCondition: %s\nWind: %s km/h",
                    info.getName(), info.getName(), info.getTemperature(), info.getFeelsLike(),
                    info.getHumidity(), info.getDescription(), info.getWindSpeed());

            // Save to Redis
            redisService.set(city.getName(), response, 3600L);

            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error parsing weather data.");
        }
    }

    @GetMapping
    public String weatherPage() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        // Authorities me se ROLE_ prefix hata do
        String role = auth.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .findFirst() // agar multiple roles hain, first le lo
                .orElse("USER");
        // System.out.println("Current User: " + auth.getName());
        // System.out.println("Authorities: " + auth.getAuthorities());
        return "✅ Google login successful! You reached /weather" + " " + auth.getName() + " " + role;
    }

}
