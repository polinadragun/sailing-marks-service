package com.polinadragun.sailingmarks.kafka;

import com.polinadragun.sailingmarks.dto.UserKafkaResponse;
import com.polinadragun.sailingmarks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaRegListener {

    private final UserService userService;

    @KafkaListener(
            topics = "user-created-topic",
            groupId = "user-service",
            containerFactory = "userKafkaListenerContainerFactory"
    )
    public void handleUserCreated(UserKafkaResponse request) {
        userService.createUserFromKafka(request);
    }
}