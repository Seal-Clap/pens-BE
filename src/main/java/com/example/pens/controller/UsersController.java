package com.example.pens.controller;

import com.example.pens.domain.auth.Token;
import com.example.pens.domain.request.UserRequest;
import com.example.pens.jwt.JwtFilter;
import com.example.pens.jwt.JwtTokenProvider;
import com.example.pens.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }

    @PostMapping("/register")
    public ResponseEntity Register(@RequestBody UserRequest request) {
        return userService.register(request);
    }
}
