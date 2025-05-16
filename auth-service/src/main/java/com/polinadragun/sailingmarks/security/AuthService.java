package com.polinadragun.sailingmarks.security;

import com.polinadragun.sailingmarks.dto.*;

import com.polinadragun.sailingmarks.kafka.UserKafkaClient;
import com.polinadragun.sailingmarks.kafka.UserKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserKafkaProducer userKafkaProducer;
    private final UserKafkaClient userKafkaClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        String encodedPassword = passwordEncoder.encode(registerRequest.password());

        String requestId = UUID.randomUUID().toString();
        UserKafkaResponse userEvent = new UserKafkaResponse(
                requestId,
                registerRequest.login(),
                registerRequest.email(),
                encodedPassword,
                registerRequest.username()
        );

        userKafkaProducer.sendUserCreated(userEvent);
        return new AuthResponse(jwtService.generateTokenFromLogin(registerRequest.login()));
    }

    public AuthResponse authenticate(AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.login(), authRequest.password()
                )
        );

        String login = authentication.getName();
        String token = jwtService.generateTokenFromLogin(login);

        return new AuthResponse(token);
    }
}