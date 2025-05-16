package com.polinadragun.sailingmarks.model;

import lombok.Data;

@Data
public class GeoData {
    private double lat;
    private double lon;
    private String windDirection;
    private double windSpeed;
    private String currentDirection;
    private double currentSpeed;

}