package com.example.pens.service;

import com.example.pens.domain.request.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity register(UserDTO request);
    ResponseEntity validationLogin(UserDTO request);
}
