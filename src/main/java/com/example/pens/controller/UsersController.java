package com.example.pens.controller;

import com.example.pens.domain.auth.Token;
import com.example.pens.domain.request.UserRequest;
import com.example.pens.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRequest request) {
        return userService.register(request);
    }

    @PostMapping("/identity")
    public ResponseEntity identify(@RequestBody Token token) { return userService.identify(token); }
}
