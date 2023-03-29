package com.example.pens.controller;

import com.example.pens.domain.UserRequest;
import com.example.pens.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final UserService userService;

    @PostMapping("/login")
    public String showValidation(@RequestBody UserRequest request) {
        return userService.validationLogin(request);
    }
}
