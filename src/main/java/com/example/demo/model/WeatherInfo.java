package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfo {
    private String name;
    private String country;
    private int temperature;
    private String description;
    private int humidity;
    private int feelsLike;
    private int windSpeed;

}
