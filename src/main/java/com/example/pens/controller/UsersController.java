package com.example.pens.controller;

import com.example.pens.domain.request.UserDTO;
import com.example.pens.service.UserService;
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
    public ResponseEntity login(@Valid @RequestBody UserDTO userRequest) {
        return userService.login(userRequest);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserDTO request) {
        return userService.register(request);
    }

    @GetMapping("/identity")
    public ResponseEntity identify() { return userService.identify(); }

    @GetMapping("/groups")
    public ResponseEntity getGroups(@RequestBody UserDTO request) { return userService.getGroups(request); }
}
