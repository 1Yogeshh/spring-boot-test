package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.City;
import com.example.demo.model.WeatherInfo;
import com.example.demo.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public ResponseEntity<?> getWeather(@RequestBody City city) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            String json = weatherService.getWeather(city.getName());
            WeatherInfo info = weatherService.parseWeather(json);

            String response = String.format(
                    "Hello, %s! Here's the weather for %s:\nTemperature: %d°C\nFeels Like: %d°C\nHumidity: %d%%\nCondition: %s\nWind: %d km/h",
                    username, info.getName(), info.getTemperature(), info.getFeelsLike(),
                    info.getHumidity(), info.getDescription(), info.getWindSpeed());

            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error parsing weather data.");
        }
    }

}
