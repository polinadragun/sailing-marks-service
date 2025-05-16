package com.polinadragun.sailingmarks.kafka;

import com.polinadragun.sailingmarks.dto.UserKafkaRequest;
import com.polinadragun.sailingmarks.dto.UserKafkaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserRequest(UserKafkaRequest request) {
        kafkaTemplate.send("user-request-topic", request.requestId(), request);
    }

    public void sendUserCreated(UserKafkaResponse user) {
        kafkaTemplate.send("user-created-topic", user.requestId(), user);
    }
}
