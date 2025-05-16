package com.polinadragun.sailingmarks;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GeoServiceApplication {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}