package com.polinadragun.sailingmarks.service;

import com.polinadragun.sailingmarks.dto.WeatherData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoService {

    @Value("${stormglass.api.key}")
    private String stormGlassApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable("weather")
    public WeatherData getWeatherData(double lat, double lon) {
        String sgUrl = String.format(
                "https://api.stormglass.io/v2/weather/point?lat=%f&lng=%f&params=visibility,windSpeed,windDirection,currentSpeed,currentDirection&source=noaa",
                lat, lon
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", stormGlassApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> sgResponse = restTemplate.exchange(sgUrl, HttpMethod.GET, entity, String.class);
        JSONObject sgJson = new JSONObject(sgResponse.getBody());

        JSONObject hourData = sgJson.getJSONArray("hours").getJSONObject(0);

        double windSpeed = hourData.getJSONObject("windSpeed").getDouble("noaa");
        double windDeg = hourData.getJSONObject("windDirection").getDouble("noaa");
        String windDirection = degToCompass(windDeg);

        double currentSpeed = hourData.getJSONObject("currentSpeed").getDouble("noaa");
        double currentDeg = hourData.getJSONObject("currentDirection").getDouble("noaa");
        String currentDirection = degToCompass(currentDeg);

        double visibility = hourData.getJSONObject("visibility").getDouble("noaa");

        return new WeatherData(lat, lon, windSpeed, windDirection, currentSpeed, currentDirection, visibility);
    }

    private String degToCompass(double deg) {
        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
        return directions[(int)Math.round(((deg % 360) / 45)) % 8];
    }
}