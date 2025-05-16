package com.polinadragun.sailingmarks.kafka;
import org.springframework.stereotype.Component;
import com.polinadragun.sailingmarks.dto.UserKafkaRequest;
import com.polinadragun.sailingmarks.dto.UserKafkaResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class UserKafkaClient {

    private final Map<String, CompletableFuture<UserKafkaResponse>> futures = new ConcurrentHashMap<>();

    public void handleResponse(UserKafkaResponse response) {
        var future = futures.remove(response.requestId());
        if (future != null) {
            future.complete(response);
        }
    }

    public UserKafkaResponse awaitResponse(String requestId) {
        CompletableFuture<UserKafkaResponse> future = new CompletableFuture<>();
        futures.put(requestId, future);
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            futures.remove(requestId);
            return null;
        }
    }
}

