package com.example.pens.service;

import com.example.pens.domain.UserRequest;

public interface UserService {
    String register(UserRequest request);
    String validationLogin(UserRequest request);
}
