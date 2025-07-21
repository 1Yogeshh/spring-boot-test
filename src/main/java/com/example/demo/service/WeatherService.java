package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.WeatherInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WeatherService {
    private final static String API_KEY = "c6551711cb660f70a3e5fe1f9c757da1";

    private static final String BASE_URL = "http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public String getWeather(String city) {
        String url = BASE_URL.replace("API_KEY", API_KEY).replace("CITY", city);
        // Here you would typically make an HTTP request to the weather API using the
        // constructed URL
        // For simplicity, we are returning the URL as a string
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public WeatherInfo parseWeather(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        String city = root.path("location").path("name").asText();
        String country = root.path("location").path("country").asText();
        int temp = root.path("current").path("temperature").asInt();
        int feelsLike = root.path("current").path("feelslike").asInt();
        int humidity = root.path("current").path("humidity").asInt();
        int windSpeed = root.path("current").path("wind_speed").asInt();
        String description = root.path("current").path("weather_descriptions").get(0).asText();

        return new WeatherInfo(city, country, temp, description, humidity, feelsLike, windSpeed);
    }
}
