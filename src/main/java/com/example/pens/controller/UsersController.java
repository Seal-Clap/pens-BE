package com.example.pens.controller;

import com.example.pens.domain.auth.Token;
import com.example.pens.domain.request.UserRequest;
import com.example.pens.service.UserService;
import com.example.pens.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity identify() { return userService.identify(); }
}
