package com.polinadragun.sailingmarks.dto;

public record UserKafkaResponse(String requestId, String login, String email, String passwordHash, String username) {
}