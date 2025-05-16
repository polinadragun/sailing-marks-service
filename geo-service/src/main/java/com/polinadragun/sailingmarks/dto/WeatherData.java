package com.polinadragun.sailingmarks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData {
    private double latitude;
    private double longitude;
    private double windSpeed;
    private String windDirection;

    private double currentSpeed;
    private String currentDirection;

    private double visibility;
}
