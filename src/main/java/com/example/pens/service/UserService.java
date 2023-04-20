package com.example.pens.service;

import com.example.pens.domain.auth.Token;
import com.example.pens.domain.request.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    ResponseEntity register(UserRequest request);
    ResponseEntity login(UserRequest userRequest);
    ResponseEntity identify(Token token);
}
