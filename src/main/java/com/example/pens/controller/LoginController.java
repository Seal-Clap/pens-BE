package com.example.pens.controller;

import com.example.pens.domain.request.UserDTO;
import com.example.pens.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity showValidation(@RequestBody UserDTO request) {
        return userService.validationLogin(request);
    }
}
