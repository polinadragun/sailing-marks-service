package com.polinadragun.sailingmarks.controller;

import com.polinadragun.sailingmarks.dto.*;
import com.polinadragun.sailingmarks.entity.User;
import com.polinadragun.sailingmarks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // ---------- JWT PROTECTED ENDPOINTS ----------

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserInfo(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@AuthenticationPrincipal User user,
                                                   @RequestBody UpdateUserRequest update) {
        return ResponseEntity.ok(userService.updateUser(user, update));
    }
}