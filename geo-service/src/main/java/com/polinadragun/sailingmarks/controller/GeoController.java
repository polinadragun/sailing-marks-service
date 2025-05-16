package com.polinadragun.sailingmarks.controller;

import com.polinadragun.sailingmarks.dto.WeatherData;
import com.polinadragun.sailingmarks.service.GeoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geo")
public class GeoController {

    private final GeoService geoService;

    public GeoController(GeoService geoService) {
        this.geoService = geoService;
    }

    @GetMapping("/weather")
    public WeatherData getWeatherData(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        return geoService.getWeatherData(lat, lon);
    }
}