package com.example.pens.service;

import com.example.pens.domain.UserRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity register(UserRequest request);
    ResponseEntity validationLogin(UserRequest request);
}
