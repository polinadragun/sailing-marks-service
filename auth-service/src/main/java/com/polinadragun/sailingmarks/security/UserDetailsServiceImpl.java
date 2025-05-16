package com.polinadragun.sailingmarks.security;

import com.polinadragun.sailingmarks.dto.UserKafkaRequest;
import com.polinadragun.sailingmarks.dto.UserKafkaResponse;
import com.polinadragun.sailingmarks.kafka.UserKafkaClient;
import com.polinadragun.sailingmarks.kafka.UserKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserKafkaProducer userKafkaProducer;
    private final UserKafkaClient userKafkaClient;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        String requestId = UUID.randomUUID().toString();
        UserKafkaRequest request = new UserKafkaRequest(requestId, login);

        userKafkaProducer.sendUserRequest(request);

        UserKafkaResponse response = userKafkaClient.awaitResponse(requestId);

        if (response == null) {
            throw new UsernameNotFoundException("User not found: " + login);
        }

        return User
                .withUsername(response.login())
                .password(response.passwordHash())
                .roles("USER")
                .build();
    }
}