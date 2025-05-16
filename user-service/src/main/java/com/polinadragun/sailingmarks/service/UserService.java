package com.polinadragun.sailingmarks.service;

import com.polinadragun.sailingmarks.dto.UpdateUserRequest;
import com.polinadragun.sailingmarks.dto.UserKafkaResponse;
import com.polinadragun.sailingmarks.dto.UserResponse;
import com.polinadragun.sailingmarks.entity.User;
import com.polinadragun.sailingmarks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse getUserInfo(User user) {
        return new UserResponse(user.getLogin(), user.getEmail(), user.getUsername());
    }

    public UserResponse updateUser(User user, UpdateUserRequest request) {
        user.setUsername(request.username());
        user.setEmail(request.email());
        userRepository.save(user);
        return new UserResponse(user.getLogin(), user.getEmail(), user.getUsername());
    }

    public User findByLogin(String login) {
        return userRepository.findById(login).orElse(null);
    }

    public void createUserFromKafka(UserKafkaResponse request) {
        if (!userRepository.existsById(request.login())) {
            User user = User.builder()
                    .login(request.login())
                    .email(request.email())
                    .passwordHash(request.passwordHash())
                    .username(request.username())
                    .build();
            userRepository.save(user);
        }
    }
}