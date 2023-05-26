package com.example.pens.service;

import com.example.pens.domain.request.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity register(UserDTO request);
    ResponseEntity login(UserDTO userRequest);
    String getUserName(Integer userId);
    ResponseEntity identify();
    ResponseEntity getGroups(Integer userId);
}
