package com.polinadragun.sailingmarks.kafka;

import com.polinadragun.sailingmarks.dto.UserKafkaRequest;
import com.polinadragun.sailingmarks.dto.UserKafkaResponse;
import com.polinadragun.sailingmarks.entity.User;
import com.polinadragun.sailingmarks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumer {

    private final UserService userService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "user-requests", groupId = "user-service")
    public void handleUserRequest(UserKafkaRequest request) {
        User user = userService.findByLogin(request.login());
        if (user != null) {
            UserKafkaResponse response = new UserKafkaResponse(
                    request.requestId(),
                    user.getLogin(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getUsername()
            );
            kafkaTemplate.send("user-responses", response);
        }
    }
}